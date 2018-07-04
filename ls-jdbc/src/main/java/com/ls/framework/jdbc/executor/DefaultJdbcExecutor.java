package com.ls.framework.jdbc.executor;

import com.ls.framework.core.utils.CollectionKit;
import com.ls.framework.core.utils.StringKit;
import com.ls.framework.jdbc.exception.LSJdbcException;
import com.ls.framework.jdbc.session.ConnectionContext;
import com.ls.framework.jdbc.util.JdbcRSConvertUtil;

import java.lang.reflect.Array;
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

    public long executeUpdate(Connection connection, String sql, Object[] paras){
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(sql);

            fillPreparedStatementParas(preparedStatement, paras);

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
            e.printStackTrace();
            throw new LSJdbcException(e);
        } finally {
            close(resultSet, preparedStatement, connection);
        }
    }

    public List<Map<String, Object>> executeQuery(Connection connection, String sql, Object[] paras){
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(sql);

            fillPreparedStatementParas(preparedStatement, paras);

            resultSet = preparedStatement.executeQuery();//查询结果

            return parseResultSet(resultSet);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new LSJdbcException(e);
        } finally {
            close(resultSet, preparedStatement, connection);
        }
    }

    public <T> List<T> queryBeanList(Connection connection, Class<T> clazz, String sql, Object[] paras){
        List<Map<String, Object>> resultList = executeQuery(connection, sql, paras);
        return JdbcRSConvertUtil.convertObjectList(clazz, resultList);
    }

    public <T> T queryBean(Connection connection, Class<T> clazz, String sql, Object[] paras){
        List<T> list = queryBeanList(connection, clazz, sql, paras);
        return CollectionKit.isEmptyCollection(list) ? null : list.get(0);
    }

    public Number queryNumber(Connection connection, String sql, Object[] paras){
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Number result = 0;
        try {
            preparedStatement = connection.prepareStatement(sql);

            fillPreparedStatementParas(preparedStatement, paras);

            resultSet = preparedStatement.executeQuery();//查询结果

            if(resultSet.next()){
                result = resultSet.getBigDecimal(1);
            }

            return result;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new LSJdbcException(e);
        } finally {
            close(resultSet, preparedStatement, connection);
        }
    }

    public int queryInt(Connection connection, String sql, Object[] paras){
        return queryNumber(connection, sql, paras).intValue();
    }

    public long queryLong(Connection connection, String sql, Object[] paras){
        return queryNumber(connection, sql, paras).longValue();
    }

    public double queryDouble(Connection connection, String sql, Object[] paras){
        return queryNumber(connection, sql, paras).doubleValue();
    }

    public float queryFloat(Connection connection, String sql, Object[] paras){
        return queryNumber(connection, sql, paras).floatValue();
    }

    private void close(ResultSet resultSet, PreparedStatement preparedStatement, Connection connection){

        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ConnectionContext.close();
    }

    private void fillPreparedStatementParas(PreparedStatement preparedStatement, Object[] paras) throws SQLException {
        if (null == paras)
            return;
        int pos = 1;
        for (Object obj : paras){
            if (obj.getClass().isArray()) { //填充数组数据
                int len = Array.getLength(obj);
                for (int j = 0; j < len; j++) {
                    preparedStatement.setObject(pos++, Array.get(obj, j));
                }
                continue;
            }
            preparedStatement.setObject(pos++, obj);
        }
    }

    private List<Map<String, Object>> parseResultSet(ResultSet resultSet) {
        List<Map<String, Object>> resultList = new ArrayList<>();

        try {
            while (resultSet.next()) {
                ResultSetMetaData metaData = resultSet.getMetaData();
                Map<String, Object> map = new HashMap<>();
                for (int i = 1; i <= metaData.getColumnCount(); i++) {
                    String name = metaData.getColumnName(i);
                    if (mapUnderscoreToCamelCase) {
                        name = StringKit.toCamelCase(name);
                    }
                    Object val = resultSet.getObject(i);
                    map.put(name, val);
                }
                resultList.add(map);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new LSJdbcException(e);
        }
        return resultList;
    }

}
