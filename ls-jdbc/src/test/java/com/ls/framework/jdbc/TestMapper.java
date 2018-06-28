package com.ls.framework.jdbc;

import com.ls.framework.jdbc.annotation.LSDbParam;
import com.ls.framework.jdbc.annotation.LSModifying;
import com.ls.framework.jdbc.annotation.LSQuery;

public interface TestMapper {

    @LSQuery("SELECT * FROM  tbl_result")
    String select();

    @LSQuery("INSERT INTO tbl_result VALUES(${day}, ${result})")
    @LSModifying
    String insert(@LSDbParam("result") String result, @LSDbParam("day") String day);

    @LSQuery("UPDATE tbl_result SET result=${result} WHERE day = ${day}")
    @LSModifying
    String update(@LSDbParam("result") String result, @LSDbParam("day") String day);

    @LSQuery("DELETE FROM tbl_result WHERE day = ${day}")
    @LSModifying
    String delete(@LSDbParam("day") String day);

}
