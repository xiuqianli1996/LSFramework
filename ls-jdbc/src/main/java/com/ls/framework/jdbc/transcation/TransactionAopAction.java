package com.ls.framework.jdbc.transcation;

import com.ls.framework.aop.annotation.LSAspect;
import com.ls.framework.aop.support.AopAction;
import com.ls.framework.aop.support.Invocation;
import com.ls.framework.jdbc.annotation.LSTransacted;
import com.ls.framework.jdbc.session.ConnectionContext;

import java.lang.reflect.Method;

@LSAspect(targetAnnotations = LSTransacted.class)
public class TransactionAopAction implements AopAction {

    private boolean isTransacted(Method method) {
        return method.isAnnotationPresent(LSTransacted.class) || method.getDeclaringClass().isAnnotationPresent(LSTransacted.class);
    }

    @Override
    public Object around(Invocation invocation) throws Throwable {
        if (!isTransacted(invocation.getMethod())) {
            //没有事务注解直接执行
            return invocation.invoke();
        }

        if (ConnectionContext.inTransaction()) {
            // 如果已经在事务中直接执行
            return invocation.invoke();
        }
        try {
            ConnectionContext.beginTransaction();
            Object result = invocation.invoke();
            ConnectionContext.commitTransaction();
            return result;
        } catch (Exception e) {
            ConnectionContext.rollbackTransaction();
            throw e;//异常继续往上抛
        }
    }
}
