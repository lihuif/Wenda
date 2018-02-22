package com.example.Test.interceptor;

import com.example.Test.dao.LoginTicketDAO;
import com.example.Test.dao.UserDAO;
import com.example.Test.model.HostHolder;
import com.example.Test.model.LoginTicket;
import com.example.Test.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * 登录的拦截器
 */
@Component
public class PassportInterceptor implements HandlerInterceptor{
    @Autowired
    LoginTicketDAO loginTicketDAO;

    @Autowired
    UserDAO userDAO;

    @Autowired
    HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        String ticket = null;
        if (httpServletRequest.getCookies() != null){
            for(Cookie cookie: httpServletRequest.getCookies()){ //遍历cookie
                if(cookie.getName().equals("ticket")){ //找到了ticket
                    ticket = cookie.getValue();
                    break;
                }
            }
        }
        if (ticket != null){//找到了ticket
            LoginTicket loginTicket = loginTicketDAO.selectByTicket(ticket);
            if (loginTicket==null || loginTicket.getExpired().before(new Date()) || loginTicket.getStatus()!=0){ //无效的状态
                return true;
            }
            User user = userDAO.selectById(loginTicket.getUserId()); //t票有效的话
            hostHolder.setUser(user);//将用户信息存入上下文
        }
        return true; //返回false直接被拦截
    }

    /**
     * 渲染之前的操作
     * @param httpServletRequest
     * @param httpServletResponse
     * @param o
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception { //渲染之前
        if (modelAndView != null){ //modelAndView = 模板+Model
            modelAndView.addObject("user", hostHolder.getUser());  //可以在模板访问user。
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        hostHolder.clear();
    }
}
