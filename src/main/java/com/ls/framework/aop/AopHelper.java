package com.ls.framework.aop;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import com.ls.framework.annotation.LSAround;
import com.ls.framework.annotation.LSAspect;
import com.ls.framework.annotation.LSBean;
import com.ls.framework.ioc.BeanFactory;
import com.ls.framework.utils.ClassUtil;
import com.ls.framework.utils.StringKit;
import net.sf.cglib.proxy.Enhancer;

/**
 * 维护AopActionChain缓存、初始化Aop、Aop强化方法
 * @author Administrator
 *
 */
public class AopHelper {
	
	
	public static <T> T enhance(T obj) {
		try {
			return (T) Enhancer.create(obj.getClass(), new AopCallback(obj));
		} catch (Exception e) {
			e.printStackTrace();
		}
        return obj;
    }

    public static void enhanceAll() {
		System.out.println("---------- Aop enhancing ------------");
		createClassAopActionChains();
        Map<String, Object> beanMap = BeanFactory.getBeanMap();
        //对bean容器里的bean进行强化
        for (Map.Entry<String, Object> entry : beanMap.entrySet()) {
        	Object obj = entry.getValue();
        	// || !obj.getClass().isAnnotationPresent(LSBean.class)
        	if (obj.getClass().isAssignableFrom(AopAction.class)) {
        		continue; //不强化Aop拦截类和没有LSBean注解的类（通过配置文件注入的类）
			}
            Object enhanceInstance = enhance(obj);
            beanMap.put(entry.getKey(), enhanceInstance);
//            BeanFactory.getInstance().getBean(entry.getKey());
        }
        System.out.println("---------- Aop enhancing success ------------");
    }
	
    public static void createClassAopActionChains() {
    	//扫描所有继承AopAction的类，如果有LSAspect注解就添加到缓存
    	List<Class<?>> aopActionClasses = ClassUtil.getClassesBySuper(AopAction.class);
    	for (Class<?> clazz : aopActionClasses) {
    		if (!clazz.isAnnotationPresent(LSAspect.class) || !clazz.isAnnotationPresent(LSBean.class)) {
    			continue;
    		}
    		
    		LSAspect aspect = clazz.getAnnotation(LSAspect.class);
    		String pkg = aspect.value();
    		String cls = aspect.cls();
    		
    		if (StringKit.notBlank(cls)) {
    			String clsName = pkg + "." + cls;
    			try {
					Class<?> targetClass = Class.forName(clsName);
					addClassAopAction(targetClass, clazz);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		} else {
    			ClassUtil.getClassesByPkg(pkg).forEach(targetClass -> {
					addClassAopAction(targetClass, clazz);
				});
			}
    		
    	}
    	//扫描LSAround注解的类
    	List<Class<?>> classes = ClassUtil.getClassesByAnnotation(LSAround.class);
    	for (Class<?> clazz : classes) {
    		if (!BeanFactory.containsKey(clazz.getName()))
    			continue;
			LSAround lsAround = clazz.getAnnotation(LSAround.class);
			for (Class<?> aopActionClass : lsAround.value())
				addClassAopAction(clazz, aopActionClass);
		}
	}

	private static void addClassAopAction(Class<?> targetClass, Class<?> aopActionClass) {
		List<AopAction> actionList = getClassAopActionChainOrNew(targetClass);
		AopAction aopAction = (AopAction) BeanFactory.getBean(aopActionClass);
		actionList.add(aopAction);
		putClassAopActionChain(targetClass, actionList);
	}
	
	private final static Map<Class<?>, List<AopAction>> classAopActionChainMap = new HashMap<>();
	private final static Map<Method, List<AopAction>> aopActionCache = new ConcurrentHashMap<>();

	public static List<AopAction> getAopActionChain(Method key) {
		return aopActionCache.get(key);
	}

	public static void putAopActionChain(Method key, List<AopAction> value) {
		aopActionCache.put(key, value);
	}

	public static List<AopAction> getClassAopActionChain(Class<?> key) {
		return classAopActionChainMap.get(key);
	}
	
	public static List<AopAction> getClassAopActionChainOrNew(Class<?> key) {
		List<AopAction> actionList = getClassAopActionChain(key);
		if (actionList == null) {
			actionList = new ArrayList<>();
		}
		return actionList;
	}
	
	public static List<AopAction> putClassAopActionChain(Class<?> key, List<AopAction> value) {
		return classAopActionChainMap.put(key, value);
	}

	
	

}
