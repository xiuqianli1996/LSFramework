package com.ls.framework.aop;

import java.util.*;

import com.ls.framework.annotation.LSAround;
import com.ls.framework.annotation.LSAspect;
import com.ls.framework.annotation.LSBean;
import com.ls.framework.ioc.BeanContainer;
import com.ls.framework.utils.ClassUtil;
import com.ls.framework.utils.StringKit;
import net.sf.cglib.proxy.Enhancer;

import static com.ls.framework.aop.AopContainer.getClassAopActionChainOrNew;
import static com.ls.framework.aop.AopContainer.putClassAopActionChain;

/**
 * 初始化Aop、Aop强化方法
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

		AopHelper aopHelper = new AopHelper();
		aopHelper.createChainsBySuper();
		aopHelper.createChainsByAnnotation();
		aopHelper.enhanceBeanContainer();

        System.out.println("---------- Aop enhancing success ------------");
    }

    private void enhanceBeanContainer() {
		Map<String, Object> beanMap = BeanContainer.getBeanMap();
		//对bean容器里的bean进行强化（生成代理对象）
		for (Map.Entry<String, Object> entry : beanMap.entrySet()) {
			Object obj = entry.getValue();
			// || !obj.getClass().isAnnotationPresent(LSBean.class)
			if (obj.getClass().isAssignableFrom(AopAction.class)) {
				continue; //不强化Aop拦截类
			}
			Object enhanceInstance = enhance(obj);
			beanMap.put(entry.getKey(), enhanceInstance);
		}
	}
	
    private void createChainsByAnnotation() {
    	//扫描LSAround注解的类
    	List<Class<?>> classes = ClassUtil.getClassesByAnnotation(LSAround.class);
    	for (Class<?> clazz : classes) {
    		if (!BeanContainer.containsKey(clazz.getName()))
    			continue;
			LSAround lsAround = clazz.getAnnotation(LSAround.class);
			for (Class<?> aopActionClass : lsAround.value())
				addClassAopAction(clazz, aopActionClass);
		}
	}

	private void createChainsBySuper() {
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
	}

	private static void addClassAopAction(Class<?> targetClass, Class<?> aopActionClass) {
		List<AopAction> actionList = getClassAopActionChainOrNew(targetClass);
		AopAction aopAction = (AopAction) BeanContainer.getBean(aopActionClass);
		actionList.add(aopAction);
		putClassAopActionChain(targetClass, actionList);
	}

}
