package com.ls.framework.jdbc.base;

import com.ls.framework.jdbc.exception.LSJdbcException;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

@Slf4j
public class SimpleDataSource implements DataSource{

    public static ThreadLocal<Connection> connectionHolder = new ThreadLocal<Connection>(); //todo remove

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
            throw new LSJdbcException(e);
        }
    }

    public SimpleDataSource(String uri, String username, String password,
                            String driver) {
        this.uri = uri;
        this.username = username;
        this.password = password;
        this.driver = driver;
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            throw new LSJdbcException(e);
        }
    }

    @Override
    public synchronized Connection getConnection() throws SQLException{
        return getConnection(username, password);
    }

    @Override
    public synchronized Connection getConnection(String username, String password)
            throws SQLException {
        return DriverManager.getConnection(uri, username, password);
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
        DriverManager.setLoginTimeout(seconds);
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return DriverManager.getLoginTimeout();
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw new SQLFeatureNotSupportedException();
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