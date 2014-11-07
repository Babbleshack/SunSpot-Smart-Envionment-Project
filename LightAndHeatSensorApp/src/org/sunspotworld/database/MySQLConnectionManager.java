/*
 * JDBC connection handler for MySQL
 * Dominic Lindsay
 */
package org.sunspotworld.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Babblebase
 */
public class MySQLConnectionManager implements IDatabaseConnectionManager 
{
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://127.0.0.1:33060/testing";
    private static final String USERNAME = "babbleshack";
    private static final String PASSWORD = "testing";
    private Connection connection = null;

    public MySQLConnectionManager()
    {
        try
        {
            Class.forName(JDBC_DRIVER);
        } catch (Exception e) {
            System.err.println("MySQL driver not found HERE" + e);
        }
        this.connect();
    }
    public void connect()
    {
        try { 
            connection  =
                DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
        } catch (SQLException ex) {
            System.err.println("Failed to connect to DB" + ex);
        }
    }
    public void disconnect()
    {
        try
        {
            connection.close();
        } catch (Exception e) {
            System.err.println("connection failed to close! " + e);
        }
    } 
    /**
     * returns connection, if connection has not been set, a null 
     * pointer exception is thrown.
     * @return 
     */
    public Connection getConnection() {
        if(connection == null)
            throw new NullPointerException();
        return connection;
    }
}
