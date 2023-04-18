
package com.zli19.onlineshopping.dao.impl;

import com.zli19.onlineshopping.dao.IOrderDAO;
import com.zli19.onlineshopping.entity.Order;
import com.zli19.onlineshopping.util.DataSourceConnection;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;

/**
 *
 * @author zhiku
 */
public class OrderDAO extends GenericDAO<Order> implements IOrderDAO{
    
    public ArrayList<Order> queryByUserDateTime(String userName, Date orderDate, Time orderTime) {
      
        String sql = "SELECT * FROM orders WHERE userName = ? AND orderDate = ? AND orderTime = ?";
        
        Connection conn = DataSourceConnection.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<Order> rlist = new ArrayList<>();
        try {                
            ps = conn.prepareStatement(sql);
            //ps.setString(1, fieldName);
            ps.setString(1, userName);
            ps.setDate(2, orderDate);
            ps.setTime(3, orderTime);
            rs = ps.executeQuery();
            while(rs.next()){
                Constructor<Order> constructor = Order.class.getConstructor();
                Order order = constructor.newInstance();
                Field[] fields = Order.class.getDeclaredFields();
                for(Field field : fields){
                    field.setAccessible(true);
                    field.set(order, rs.getObject(field.getName()));
                }
                rlist.add(order);
            }
            return rlist;
        } catch (SQLException | NoSuchMethodException | InstantiationException
                | IllegalAccessException | InvocationTargetException ex) {
            ex.printStackTrace();
            return rlist;
        } finally{         
            try {
                rs.close();
                ps.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}
