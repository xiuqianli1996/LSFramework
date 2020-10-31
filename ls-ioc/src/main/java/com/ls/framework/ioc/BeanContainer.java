package com.ls.framework.ioc;

import com.ls.framework.common.exception.LSException;
import com.ls.framework.common.kit.*;
import com.ls.framework.ioc.annotation.LSAutowired;
import com.ls.framework.ioc.annotation.LSBean;
import com.ls.framework.ioc.definition.BeanDefinition;
import com.ls.framework.ioc.kit.BeanDefinitionKit;
import com.ls.framework.ioc.listener.BeanContainerListener;
import com.ls.framework.ioc.listener.BeanLifeCircle;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Parameter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
public class BeanContainer {
    private static BeanContainer instance = new BeanContainer();
    private Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();
    private Map<Class<?>, Set<BeanDefinition>> classBeanDefinitionMap = new ConcurrentHashMap<>();

    private List<BeanLifeCircle> beanLifeCircles = new LinkedList<>();
    private List<BeanContainerListener> beanContainerListeners = new LinkedList<>();
//
//    @Setter
//    @Getter
//    private ApplicationContext applicationContext;

    public static BeanContainer sharedContainer() {
        return instance;
    }

//    public static List<Object> newBeanInstance(BeanDefinition beanDefinition) {
//
//    }

    public Object getBean(String name) {
        return Optional.ofNullable(beanDefinitionMap.get(name))
                .map(beanDefinition -> beanDefinition.getBean(this))
                .orElseThrow(() -> new LSException("can not get bean by name: " + name));
    }

    /**
     * 尝试强转一次，保证类型正确
     * @param name
     * @param clazz
     * @return
     */
    public <T> T getBean(String name, Class<T> clazz) {
        return clazz.cast(getBean(name));
    }

    /**
     * 只根据class获取bean，如果同一个class有多个实例会抛出异常
     * @param clazz
     * @return
     */
    public <T> T getBean(Class<T> clazz) {
        Set<BeanDefinition> beanDefinitions = classBeanDefinitionMap.getOrDefault(clazz, Collections.emptySet());
        if (beanDefinitions.isEmpty()) {
            throw new LSException("can not get bean by class: " + clazz.getName());
        }
        if (beanDefinitions.size() > 1) {
            throw new LSException("beans count more than 1, please get bean by name, class name: " + clazz.getName());
        }
        BeanDefinition beanDefinition = beanDefinitions.iterator().next();
        return (T) Objects.requireNonNull(beanDefinition.getBean(this), "can not get bean by class: " + clazz.getName());
    }

    /**
     * 根据class获取所有的bean实例
     * @param clazz
     * @return 如果没有对应的bean实例会返回空的set
     */
    public <T> Set<T> getBeans(Class<T> clazz) {
        return classBeanDefinitionMap.getOrDefault(clazz, Collections.emptySet()).stream()
                .map(beanDefinition -> beanDefinition.getBean(this))
                .filter(Objects::nonNull)
                .map(clazz::cast)
                .collect(Collectors.toSet());
    }

    public List<Object> resolveDependencyParams(Parameter[] parameters) {
        if (CollectionKit.isEmpty(parameters)) {
            return Collections.emptyList();
        }
        return Arrays.stream(parameters)
                .map(parameter -> {
                    if (!parameter.isAnnotationPresent(LSAutowired.class)) {
                        // 没有注解的参数直接根据class获取
                        return getBean(parameter.getType());
                    }
                    LSAutowired autowired = parameter.getAnnotation(LSAutowired.class);
                    String name = autowired.value();
                    if (StrKit.isBlank(name)) {
                        // 如果注解了但是没给名字也直接根据class获取
                        return getBean(parameter.getType());
                    }
                    // 给了名字尝试根据名字获取，并且尝试一次强转，要是取到的bean实例和参数所需类型不同可以直接抛出异常
                    return getBean(name, parameter.getType());
                })
                .collect(Collectors.toList());
    }

