package com.ls.framework.jdbc;

import com.ls.framework.jdbc.annotation.LSDbParam;
import com.ls.framework.jdbc.annotation.LSMapper;
import com.ls.framework.jdbc.annotation.LSModifying;
import com.ls.framework.jdbc.annotation.LSQuery;

import java.util.List;

@LSMapper
public interface TestMapper2 {

    @LSQuery("SELECT * FROM  tbl_result")
    List<ResultBean> selectList();

    @LSQuery("SELECT * FROM  tbl_result WHERE day in (${days})")
    List<ResultBean> selectList(@LSDbParam("days") String[] days);

    @LSQuery("SELECT count(*) FROM  tbl_result")
    long selectCount();


    @LSQuery("SELECT * FROM  tbl_result limit 1")
    ResultBean selectOne();

    @LSQuery("SELECT * FROM  tbl_result WHERE day = ${day} limit 1")
    ResultBean selectOneByDay(@LSDbParam("day")String day);

    @LSQuery("SELECT * FROM  tbl_result")
    ResultBean[] selectArray();

    @LSQuery("INSERT INTO tbl_result(`day`, `col_result`) VALUES(${day}, ${result})")
    @LSModifying
    long insert(@LSDbParam("result") String result, @LSDbParam("day") String day);

    @LSQuery("UPDATE tbl_result SET col_result=${result} WHERE day = ${day}")
    @LSModifying
    long update(@LSDbParam("result") String result, @LSDbParam("day") String day);

    @LSQuery("UPDATE tbl_result SET col_result=${result} WHERE day = ${day}")
    @LSModifying
    void updateWithoutReturn(@LSDbParam("result") String result, @LSDbParam("day") String day);

    @LSQuery("DELETE FROM tbl_result WHERE day = ${day}")
    @LSModifying
    long delete(@LSDbParam("day") String day);

}
