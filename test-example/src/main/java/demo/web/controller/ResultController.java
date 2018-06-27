package demo.web.controller;

import com.ls.framework.core.annotation.LSAutowired;
import com.ls.framework.core.annotation.LSBean;
import com.ls.framework.core.ioc.BeanContainer;
import com.ls.framework.core.utils.ClassUtil;
import com.ls.framework.jdbc.session.SqlSession;
import com.ls.framework.web.annotation.LSRequestMapping;
import com.ls.framework.web.annotation.LSResponseBody;
import demo.web.bean.ResultBean;
import demo.web.mapper.ResultMapper;

import java.util.List;
import java.util.stream.Collectors;

@LSBean
@LSRequestMapping("/result")
@LSResponseBody
public class ResultController {

    @LSAutowired
    private ResultMapper resultMapper;

    @LSRequestMapping("list")
    public List<ResultBean> list() {
        return resultMapper.selectList();
    }

    @LSRequestMapping("calsses")
    public List<String> getClasses() {
        return ClassUtil.scanClassListByPkg("com.ls.framework").stream().map(Class::getName).collect(Collectors.toList());
    }

    @LSRequestMapping("sqlsession")
    public SqlSession getSqlSessqion() {
        return BeanContainer.getBean("sqlSession");
    }
}
