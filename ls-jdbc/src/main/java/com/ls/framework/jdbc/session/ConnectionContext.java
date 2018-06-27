package com.ls.framework.jdbc.session;

import com.ls.framework.jdbc.exception.LSJdbcException;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionContext {

    private static ThreadLocal<Connection> connectionHolder = new ThreadLocal<>();
    private static ThreadLocal<Boolean> transactedHolder = new ThreadLocal<Boolean>() {
        @Override
        protected Boolean initialValue() {
            return false;
        }
    };

    public static boolean inTranscation() {
        return transactedHolder.get();
    }

    public static Connection get() {
        Connection connection = connectionHolder.get();
        if (transactedHolder.get() && connection != null) {
            try {
                connection.setAutoCommit(false);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return connection;
    }

    public static void set(Connection value) {
        if (transactedHolder.get()) {
            try {
                value.setAutoCommit(false);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        connectionHolder.set(value);
    }

    public static void close() {
        if (transactedHolder.get()) {
            return;
        }
        try {
            Connection connection = connectionHolder.get();
            connectionHolder.remove();
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new LSJdbcException(e);
        }

    }

    public static void beginTransaction() {
        get();//设置AutoCommit为false
        transactedHolder.set(true);
    }

    public static void commitTransaction() throws SQLException{
        Connection conn = connectionHolder.get();
        if (conn != null){
            if (!conn.getAutoCommit()){
                conn.commit();
            }
            transactedHolder.set(false);
            close(); //提交事务后关闭连接
        }
    }

    public static void rollbackTransaction(){
        Connection conn = connectionHolder.get();
        try {
            if (conn != null){
                if(!conn.getAutoCommit()){
                    conn.rollback();
                }
                transactedHolder.set(false);
                close();//回滚后关闭连接
            }
        }catch(SQLException e){
            e.printStackTrace();
            throw new LSJdbcException(e);
        }
    }

}
