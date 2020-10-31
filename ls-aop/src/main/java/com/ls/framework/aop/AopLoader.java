package com.ls.framework.aop;

import com.ls.framework.aop.annotation.LSAround;
import com.ls.framework.aop.annotation.LSAspect;
import com.ls.framework.aop.support.AopAction;
import com.ls.framework.aop.support.AopContainer;
import com.ls.framework.common.exception.LSException;
import com.ls.framework.common.kit.ClassKit;
import com.ls.framework.common.kit.CollectionKit;
import com.ls.framework.common.kit.ObjectKit;
import com.ls.framework.common.kit.StrKit;
import com.ls.framework.core.ApplicationContext;
import com.ls.framework.core.loader.Loader;
import com.ls.framework.ioc.IocLoader;
import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.Annotation;
import java.util.*;

@Slf4j
public class AopLoader implements Loader {

    public static Map<Class<?>, Set<Class<?>>> NEED_AOP_CLASSES = new WeakHashMap<>();

    private static final int ORDER = IocLoader.ORDER - 10;

    @Override
    public void doLoad(ApplicationContext context, Set<Class<?>> classSet) {
        processAspectAnnotation(classSet);
        processAroundAnnotation(classSet);
        initIfNotFoundIocLoader();
    }

    private void processAroundAnnotation(Set<Class<?>> classSet) {
        // 处理在类上有@LSAround注解的需要代理的类
        ClassKit.getClassesByAnnotation(classSet, LSAround.class)
                .stream()
                .filter(ClassKit::notAbstract)
                .forEach(targetClass -> {
                    LSAround around = targetClass.getAnnotation(LSAround.class);
                    putNeedAopClass(targetClass, around.value());
                });
    }

    private void processAspectAnnotation(Set<Class<?>> classSet) {
        // 处理直接在@LSAspect注解定义的需要代理的类
        ClassKit.getClassesByAnnotation(classSet, LSAspect.class)
                .stream()
                .filter(ClassKit::notAbstract)
                .forEach(clazz -> {
                    if (!AopAction.class.isAssignableFrom(clazz)) {
                        throw new LSException("The class is not assignable from AopAction, can not annotated by LSAspect, class:" + clazz.getName());
                    }
                    LSAspect aspect = clazz.getAnnotation(LSAspect.class);
                    Class<?>[] targetClasses = aspect.targetClasses();
                    if (CollectionKit.notEmpty(targetClasses)) {
                        // 强化所有指定的目标类
                        Arrays.stream(targetClasses).forEach(targetClass -> putNeedAopClass(targetClass, clazz));
                    }
                    String[] packages = aspect.packages();
                    if (CollectionKit.notEmpty(packages)) {
                        // 标记包名下的所有类需要强化
                        Set<String> packageSet = new HashSet<>(Arrays.asList(packages));
                        packageSet.stream().filter(StrKit::notBlank).forEach(packageName -> {
                            ClassKit.getClassesByPkg(classSet, packageName)
                                    .stream()
                                    .filter(ClassKit::notAbstract)
                                    .forEach(targetClass -> putNeedAopClass(targetClass, clazz));
                        });
                    }
                    Class<? extends Annotation>[] targetAnnotations = aspect.targetAnnotations();
                    if (CollectionKit.notEmpty(targetAnnotations)) {
                        // 强化类或者方法上有目标注解的类
                        Arrays.stream(targetAnnotations).forEach(annotation -> {
                            classSet.stream()
                                    .filter(c -> ClassKit.hasAnnotation(c, annotation))
                                    .forEach(targetClass -> putNeedAopClass(targetClass, clazz));
                        });
                    }
                });
    }

    private void putNeedAopClass(Class<?> targetClass, Class<?>... newActionClasses) {
        Set<Class<?>> actionClassSet = NEED_AOP_CLASSES.computeIfAbsent(targetClass, k -> new HashSet<>());
        actionClassSet.addAll(Arrays.asList(newActionClasses));
    }

    /**
     * 如果没有IocLoader类就代表没有依赖ioc组件，自己手动把AopAction类反射获取实例注册到AOP拦截器缓存容器中
     */
    @SuppressWarnings("unchecked")
    private void initIfNotFoundIocLoader() {
        try {
            Class.forName("com.ls.framework.ioc.IocLoader");
            return;
        } catch (ClassNotFoundException e) {
            log.info("not found ioc module, init AOP actions by reflect");
        }
        NEED_AOP_CLASSES.forEach((targetClass, aopActionClasses) -> {
            aopActionClasses.forEach(actionClass -> {
                AopAction action = ObjectKit.getInstance((Class<AopAction>)actionClass);
                AopContainer.putAopAction(targetClass, action);
            });
        });
    }


    @Override
    public int order() {
        return ORDER;
    }

}
