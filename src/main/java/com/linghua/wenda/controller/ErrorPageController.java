package com.linghua.wenda.controller;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorPageController implements ErrorController {
    @Override
    public String getErrorPath() {
        return "404";
    }

    @RequestMapping("/error")
    public String error() {
        return getErrorPath();
    }

}
