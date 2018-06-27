package com.ls.framework.jdbc.transcation;

import com.ls.framework.core.aop.AopAction;
import com.ls.framework.core.aop.Invocation;
import com.ls.framework.jdbc.session.ConnectionContext;

public class TransactionAopAction extends AopAction {
    @Override
    public void invoke(Invocation invocation) throws Throwable {
        if (ConnectionContext.inTranscation()) {
            invocation.invoke();
        }
        try {
            ConnectionContext.beginTransaction();
            invocation.invoke();
            ConnectionContext.commitTransaction();
        } catch (Exception e) {
            ConnectionContext.rollbackTransaction();
            throw e;//异常继续往上抛
        }
    }
}
