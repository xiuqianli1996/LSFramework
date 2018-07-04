package com.ls.framework.jdbc;

import com.ls.framework.core.annotation.LSBean;
import com.ls.framework.core.annotation.LSLoader;
import com.ls.framework.core.aop.AopAction;
import com.ls.framework.core.aop.AopContainer;
import com.ls.framework.core.ioc.BeanContainer;
import com.ls.framework.core.ioc.DependencyInjector;
import com.ls.framework.core.loader.Loader;
import com.ls.framework.core.utils.ClassUtil;
import com.ls.framework.jdbc.annotation.LSMapper;
import com.ls.framework.jdbc.annotation.LSTranscated;
import com.ls.framework.jdbc.exception.LSJdbcException;
import com.ls.framework.jdbc.session.SqlSession;
import com.ls.framework.jdbc.transcation.TransactionAopAction;

import java.util.List;
import java.util.Set;

@LSLoader(20)
public class JdbcLoader implements Loader {
    @Override
    public void doLoad(Set<Class<?>> classSet) {

//        String jdbcUrl = PropKit.get(Constants.CONFIG_DATASOURCE_URL);
//        String jdbcDriver = PropKit.get(Constants.CONFIG_DATASOURCE_DRIVER);

//        DataSource dataSource = BeanContainer.getBean("dataSource");
//        if (dataSource == null) {
//            throw new LSJdbcException("Datasource is null");
//        }
//
//        LSDbConfiguration configuration = new LSDbConfiguration();
//        configuration.setDataSource(dataSource);
//
//        JdbcExecutor executor = new DefaultJdbcExecutor(configuration.isMapUnderscoreToCamelCase());
//        configuration.setExecutor(executor);

        SqlSession sqlSession = BeanContainer.getBean("sqlSession"); //从Bean容器获取sqlsession
        if (sqlSession == null) {
            throw new LSJdbcException("sqlSession is null");
        }
        //将所有LSDao注解的类加入Bean容器
        Set<Class<?>> mapperClassList = ClassUtil.getClassesByAnnotation(classSet, LSMapper.class);
        for (Class<?> clazz : mapperClassList) {
            Object mapper = sqlSession.getMapper(clazz);
            BeanContainer.put(clazz.getName(), mapper);
        }

        DependencyInjector.injectAll(); //重新进行依赖注入

        //给所有LSBean注解并且类里包含LSTranscated注解的类加上事务拦截
        TransactionAopAction transactionAopAction = new TransactionAopAction();
        BeanContainer.put(TransactionAopAction.class.getName(), transactionAopAction);
        ClassUtil.getClassesByAnnotation(classSet, LSBean.class)
                .stream()
                .filter(clazz -> ClassUtil.hasAnnotation(clazz, LSTranscated.class))
                .forEach(clazz -> {
                    List<AopAction> list = AopContainer.getClassAopActionChainOrNew(clazz);
                    list.add(transactionAopAction);
                    AopContainer.putClassAopActionChain(clazz, list);
                });
    }


}
