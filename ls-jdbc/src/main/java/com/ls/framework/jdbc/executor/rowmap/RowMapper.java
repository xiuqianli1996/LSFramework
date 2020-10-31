package com.ls.framework.jdbc.executor.rowmap;

import java.sql.ResultSet;

public interface RowMapper<T> {
    T map(ResultSet rs, int index);
}
