package com.linghua.wenda.interceptor;

import com.linghua.wenda.dao.LoginTicketDao;
import com.linghua.wenda.dao.UserDao;
import com.linghua.wenda.model.HostHolder;
import com.linghua.wenda.model.LoginTicket;
import com.linghua.wenda.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 *     preHandle在业务处理器处理请求之前被调用，
 *     postHandle在业务处理器处理请求执行完成后,生成视图之前执行，
 *     afterCompletion在DispatcherServlet完全处理完请求后被调用,可用于清理资源等 。
 */
@Component
public class PassportInterceptor implements HandlerInterceptor {

    @Autowired
    LoginTicketDao loginTicketDao;

    @Autowired
    UserDao userDao;

    @Autowired
    HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        String ticket = null;
        if (httpServletRequest.getCookies()!=null){
            for(Cookie cookie:httpServletRequest.getCookies()){
                if (cookie.getName().equals("ticket")){
                    ticket = cookie.getValue();
                    break;
                }
            }
        }
        if (ticket!=null){
            LoginTicket loginTicket = loginTicketDao.selectByTicket(ticket);
            if (loginTicket==null || loginTicket.getExpired().before(new Date()) || loginTicket.getStatus()!=0){
                return true;
            }

            User user = userDao.selectById(loginTicket.getUserId());
            hostHolder.setUser(user);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        if (modelAndView!=null){
            modelAndView.addObject("user",hostHolder.getUser());
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        hostHolder.clear();
    }
}
