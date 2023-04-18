
package com.zli19.onlineshoppingserver;

import java.util.HashMap;

/**
 *
 * @author zhiku
 */
public class Session extends HashMap<String, Object>{
    
    public void setAttribute(String key, Object value){
        super.put(key, value);
    }
    
    public Object getAttributeValue(String key){
        return super.get(key);
    }
}
