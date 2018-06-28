package com.ls.framework.jdbc.base;

import com.ls.framework.core.annotation.LSBean;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

public class SimpleDataSource implements DataSource{

    public static ThreadLocal<Connection> connectionHolder = new ThreadLocal<Connection>();

    protected String uri;

    protected String username;

    protected String password;

    protected String driver;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public SimpleDataSource() {
        super();
    }


    public SimpleDataSource(String uri, String username, String password,
                            String driver) {
        super();
        this.uri = uri;
        this.username = username;
        this.password = password;
        this.driver = driver;
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Connection connecting() throws SQLException {
        return connecting(username, password);
    }

    private Connection connecting(String username, String password) throws SQLException{
        return DriverManager.getConnection(uri, username, password);
    }

    @Override
    public synchronized Connection getConnection() throws SQLException{
//        Connection conn = connectionHolder.get();
//        if (conn == null){
//            conn = connecting();
//            connectionHolder.set(conn);
////			conn.getClientInfo().put("status", ConnectionStatus.CONNECTED);
//        }
        return connecting();
    }

    @Override
    public synchronized Connection getConnection(String username, String password)
            throws SQLException {
//        Connection conn = connectionHolder.get();
//        if (conn == null){
//            conn = connecting(username, password);
//            connectionHolder.set(conn);
////			conn.getClientInfo().put("status", ConnectionStatus.CONNECTED);
//        }
        return connecting(username, password);
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {

    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {

    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }

}