package com.ls.framework.core.aop;

import com.ls.framework.core.annotation.LSAround;
import com.ls.framework.core.annotation.LSAspect;
import com.ls.framework.core.annotation.LSBean;
import com.ls.framework.core.ioc.BeanContainer;
import com.ls.framework.core.utils.ClassUtil;
import com.ls.framework.core.utils.CollectionKit;
import com.ls.framework.core.utils.StringKit;
import net.sf.cglib.proxy.Enhancer;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 初始化Aop、Aop强化方法
 * @author Administrator
 *
 */
public class AopHelper {
	
	
	public static <T> T enhance(T obj) {
		if (!ClassUtil.hasAnnotation(obj.getClass(), LSAround.class)
				&& CollectionKit.isEmptyCollection(AopContainer.getClassAopActionChain(obj.getClass()))) {
			return obj;
			//切面列表为空且整个类里没有LSAround注解的类不加强，会牺牲启动时的速度，避免有不需要加强的类里的public属性丢失
			// ，当然个人还是习惯所有属性私有加getter/setter
		}
		if (Enhancer.isEnhanced(obj.getClass())) {
			return obj;
		}
		try {
			return (T) Enhancer.create(obj.getClass(), new AopCallback(obj));
		} catch (Exception e) {
			e.printStackTrace();
		}
        return obj;
    }

	public static void addClassAopAction(Class<?> targetClass, Class<?> aopActionClass) {
		List<AopAction> actionList = AopContainer.getClassAopActionChainOrNew(targetClass);
		AopAction aopAction = (AopAction) BeanContainer.getBean(aopActionClass);
		actionList.add(aopAction);
		AopContainer.putClassAopActionChain(targetClass, actionList);
	}

}
