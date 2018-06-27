package demo.web.mapper;

import com.ls.framework.jdbc.annotation.LSMapper;
import com.ls.framework.jdbc.annotation.LSQuery;
import demo.web.bean.ResultBean;

import java.util.List;

@LSMapper
public interface ResultMapper {

    @LSQuery("SELECT * FROM  tbl_result")
    List<ResultBean> selectList();

}
