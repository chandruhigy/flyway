package com.example.product.connections;

import com.sun.istack.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TenantIdentifierConfig implements HandlerInterceptor {

    @Autowired
    private TenantFinder tenantFinder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
        String tenant = request.getParameter("tenant");
        TenantContext.setTenant(tenantFinder.findByName(tenant));
        return true;
    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, @Nullable Exception ex) throws Exception {
        TenantContext.clearTenant();
    }

}
