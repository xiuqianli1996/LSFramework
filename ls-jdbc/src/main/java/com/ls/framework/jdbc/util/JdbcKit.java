package com.ls.framework.jdbc.util;

import com.ls.framework.common.function.CheckedFunction;
import com.ls.framework.jdbc.exception.LSJdbcException;
import com.ls.framework.jdbc.session.ConnectionContext;

import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;

public class JdbcKit {

    public static <T> T executeQuery(Connection connection, String sql, Object[] params, CheckedFunction<ResultSet, T> resultSetFunction) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = createPreparedStatementWithParams(connection, sql, params);
            resultSet = preparedStatement.executeQuery();//查询结果

            return resultSetFunction.apply(resultSet);
        } catch (Throwable e) {
            throw new LSJdbcException(e);
        } finally {
            close(resultSet, preparedStatement, connection);
        }
    }

    public static PreparedStatement createPreparedStatementWithParams(Connection connection, String sql, Object[] params) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        fillPreparedStatementParas(preparedStatement, params);
        return preparedStatement;
    }

    public static void close(ResultSet resultSet, PreparedStatement preparedStatement, Connection connection){

        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        } catch (SQLException e) {
            throw new LSJdbcException(e);
        } finally {
            ConnectionContext.close(connection);
        }
    }


    public static void fillPreparedStatementParas(PreparedStatement preparedStatement, Object[] params) throws SQLException {
        if (null == params)
            return;
        int pos = 1;
        for (Object obj : params){
            if (obj == null) {
                preparedStatement.setObject(pos++, null);
            } else if (obj.getClass().isArray()) { //填充数组数据
                int len = Array.getLength(obj);
                for (int j = 0; j < len; j++) {
                    preparedStatement.setObject(pos++, Array.get(obj, j));
                }
            } else if (obj instanceof Collection) {
                for (Object item : (Collection) obj) {
                    preparedStatement.setObject(pos++, item);
                }
            } else {
                preparedStatement.setObject(pos++, obj);
            }
        }
    }
}
