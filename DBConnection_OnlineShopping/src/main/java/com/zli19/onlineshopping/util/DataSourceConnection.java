
package com.zli19.onlineshopping.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;


/**
 *
 * @author zhiku
 */
public abstract class DataSourceConnection {
    private static HikariDataSource datasource;
    private static final ThreadLocal<Connection> CONN = new ThreadLocal<>();
    private static final HikariConfig CONFIG = new HikariConfig("/hikari.properties");

    static {
        datasource = new HikariDataSource(CONFIG);
    }
    
    public static Connection getConnection(){   
        if(CONN.get() == null){
            try {
                CONN.set(datasource.getConnection());
            } catch (SQLException ex) {
                throw new RuntimeException("Failed to get connection.");
            }
        }
        return CONN.get();        
    }
  
    public static void closeConnection(){
        try {
            CONN.get().close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        CONN.remove();
    } 
     
    public static void closeDataSource(){
        datasource.close();
    }
    
    public static void restartDataSource(){
        datasource = new HikariDataSource(CONFIG);
    }
}
