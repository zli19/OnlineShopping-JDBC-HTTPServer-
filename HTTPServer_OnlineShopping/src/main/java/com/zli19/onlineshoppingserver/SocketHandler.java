
package com.zli19.onlineshoppingserver;

import com.zli19.onlineshoppingserver.entity.HttpResponse;
import com.zli19.onlineshoppingserver.entity.HttpRequest;
import com.zli19.onlineshoppingserver.filter.Filter;
import com.zli19.onlineshoppingserver.filter.LoginFilter;
import com.zli19.onlineshoppingserver.servlet.Servlet;
import com.zli19.onlineshoppingserver.util.IOUtil;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 *
 * @author zhiku
 */
public class SocketHandler implements Runnable{
    private final Socket socket;

    public SocketHandler(Socket socket) {
        this.socket = socket;
    }

    
    @Override
    public void run() {
        try(InputStream is = socket.getInputStream();
                OutputStream os = socket.getOutputStream();) {   
            // Analyze request
            HttpRequest request = new HttpRequest();
            System.out.println(Thread.currentThread());
            request.parseRequest(is);
            System.out.println(request);
            HttpResponse response = new HttpResponse();  
            
            // Configure response          
            Servlet servlet = Container.getServlet(request.getPath());
            if(servlet != null){
                servlet.manage(request, response);
                System.out.println(response);                  
            }else{
                byte[] content = IOUtil.readContent("/404_NOT_FOUND.html");
                response.set404NotFoundResponse(content);
            }
            System.out.println(response);
            response.writeResponse(os);  
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }  
}
