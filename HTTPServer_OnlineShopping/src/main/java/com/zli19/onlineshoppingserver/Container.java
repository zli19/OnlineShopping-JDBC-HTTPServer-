
package com.zli19.onlineshoppingserver;

import com.zli19.onlineshoppingserver.servlet.Servlet;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author zhiku
 */
public class Container {
    private static final Map<String, Servlet> SERVLETS
            = new ConcurrentHashMap<>();
    
    private static final Map<String, Session> SESSIONS
            = new ConcurrentHashMap<>();    
    
    public static Servlet getServlet(String key){
        if(key.equals("/")){
            key = "/home";
        }
        if(!SERVLETS.containsKey(key)){               
            try {
                if(Configuration.SERVLET_CONFIG.containsKey(key)){
                Object instance = Class.
                        forName(Configuration
                                .SERVLET_CONFIG
                                .getProperty(key))
                        .getConstructor()
                        .newInstance();       
                SERVLETS.put(key, (Servlet)instance);
                }
            } catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | SecurityException | InvocationTargetException ex) {
                ex.printStackTrace();
            }
        }
        return SERVLETS.get(key);
    }
    
    public static Session getSession(String sessionId){
        return SESSIONS.get(sessionId);
    }
    
    public static void addSession(String sessionId, Session session){
        SESSIONS.put(sessionId, session);
    }
}
