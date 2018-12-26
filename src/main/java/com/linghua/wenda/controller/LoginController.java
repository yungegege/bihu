package com.linghua.wenda.controller;

import com.linghua.wenda.async.EventModel;
import com.linghua.wenda.async.EventProducer;
import com.linghua.wenda.async.EventType;
import com.linghua.wenda.model.User;
import com.linghua.wenda.service.UserService;
import com.linghua.wenda.util.JedisUtil;
import com.linghua.wenda.util.RedisKeyUtil;
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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Random;

@Controller
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    EventProducer eventProducer;

    @Autowired
    UserService userService;

    @Autowired
    JedisUtil jedisUtill;

    @RequestMapping(path = "/reg", method = RequestMethod.POST)
    public String reg(Model model, HttpServletRequest request,
                      @RequestParam("username") String username,
                      @RequestParam("password") String password,
                      @RequestParam(value = "email") String email,
                      @RequestParam(value = "next", required = false) String next) {
        try {
            Map<String, String> map = userService.register(username, password, email);
            if (map.containsKey("userId")) {
                //往redis存激活码
                String key = RedisKeyUtil.getActiveKey(username);
                Random random = new Random();
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < 15; i++) {
                    sb.append(random.nextInt(10));
                }
                String code = sb.toString();
                jedisUtill.add(key, code);
                //发激活邮件
                EventModel eventModel = new EventModel();
                eventModel.setType(EventType.REGISTER);
                eventModel.setExt("email", userService.getUserByName(username).getEmail());
                eventModel.setExt("username", username);
                username = URLEncoder.encode(username, "utf-8");
                eventModel.setExt("url", "http://127.0.0.1:8080/activeUser?username=" + username + "&code=" + code);
                eventProducer.fireEvent(eventModel);

                model.addAttribute("msg", "请进入邮箱激活账户");
                if (!StringUtils.isBlank(next)) {
                    return "register";
                }
                return "register";
            } else {
                model.addAttribute("msg", map.get("msg"));
                return "register";
            }

        } catch (Exception e) {
            model.addAttribute("msg", "注册异常");
            logger.error("注册异常" + e.getMessage());
            return "register";
        }
    }


    @RequestMapping(path = "/activeUser", method = RequestMethod.GET)
    public String activeUser(Model model, String username, String code) {
        String key = RedisKeyUtil.getActiveKey(username);
        String value = jedisUtill.get(key);
        if (StringUtils.isBlank(value)) {
            model.addAttribute("msg", "激活码过期");
            return "register";
        }
        if (!value.equals(code)) {
            model.addAttribute("msg", "激活码错误");
            return "register";
        }
        //激活成功改状态
        User user = userService.getUserByName(username);
        user.setStatus(1);
        userService.updateStatus(user);
        return "redirect:/reglogin";
    }


    @RequestMapping(path = "/reglogin", method = RequestMethod.GET)
    public String toLogin(Model model, @RequestParam(value = "next", required = false) String next) {
        model.addAttribute("next", next);
        return "login";
    }

    @RequestMapping(path = "/toReg", method = RequestMethod.GET)
    public String toReg() {
        return "register";
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
                    cookie.setMaxAge(3600 * 24 * 5);
                }
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
            logger.error("登录异常" + e.getMessage());
            return "login";
        }

    }


    @RequestMapping(path = "/logout", method = RequestMethod.GET)
    public String logout(@CookieValue("ticket") String ticket) {
        userService.logout(ticket);
        return "redirect:/";
    }


}
