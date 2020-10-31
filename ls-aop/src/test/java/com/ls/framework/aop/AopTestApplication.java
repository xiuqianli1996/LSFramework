package com.ls.framework.aop;


import com.ls.framework.aop.component.TestComponent;
import com.ls.framework.aop.component.TestComponent1;
import com.ls.framework.core.ApplicationContext;
import com.ls.framework.core.annotation.LSApplication;
import com.ls.framework.ioc.BeanContainer;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

@LSApplication
public class AopTestApplication {

    public static void main(String[] args) {
        ApplicationContext context = ApplicationContext.run(AopTestApplication.class);
        TestComponent testComponent = BeanContainer.sharedContainer().getBean(TestComponent.class);
        TestComponent1 testComponent1 = BeanContainer.sharedContainer().getBean(TestComponent1.class);
        testComponent.test();
        System.out.println();
        testComponent.testClearAllClassAction();
        System.out.println();
        testComponent.testClearAspect2();

        System.out.println();
        System.out.println();
        System.out.println();
        testComponent1.test();
//        BeanContainer.sharedContainer().getBean(TestComponent1.class).test();

    }

}
