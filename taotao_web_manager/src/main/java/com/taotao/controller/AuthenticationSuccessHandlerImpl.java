package com.taotao.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.taotao.pojo.system.LoginLog;
import com.taotao.service.system.LoginLogService;
import com.taotao.util.WebUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

public class AuthenticationSuccessHandlerImpl implements AuthenticationSuccessHandler {
    @Reference
    LoginLogService loginLogService;


    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        //登录之后会调用
        System.out.println("登录成功了！！！！！！");
        String ip = httpServletRequest.getRemoteAddr();
        LoginLog loginLog = new LoginLog();
        loginLog.setLoginName(authentication.getName());
        loginLog.setLoginTime(new Date());
        loginLog.setIp(ip);
        loginLog.setLocation(WebUtil.getCityByIP(ip));
        loginLog.setBrowserName(WebUtil.getBrowserName(httpServletRequest.getHeader("user-agent")));
        loginLogService.add(loginLog);
        httpServletRequest.getRequestDispatcher("/main.html").forward(httpServletRequest,httpServletResponse);
    }
}
