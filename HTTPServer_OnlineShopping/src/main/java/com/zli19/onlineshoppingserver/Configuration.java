
package com.zli19.onlineshoppingserver;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author zhiku
 */
public class Configuration {
    public static final Properties SERVLET_CONFIG = new Properties();
    
    static {
        String rootPath = Thread
                        .currentThread()
                        .getContextClassLoader()
                        .getResource("")
                        .getPath();
//        System.out.println(rootPath);
        String servletConfigPath = rootPath + "servlet.properties";             
        try {
            SERVLET_CONFIG.load(new FileInputStream(servletConfigPath));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
