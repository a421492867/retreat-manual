package org.lordy.concurrent.shared;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 使用ThreadLocal维持线程封闭
 */
public class ConnectionDispenser {

    static final String DB_URL = "jdbc:mysql://localhost:3306/database";

    private ThreadLocal<Connection> connectionHolder =
            new ThreadLocal<Connection>(){
                public Connection initValue(){
                    try {
                        return DriverManager.getConnection(DB_URL);
                    }catch (SQLException e){
                        throw new RuntimeException("Unable to acquire Connection, " + e);
                    }
                }
            };
    public Connection get(){
        return connectionHolder.get();
    }
}
