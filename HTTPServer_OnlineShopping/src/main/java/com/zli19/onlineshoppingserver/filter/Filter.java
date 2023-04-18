
package com.zli19.onlineshoppingserver.filter;

import com.zli19.onlineshoppingserver.entity.HttpRequest;
import com.zli19.onlineshoppingserver.entity.HttpResponse;

/**
 *
 * @author zhiku
 */
public interface Filter {
    boolean preFilter(HttpRequest request, HttpResponse response);
    boolean postFilter(HttpRequest request, HttpResponse response);
}
