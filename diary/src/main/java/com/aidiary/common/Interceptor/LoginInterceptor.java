package com.aidiary.common.Interceptor;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component 
public class LoginInterceptor implements HandlerInterceptor{

    private Logger log = LoggerFactory.getLogger(LoginInterceptor.class);

    @Override 
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException{
        

        HttpSession session = request.getSession(false);

        if(session == null || session.getAttribute("loginId") ==  null){

    log.info("Interceptor URL:{}",request.getRequestURI());
     log.info("Interceptor METHOD:{} " , request.getMethod());
            //AJAX 요청인 경우
            String requestedWith = request.getHeader("X-Requested-With");

            if("XMLHttpRequest".equals(requestedWith)){
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return false;
            }
              //일반 페이지 요청인 경우
            response.sendRedirect("/");
            return false;
        }
        log.info("로그인세션 정상");
        return true;
    }

}
