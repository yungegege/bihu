package com.linghua.wenda.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;


@Aspect
@Component
public class LogAspect {

    private static final Logger log = LoggerFactory.getLogger(LogAspect.class);

    @Before("execution(* com.linghua.wenda.controller.*.*(..))")
    public void beforeMethod(JoinPoint joinPoint){
        log.info("before:"+new Date()+joinPoint.getArgs());
    }

    @After("execution(* com.linghua.wenda.controller.*.*(..))")
    public void afterMethod(){
        log.info("after:"+new Date());
    }
}
