
package com.zli19.onlineshoppingserver.filter;

import com.zli19.onlineshoppingserver.Container;
import com.zli19.onlineshoppingserver.entity.HttpRequest;
import com.zli19.onlineshoppingserver.entity.HttpResponse;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author zhiku
 */
public class LoginFilter implements Filter{
    
    private static final List<String> WHITE_LIST = new ArrayList<>();
    static {
        WHITE_LIST.add("/sign-in");
        WHITE_LIST.add("/create-account");
    }
    
    @Override
    public boolean preFilter(HttpRequest request, HttpResponse response) {
        if(WHITE_LIST.contains(request.getPath())){
            return true;
        }
        String sessionId = request.getCookieAttributeValue("sessionId");
        if(sessionId == null || Container.getSession(sessionId) == null){
            request.setPath("/sign-in");
            return false;
        }
        return true;
    }

    @Override
    public boolean postFilter(HttpRequest request, HttpResponse response) {
        return true;
    }
    
}
