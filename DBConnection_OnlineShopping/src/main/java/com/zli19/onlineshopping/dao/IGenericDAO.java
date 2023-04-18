
package com.zli19.onlineshopping.dao;

import java.util.ArrayList;

/**
 *
 * @author zhiku
 * @param <T>
 */
public interface IGenericDAO<T> {
    
    
    int insert(T t);
    int delete(String fieldName, Object value);
    int update(T t);
    ArrayList<T> queryBy(String fieldName, Object value);
    ArrayList<T> queryAll();
    
}
