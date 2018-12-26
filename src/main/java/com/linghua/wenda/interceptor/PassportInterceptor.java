package com.linghua.wenda.interceptor;

import com.linghua.wenda.dao.LoginTicketDao;
import com.linghua.wenda.dao.PositionDao;
import com.linghua.wenda.dao.UserDao;
import com.linghua.wenda.model.HostHolder;
import com.linghua.wenda.model.LoginTicket;
import com.linghua.wenda.model.User;
import com.linghua.wenda.model.baidu.BaiduResponse;
import com.linghua.wenda.model.baidu.Position;
import com.linghua.wenda.util.PositionUtil;
import com.linghua.wenda.util.WendaUtil;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Date;

/**
 * preHandle在业务处理器处理请求之前被调用，
 * postHandle在业务处理器处理请求执行完成后,生成视图之前执行，
 * afterCompletion在DispatcherServlet完全处理完请求后被调用,可用于清理资源等 。
 */
@Log4j
@Component
public class PassportInterceptor implements HandlerInterceptor {

    @Autowired
    LoginTicketDao loginTicketDao;

    @Autowired
    UserDao userDao;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    PositionDao positionDao;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        addPosition(httpServletRequest);
        String ticket = null;
        if (httpServletRequest.getCookies() != null) {
            for (Cookie cookie : httpServletRequest.getCookies()) {
                if (cookie.getName().equals("ticket")) {
                    ticket = cookie.getValue();
                    break;
                }
            }
        }
        if (ticket != null) {
            LoginTicket loginTicket = loginTicketDao.selectByTicket(ticket);
            if (loginTicket == null || loginTicket.getExpired().before(new Date()) || loginTicket.getStatus() != 0) {
                return true;
            }

            User user = userDao.selectById(loginTicket.getUserId());
            hostHolder.setUser(user);
        }


        return true;
    }

    private void addPosition(HttpServletRequest httpServletRequest) throws Exception{
        if (!httpServletRequest.getRequestURI().equals("/error")) {
            String remoteAddr = getIP(httpServletRequest);
            if ("127.0.0.1".equals(remoteAddr)) {
                remoteAddr = "222.64.97.180";
            }
            String address = "";
            BaiduResponse baiduResonse = PositionUtil.getBaiduResonse(remoteAddr);
            if ("0".equals(baiduResonse.getStatus())) {
                Position position = new Position();
                if (hostHolder == null) {
                    position.setUserId(hostHolder.getUser().getId());
                } else {
                    position.setUserId(WendaUtil.ANONYMOUS_USERID);
                }
                position.setExecuteTime(new Date());
                position.setUrl(httpServletRequest.getRequestURL().toString());
                position.setAllAddress(baiduResonse.getAddress());
                position.setSimpleAddress(baiduResonse.getContent().getAddress());
                position.setCity(baiduResonse.getContent().getAddress_detail().getCity());
                position.setCityCode(baiduResonse.getContent().getAddress_detail().getCity_code());
                position.setDistrict(baiduResonse.getContent().getAddress_detail().getDistrict());
                position.setProvince(baiduResonse.getContent().getAddress_detail().getProvince());
                position.setStreet(baiduResonse.getContent().getAddress_detail().getStreet());
                position.setStreetNumber(baiduResonse.getContent().getAddress_detail().getStreet_number());
                position.setX(baiduResonse.getContent().getPoint().getX());
                position.setY(baiduResonse.getContent().getPoint().getY());
                positionDao.addPostion(position);
                if (!Arrays.asList("北京市", "上海市", "天津市", "重庆市").contains(position.getProvince())) {
                    address = address + position.getProvince();
                }
                address = " " + address + position.getCity() + position.getDistrict() + position.getStreet() + position.getStreetNumber();
            }
            log.info("ip地址:" + remoteAddr + address);

        }
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        if (modelAndView != null) {
            modelAndView.addObject("user", hostHolder.getUser());
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        hostHolder.clear();
    }

    public static String getIP(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (!checkIP(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (!checkIP(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (!checkIP(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
    private static boolean checkIP(String ip) {
        if (ip == null || ip.length() == 0 || "unkown".equalsIgnoreCase(ip)
                || ip.split(".").length != 4) {
            return false;
        }
        return true;
    }
}
