
package com.zli19.onlineshopping.util;

import java.sql.Connection;
import java.sql.SQLException;



/**
 *
 * @author zhiku
 */
public class Transaction {
    
    public static void begin(){
        Connection connection = DataSourceConnection.getConnection();
        try {
            connection.setAutoCommit(false);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }   
    
    public static void commit(){
        try {
            DataSourceConnection.getConnection().commit();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public static void end(){
        DataSourceConnection.closeConnection();
    }
}
