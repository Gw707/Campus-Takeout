package com.gw.filter;


import com.alibaba.fastjson.JSON;
import com.gw.common.BaseContext;
import com.gw.common.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Slf4j
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
public class LoginCheckFilter implements Filter {

    /**
     * 路径匹配器，可以匹配通配符
     */
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        /**
         * 1、获取本次拦截的url, /backend/index.html
         */
        String requestURL = request.getRequestURI();
        //log.info("本次拦截的url{}",requestURL);

        /**
         * 定义不需要处理的路径
         */
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/user/sendMsg",
                "/user/login"  //放行移动端发送短信和登录请求
        };

        /**
         * 2、判断本次请求是否需要处理
         */
        boolean check = check(urls, requestURL);

        /**
         * 3、如果不需要处理直接放行
         */
        if(check){
            filterChain.doFilter(request, response);
            return;
        }

        /**
         * 4、需要处理，则需要判断登录状态,如果已登录则直接放行
         */
        if(request.getSession().getAttribute("employee") != null){

            Long id = (Long) request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(id);
            filterChain.doFilter(request, response);
            return;
        }

        /**
         * 客户端的登录判断
         */
        if(request.getSession().getAttribute("user") != null){

            Long userId = (Long) request.getSession().getAttribute("user");
            BaseContext.setCurrentId(userId);
            filterChain.doFilter(request, response);
            return;
        }
        /**
         * 5、未登录，需要进行处理返回登录页面
         */

        response.getWriter().write(JSON.toJSONString(Status.error("NOTLOGIN")));


        //filterChain.doFilter(servletRequest, servletResponse);
        //return;
    }

    @Override
    public void destroy() {

    }

    public boolean check(String[] urls, String requestUrl){

        for (String url : urls) {
            if(PATH_MATCHER.match(url, requestUrl)){
                return true;
            }
        }
        return false;

    }
}
