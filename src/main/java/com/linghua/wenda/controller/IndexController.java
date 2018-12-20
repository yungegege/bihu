package com.linghua.wenda.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;

//@Controller
public class IndexController {

    @RequestMapping("/")
    @ResponseBody
    public String index(HttpSession session) {
        return "hello, liyunfei " + session.getAttribute("msg");
    }

    @RequestMapping("/name/{name}")
    @ResponseBody
    public String hello(@PathVariable("name") String name, @RequestParam(value = "Id", defaultValue = "1") int id) {
        return "hello, " + name + "!" + id;
    }

    @RequestMapping(path = "/index", method = RequestMethod.GET)
    public String template() {
        return "home";
    }

    @RequestMapping("/re/{id}")
    @ResponseBody
    public String request(Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        HashMap<Integer, String> map = new HashMap<>();
        map.put(1, request.getMethod());
        map.put(2, request.getQueryString());
        map.put(3, request.getHeader("cookie"));
        return map.toString();
    }

    @RequestMapping("/redirect/{code}")
    public String redirect(@PathVariable("code") int code, HttpSession session) {
        session.setAttribute("msg", "lalala");
        return "redirect:/";
    }


    @RequestMapping("/redirect1/{code}")
    public RedirectView redirect1(@PathVariable("code") int code, HttpSession session, HttpServletResponse response) {
        session.setAttribute("msg", "lalala");
        RedirectView red = new RedirectView("/");
        if (code == 301) {
            red.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        }
        return red;
    }
}




