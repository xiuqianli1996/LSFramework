package com.ls.framework.jdbc.session;

import com.ls.framework.common.kit.StrKit;
import com.ls.framework.jdbc.config.Constants;
import com.ls.framework.jdbc.exception.LSJdbcException;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionContext {

    private static final ThreadLocal<String> DATASOURCE_NAME_HOLDER = ThreadLocal.withInitial(() -> Constants.DEFAULT_DATASOURCE_NAME);
    private static final ThreadLocal<Connection> connectionHolder = new ThreadLocal<>();
    private static final ThreadLocal<Boolean> transactedHolder = ThreadLocal.withInitial(() -> false);

    public static void setDatasourceName(String name) {
        if (StrKit.isEmpty(name)) {
            return;
        }
        DATASOURCE_NAME_HOLDER.set(name);
    }

    public static String getDatasourceName() {
        return DATASOURCE_NAME_HOLDER.get();
    }

    public static boolean inTransaction() {
        return transactedHolder.get();
    }

    public static Connection get() {
        Connection connection = connectionHolder.get();
        if (connection == null) {
            return null;
        }
        if (transactedHolder.get()) {
            try {
                connection.setAutoCommit(false);
            } catch (SQLException e) {
                throw new LSJdbcException(e);
            }
        }

        return connection;
    }

    public static void set(Connection value) {
        if (transactedHolder.get()) {
            try {
                value.setAutoCommit(false);
            } catch (SQLException e) {
                throw new LSJdbcException(e);
            }
        }
        connectionHolder.set(value);
    }

    public static void close(Connection connection) {
        if (transactedHolder.get()) {
            if (connection != null && connection != connectionHolder.get()) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new LSJdbcException(e);
                }
            }
            return;
        }
        Connection holdConnection = connectionHolder.get();
        connectionHolder.remove();
        if (holdConnection == null) {
            return;
        }
        try {
            holdConnection.close();
        } catch (SQLException e) {
            throw new LSJdbcException(e);
        }

    }

    public static void beginTransaction() {
        get();//设置AutoCommit为false
        transactedHolder.set(true);
    }

    public static void commitTransaction() throws SQLException{
        Connection conn = connectionHolder.get();
        if (conn == null){
            return;
        }
        if (!conn.getAutoCommit()){
            conn.commit();
        }
        transactedHolder.set(false);
        close(conn); //提交事务后关闭连接
    }

    public static void rollbackTransaction(){
        Connection conn = connectionHolder.get();
        if (conn == null) {
            return;
        }
        try {
            if(!conn.getAutoCommit()){
                conn.rollback();
            }
            transactedHolder.set(false);
            close(conn);//回滚后关闭连接
        }catch(SQLException e){
            throw new LSJdbcException(e);
        }
    }

}
