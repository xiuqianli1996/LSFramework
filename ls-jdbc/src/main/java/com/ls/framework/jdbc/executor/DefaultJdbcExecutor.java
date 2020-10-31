package com.ls.framework.jdbc.executor;

import com.ls.framework.common.kit.CollectionKit;
import com.ls.framework.common.kit.StrKit;
import com.ls.framework.jdbc.exception.LSJdbcException;
import com.ls.framework.jdbc.executor.rowmap.BeanRowMapper;
import com.ls.framework.jdbc.util.JdbcKit;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultJdbcExecutor implements JdbcExecutor {

    private boolean mapUnderscoreToCamelCase = false;

    public DefaultJdbcExecutor(boolean mapUnderscoreToCamelCase) {
        this.mapUnderscoreToCamelCase = mapUnderscoreToCamelCase;
    }

    @Override
    public <T> List<T> queryBeanList(Connection connection, Class<T> clazz, String sql, Object[] paras){
//        List<Map<String, Object>> resultList = executeQuery(connection, sql, paras);
//        return JdbcRSConvertUtil.convertObjectList(clazz, resultList);
        return JdbcKit.executeQuery(connection, sql, paras, resultSet -> {
            List<T> result = new ArrayList<>();
            int index = 0;
            while (resultSet.next()) {
                result.add(new BeanRowMapper<T>(clazz).map(resultSet, index++));
            }
            return result;
        });
    }

    @Override
    public <T> T queryBean(Connection connection, Class<T> clazz, String sql, Object[] paras){
        List<T> list = queryBeanList(connection, clazz, sql, paras);
        return CollectionKit.isEmpty(list) ? null : list.get(0);
    }

    @Override
    public int queryInt(Connection connection, String sql, Object[] paras){
        return queryNumber(connection, sql, paras).intValue();
    }

    @Override
    public long queryLong(Connection connection, String sql, Object[] paras){
        return queryNumber(connection, sql, paras).longValue();
    }

    @Override
    public double queryDouble(Connection connection, String sql, Object[] paras){
        return queryNumber(connection, sql, paras).doubleValue();
    }

    @Override
    public float queryFloat(Connection connection, String sql, Object[] paras){
        return queryNumber(connection, sql, paras).floatValue();
    }

    @Override
    public long executeUpdate(Connection connection, String sql, Object[] paras){
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = JdbcKit.createPreparedStatementWithParams(connection, sql, paras);
            Integer code = preparedStatement.executeUpdate();

            //尝试加上插入数据返回插入的主键，暂时没有好的思路
//            if (sql.toLowerCase().contains("insert")) {
//                try {
//                    resultSet = preparedStatement.getGeneratedKeys();
//                    PreparedStatement statement = connection.prepareStatement("SELECT LAST_INSERT_ID()");
//                    if (resultSet.next()) {
//                        return resultSet.getLong(1);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    throw new LSJdbcException(e);
//                }
//            }

            return code.longValue();

        } catch (SQLException e) {
            throw new LSJdbcException(e);
        } finally {
            JdbcKit.close(resultSet, preparedStatement, connection);
        }
    }


    private Number queryNumber(Connection connection, String sql, Object[] paras){
        return JdbcKit.executeQuery(connection, sql, paras, resultSet -> {
            if (resultSet.next()) {
                return resultSet.getBigDecimal(1);
            }
            return new BigDecimal(0);
        });
    }

}
