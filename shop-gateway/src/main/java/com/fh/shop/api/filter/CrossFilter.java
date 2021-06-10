package com.fh.shop.api.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author wzq
 * @description
 * @date 2021/6/8 14:14
 */
@Component
public class CrossFilter extends ZuulFilter {
    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpServletResponse response = currentContext.getResponse();
        //处理跨域
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        //处理自定义的请求头
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "x-auth,content-type,x-IdempotenceToken");
        //处理特殊请求：dalete put get post
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "DELETE,PUT,POST,GET");

        //验证options请求
        HttpServletRequest request = currentContext.getRequest();
        String methodHTTP = request.getMethod();
        if ("OPTIONS".equalsIgnoreCase(methodHTTP)) {
            // 禁止路由 不会继续向微服务发送请求
            currentContext.setSendZuulResponse(false);
            return null;
        }

        return null;
    }
}
