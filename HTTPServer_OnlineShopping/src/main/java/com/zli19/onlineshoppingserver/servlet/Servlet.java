
package com.zli19.onlineshoppingserver.servlet;

import com.zli19.onlineshoppingserver.entity.HttpRequest;
import com.zli19.onlineshoppingserver.entity.HttpResponse;

/**
 *
 * @author zhiku
 */
public interface Servlet {
    void manage(HttpRequest request, HttpResponse response);
}
