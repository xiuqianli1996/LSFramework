package com.ls.framework.core.aop;

import com.ls.framework.core.annotation.LSAround;
import com.ls.framework.core.annotation.LSAspect;
import com.ls.framework.core.annotation.LSBean;
import com.ls.framework.core.ioc.BeanContainer;
import com.ls.framework.core.utils.ClassUtil;
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