    public BeanContainer register(BeanDefinition beanDefinition) {
        for (BeanContainerListener listener : beanContainerListeners) {
            BeanDefinition ret = listener.processBeanDefinition(beanDefinition);
            if (ret != null) {
                beanDefinition = ret;
            }
        }
        BeanDefinition processedBeanDefinition = beanDefinition;
        BeanDefinition oldVal = beanDefinitionMap.putIfAbsent(processedBeanDefinition.getName(), processedBeanDefinition);

        Class<?> beanClass = processedBeanDefinition.getBeanClass();

        if (oldVal != null) {
            throw new LSException("definition name duplicate, name: " + processedBeanDefinition.getName() + ", class name: " + beanClass.getName());
        }
        // 根据class superClass interface注册
        registerByClass(beanClass, beanDefinition);
        ClassKit.doWithSuperClass(beanClass,clazz -> clazz != Object.class, clazz -> registerByClass(clazz, processedBeanDefinition));
        ClassKit.doWithInterfaces(beanClass, clazz -> registerByClass(clazz, processedBeanDefinition));

        // 扫描class里有LSBean注解的method
        scanBeanDefinitionFromClass(beanClass);
        return this;
    }

    public Set<BeanDefinition> getBeanDefinitionSet() {
        // 先去重
        Set<BeanDefinition> beanDefinitions = new HashSet<>(beanDefinitionMap.values());
        return Collections.unmodifiableSet(beanDefinitions);
    }

    public void initBeanLifeCircles() {
        getBeans(BeanLifeCircle.class).forEach(listener -> {
            if (beanLifeCircles.contains(listener)) {
                return;
            }
            beanLifeCircles.add(listener);
        });

        beanLifeCircles.sort(Comparator.comparingInt(BeanLifeCircle::order));
    }

    public void registerBeanContainerListener(BeanContainerListener listener) {
        if (beanContainerListeners.contains(listener)) {
            return;
        }
        beanContainerListeners.sort(Comparator.comparingInt(BeanContainerListener::order));
    }

    /**
     * 用于扫描LSBean注解的method
     * @param clazz
     */
    public void scanBeanDefinitionFromClass(Class<?> clazz) {
        ReflectKit.doWithMethod(clazz, method -> {
            LSBean lsBean = method.getAnnotation(LSBean.class);
            String beanName = StrKit.orDefault(lsBean.value(), method::getName);
            register(BeanDefinitionKit.from(clazz, method, beanName, lsBean.singleton()));
        }, method -> method.isAnnotationPresent(LSBean.class));
    }

    private void registerByClass(Class<?> clazz, BeanDefinition beanDefinition) {
        Set<BeanDefinition> beanDefinitions = classBeanDefinitionMap.computeIfAbsent(clazz, k -> new HashSet<>(1, 1));
        beanDefinitions.add(beanDefinition);
    }

    // 以下生命周期相关
    public Object beforeInitialize(BeanDefinition beanDefinition) {
        String name = beanDefinition.getName();
        Class<?> clazz = beanDefinition.getBeanClass();
        boolean singleton = beanDefinition.isSingleton();
        Object result = null;
        for (BeanLifeCircle listener : beanLifeCircles) {
            Object ret = listener.beforeInitialize(name, clazz, singleton, this);
            if (result == null) {
                result = ret;
            }
            log.debug("ioc lister: {}, beforeInitialize result: {}", listener.getClass().getName(), ret);
        }
        return result == null ? null : clazz.cast(result);
    }

    public Object afterInitialize(BeanDefinition beanDefinition, Object bean) {
        String name = beanDefinition.getName();
        boolean singleton = beanDefinition.isSingleton();
        Object result = bean;
        for (BeanLifeCircle listener : beanLifeCircles) {
            Object ret = listener.afterInitialize(name, result, singleton, this);
            if (ret != null) {
                result = ret;
            }
            log.debug("ioc lister: {}, afterInitialize result: {}", listener.getClass().getName(), ret);
        }
        return result;
    }

    public Object postPropertiesInject(BeanDefinition beanDefinition, Object bean) {
        String name = beanDefinition.getName();
        Object result = bean;
        for (BeanLifeCircle listener : beanLifeCircles) {
            Object ret = listener.postPropertiesInject(name, result, this);
            if (ret != null) {
                result = ret;
            }
            log.debug("ioc lister: {}, postPropertiesInject result: {}", listener.getClass().getName(), ret);
        }
        return result;
    }

    public Object afterPropertiesInject(BeanDefinition beanDefinition, Object bean) {
        String name = beanDefinition.getName();
        Object result = bean;
        for (BeanLifeCircle listener : beanLifeCircles) {
            Object ret = listener.afterPropertiesInject(name, result, this);
            if (ret != null) {
                result = ret;
            }
            log.debug("ioc lister: {}, afterPropertiesInject result: {}", listener.getClass().getName(), ret);
        }
        return result;
    }
}
