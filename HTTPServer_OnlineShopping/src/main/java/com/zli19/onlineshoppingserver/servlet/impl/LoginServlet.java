
package com.zli19.onlineshoppingserver.servlet.impl;

import com.zli19.onlineshopping.dao.impl.UserDAO;
import com.zli19.onlineshopping.entity.User;
import com.zli19.onlineshoppingserver.Container;
import com.zli19.onlineshoppingserver.Session;
import com.zli19.onlineshoppingserver.entity.HttpRequest;
import com.zli19.onlineshoppingserver.entity.HttpResponse;
import com.zli19.onlineshoppingserver.servlet.Servlet;
import com.zli19.onlineshoppingserver.util.IOUtil;
import java.util.ArrayList;
import java.util.UUID;


/**
 *
 * @author zhiku
 */
public class LoginServlet implements Servlet{
    
    @Override
    public void manage(HttpRequest request, HttpResponse response) {
        String method = request.getMethod();
        switch (method){
            case "GET" -> handleGET(response);
            case "POST" -> handlePOST(request, response);
        }        
    }
    
    private void handleGET(HttpResponse response) {
           byte[] content = IOUtil.readContent("/login.html");            
           response.set200OKResponse(content);
       }

    private void handlePOST(HttpRequest request, HttpResponse response) {
        String username = request.getParameterValue("username");
        if(username == null || "".equals(username)){
            return;
        }
        UserDAO userDAO = new UserDAO();
        ArrayList<User> users = userDAO.queryBy("username", username);
        if(users.isEmpty()){
            byte[] content = IOUtil.readContent("/nosuchuser.html");         
            response.set200OKResponse(content);
        }else{
            User user = users.get(0);
            if(user.getPassword().equals(request.getParameterValue("password"))){
                byte[] content =("""
                                  <html>
                                  
                                  <body>
                                  <h1>Welcome back,\t""" + user.getUserName() + "!</h1>\n"
                        +"</body>\n</html>")
                        .getBytes();
                response.set200OKResponse(content);
                // Handle cookie
                String sessionId = request.getCookieAttributeValue("sessionId");
                if(sessionId == null || Container.getSession(sessionId) == null){
                    String randomUUID = UUID.randomUUID().toString();
                    response.addHeader("set-Cookie", "sessionId=" + randomUUID);
                    Container.addSession(randomUUID, new Session());
                }
            }else{
                byte[] content ="""
                                  <html>
                                  
                                  <body>
                                  <h1>Incorrect password!</h1>
                                   </body>
                                   </html>""".getBytes();                
                response.set200OKResponse(content);
            }
        }
    }
}

   

    

