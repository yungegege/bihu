package com.linghua.wenda.controller;

import com.linghua.wenda.async.EventModel;
import com.linghua.wenda.async.EventProducer;
import com.linghua.wenda.async.EventType;
import com.linghua.wenda.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    EventProducer eventProducer;

    @Autowired
    UserService userService;

    @RequestMapping(path = "/reg", method = RequestMethod.POST)
    public String reg(Model model, HttpServletResponse response,
                      @RequestParam("username") String username,
                      @RequestParam("password") String password,
                      @RequestParam(value = "email",defaultValue = "") String email,
                      @RequestParam(value = "next", required = false) String next) {
        try {
            Map<String, String> map = userService.register(username, password,email);
            if (map.containsKey("ticket")) {
                Cookie cookie = new Cookie("ticket", map.get("ticket"));
                cookie.setPath("/");
                response.addCookie(cookie);
                if (!StringUtils.isBlank(next)) {
                    return "redirect:/" + next;
                }
                return "redirect:/";
            } else {
                model.addAttribute("msg", map.get("msg"));
                return "login";
            }

        } catch (Exception e) {
            model.addAttribute("msg", "注册异常");
            logger.error("注册异常" + e.getMessage());
            return "login";
        }
    }


    @RequestMapping(path = "/reglogin", method = RequestMethod.GET)
    public String reglogin(Model model, @RequestParam(value = "next", required = false) String next) {
        model.addAttribute("next", next);
        return "login";
    }


    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public String login(Model model, HttpServletResponse response,
                        @RequestParam("username") String username,
                        @RequestParam("password") String password,
                        @RequestParam(value = "next", required = false) String next,
                        @RequestParam(value = "rememberme", defaultValue = "false") boolean rememberme) {
        try {
            Map<String, String> map = userService.login(username, password);
            if (map.containsKey("ticket")) {
                Cookie cookie = new Cookie("ticket", map.get("ticket"));
                cookie.setPath("/");
                if (rememberme) {
                    cookie.setMaxAge(3600*24*5);
                }
                response.addCookie(cookie);
                EventModel eventModel = new EventModel();
                eventModel.setType(EventType.LOGIN);
                eventModel.setExt("email",userService.getUserByName(username).getEmail());
                eventModel.setExt("username", username);
                eventProducer.fireEvent(eventModel);

                if (!StringUtils.isBlank(next)) {
                    return "redirect:/" + next;
                }
                return "redirect:/";
            } else {
                model.addAttribute("msg", map.get("msg"));
                return "login";
            }
        } catch (Exception e) {
            logger.error("登录异常"+e.getMessage());
            return "login";
        }

    }


    @RequestMapping(path = "/logout", method = RequestMethod.GET)
    public String logout(@CookieValue("ticket") String ticket) {
        userService.logout(ticket);
        return "redirect:/";
    }


}
