package com.jthinking.image.web.interceptor;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 允许请求跨域访问本服务器
 * 跨域上传必须！
 *
 * @author JiaBochao
 * @version 2017-11-17 15:34:32
 */
public class UploadInterceptor extends HandlerInterceptorAdapter {

    /**
     * 设置允许跨域的头信息
     *
     * @param httpServletRequest
     * @param httpServletResponse
     * @param o
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        httpServletResponse.setHeader("Access-Control-Allow-Credentials", "false");
        //注意！生产环境不要设置为 “*” ，设置为前端项目域名
        //httpServletResponse.setHeader("Access-Control-Allow-Origin", "http://www.jthinking.com");
        httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "*");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", "X_Requested_With,Content-Type");
        httpServletResponse.setContentType("application/json");
        httpServletResponse.setCharacterEncoding("utf-8");
        return true;
    }
}
