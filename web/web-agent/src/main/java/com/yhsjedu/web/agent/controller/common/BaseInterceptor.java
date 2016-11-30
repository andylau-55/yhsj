package com.yhsjedu.web.agent.controller.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.yhsjedu.datacloud.core.entity.Users;

/**
 * 拦截器<br>
 * 是否登陆检查、业务共同处理
 * 
 * @author yangjin
 */
public class BaseInterceptor extends HandlerInterceptorAdapter {

    /** 不用拦截的请求 */
    private static final String[] IGNORE_URI = { "login","userLogin"};

    private final static Log log = LogFactory.getLog(BaseInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String url = request.getRequestURI();
        // 忽略不需要拦截的地址
        for (String s : IGNORE_URI) {
            if (url.contains(s) || url.indexOf("/static/") >= 0) {
                return true;
            }
        }
        Users users = (Users) request.getSession().getAttribute("users");
        if (users != null && users.getUserName() != null) {
        	return true;
		} else {
            log.warn("来自" + request.getRemoteAddr() + "的请求" + url + "被拦截");
            if (request.getHeader("x-requested-with") != null
                    && request.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest")) { // 如果是ajax请求响应头会有，x-requested-with
                response.setHeader("users", "timeout");// 在响应头设置session状态

            } else if (request.getHeader("accept") != null && request.getHeader("accept").contains("json")) {
                response.setHeader("users", "timeout");// 在响应头设置session状态

            } else {
                response.sendRedirect("/login");
            }

            return false;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }

}
