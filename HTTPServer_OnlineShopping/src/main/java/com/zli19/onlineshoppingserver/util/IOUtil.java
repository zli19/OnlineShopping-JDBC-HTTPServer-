
package com.zli19.onlineshoppingserver.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 *
 * @author zhiku
 */
public class IOUtil {
    private static final String ROOT_DIR = "./www";
    
    public static byte[] readContent(String path) {
        File file = new File(ROOT_DIR,path);
        if(!file.exists()){
            return null;
        }
        try(FileInputStream fis = new FileInputStream(file);) {
            byte[] buff = new byte[(int)file.length()];
            fis.read(buff);
            return buff;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }      
    }
}
