
package com.zli19.onlineshoppingserver.servlet.impl;

import com.zli19.onlineshoppingserver.entity.HttpRequest;
import com.zli19.onlineshoppingserver.entity.HttpResponse;
import com.zli19.onlineshoppingserver.servlet.Servlet;
import com.zli19.onlineshoppingserver.util.IOUtil;

/**
 *
 * @author zhiku
 */
public class HomeServlet implements Servlet{

    
    @Override
    public void manage(HttpRequest request, HttpResponse response) {
        byte[] content = IOUtil.readContent("/index.html");            
        response.set200OKResponse(content);
    }
}
