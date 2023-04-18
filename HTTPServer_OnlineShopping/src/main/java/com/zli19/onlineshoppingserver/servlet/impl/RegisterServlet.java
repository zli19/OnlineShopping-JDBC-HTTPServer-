
package com.zli19.onlineshoppingserver.servlet.impl;

import com.zli19.onlineshopping.dao.impl.UserDAO;
import com.zli19.onlineshopping.entity.User;
import com.zli19.onlineshoppingserver.entity.HttpRequest;
import com.zli19.onlineshoppingserver.entity.HttpResponse;
import com.zli19.onlineshoppingserver.servlet.Servlet;
import com.zli19.onlineshoppingserver.util.IOUtil;
import java.util.ArrayList;

/**
 *
 * @author zhiku
 */
public class RegisterServlet implements Servlet{

    @Override
    public void manage(HttpRequest request, HttpResponse response) {
        String method = request.getMethod();
        switch (method){
            case "GET" -> handleGET(response);
            case "POST" -> handlePOST(request, response);
        }        
    }

    private void handleGET(HttpResponse response) {
        byte[] content = IOUtil.readContent("/register.html");            
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
            User user = new User();
            user.setUserName(username);
            user.setPassword(request.getParameterValue("password"));
            userDAO.insert(user);
            byte[] content =("""
                              <html>
                              
                              <body>
                              <h1>User\t""" + username + " registered successfully!</h1>\n"
                        +"</body>\n</html>").getBytes();
            response.set200OKResponse(content);
        }else{           
            byte[] content =("""
                              <html>

                              <body>
                              <h1>Sorry, username\t""" + username + " already exists!</h1>\n"
                    +"</body>\n</html>")
                    .getBytes();
            response.set200OKResponse(content);
        }
    }
    
}
