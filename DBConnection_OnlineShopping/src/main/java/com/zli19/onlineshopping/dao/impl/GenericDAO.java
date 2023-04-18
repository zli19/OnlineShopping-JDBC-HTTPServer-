
package com.zli19.onlineshopping.dao.impl;

import com.zli19.onlineshopping.util.DataSourceConnection;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.lang.reflect.ParameterizedType;
import com.zli19.onlineshopping.dao.IGenericDAO;



public abstract class GenericDAO<T> implements IGenericDAO<T> {
    
    private final Class<T> persistentClass;

    public GenericDAO() {      
        this.persistentClass = (Class<T>)((ParameterizedType)getClass().getGenericSuperclass())
                .getActualTypeArguments()[0];
    }      
    
    
    @Override
    public int insert(T t) {
   
        StringBuilder sql = new StringBuilder("INSERT INTO ");
        sql.append(t.getClass().getSimpleName().toLowerCase())
                .append("s(");
        Field[] fields = t.getClass().getDeclaredFields();
        StringBuilder qmarks = new StringBuilder();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            field.setAccessible(true);
            try {
                if(field.get(t)==null){
                    continue;
                }                  
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                ex.printStackTrace();
            }
            sql.append(field.getName());
            qmarks.append("?");
            if(i != fields.length - 1){
                sql.append(",");
                qmarks.append(",");
            }
        }
        sql.append(") VALUES(")
                .append(qmarks.toString())
                .append(");"); 
        
        Connection conn = DataSourceConnection.getConnection();
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql.toString());
            int i = 1;
            for (Field field : fields) {
                field.setAccessible(true);
                if(field.get(t)==null){
                    continue;
                }
                ps.setObject(i, field.get(t));
                i++;
            }
            return ps.executeUpdate();
        }catch (IllegalArgumentException | IllegalAccessException | SQLException ex) {
            ex.printStackTrace();
            return -1;
        }finally{
            try {
                ps.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public int delete(String fieldName, Object value) {
        
        StringBuilder sql = new StringBuilder("DELETE FROM ");
        sql.append(this.persistentClass.getSimpleName().toLowerCase())
                .append("s WHERE ")
                .append(fieldName)
                .append(" = ?;");
        
        Connection conn = DataSourceConnection.getConnection();
        PreparedStatement ps = null;
        try {                
            ps = conn.prepareStatement(sql.toString());
            //ps.setString(1, fieldName);
            ps.setObject(1, value);
            return ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        } finally{         
            try {
                ps.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public int update(T t) {    
        
        StringBuilder sql = new StringBuilder("UPDATE ");
        sql.append(t.getClass().getSimpleName().toLowerCase())
                .append("s SET ");
        Field[] fields = t.getClass().getDeclaredFields();   
        for (int i = 1; i < fields.length; i++) {
            Field field = fields[i];
            sql.append(field.getName())
                    .append(" = ?");
            if(i != fields.length - 1){
                sql.append(",");
            }
        }
        sql.append(" WHERE ")
                .append(fields[0].getName())
                .append(" = ?;"); 
        
        Connection conn = DataSourceConnection.getConnection();
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql.toString());
            for (int i = 1; i < fields.length; i++) {
                Field field = fields[i];
                field.setAccessible(true);
                ps.setObject(i, field.get(t));
            }
            fields[0].setAccessible(true);
            ps.setObject(fields.length, fields[0].get(t));
            return ps.executeUpdate();
        }catch (IllegalArgumentException | IllegalAccessException | SQLException ex) {
            ex.printStackTrace();
            return -1;
        }finally{
            try {
                ps.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public ArrayList<T> queryBy(String fieldName, Object value) {
      
        StringBuilder sql = new StringBuilder("SELECT * FROM ");
        sql.append(this.persistentClass.getSimpleName().toLowerCase())
                .append("s WHERE ")
                .append(fieldName)
                .append(" = ?;");
        Field[] fields = this.persistentClass.getDeclaredFields();
        
        Connection conn = DataSourceConnection.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<T> rlist = new ArrayList<>();
        try {                
            ps = conn.prepareStatement(sql.toString());
            //ps.setString(1, fieldName);
            ps.setObject(1, value);
            rs = ps.executeQuery();
            while(rs.next()){
                Constructor<T> constructor = this.persistentClass.getConstructor();
                T instance = constructor.newInstance();
                for(Field field : fields){
                    field.setAccessible(true);
                    field.set(instance, rs.getObject(field.getName()));
                }
                rlist.add(instance);
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

    @Override
    public ArrayList<T> queryAll() {

        StringBuilder sql = new StringBuilder("SELECT * FROM ");
        sql.append(this.persistentClass.getSimpleName().toLowerCase())
                .append("s");

        Field[] fields = this.persistentClass.getDeclaredFields();
        
        Connection conn = DataSourceConnection.getConnection();
        ArrayList<T> rlist = new ArrayList<>();
        try(PreparedStatement ps = conn.prepareStatement(sql.toString());
                ResultSet rs = ps.executeQuery();) {                
            while(rs.next()){
                Constructor<T> constructor = this.persistentClass.getConstructor();
                T instance = constructor.newInstance();
                for(Field field : fields){
                    field.setAccessible(true);
                    field.set(instance, rs.getObject(field.getName()));
                }
                rlist.add(instance);
            }
            return rlist;
        } catch (SQLException | NoSuchMethodException | InstantiationException
                | IllegalAccessException | InvocationTargetException ex) {
            ex.printStackTrace();
            return rlist;
        } 
    }
}
