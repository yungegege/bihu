package com.linghua.wenda.aspect;

import com.linghua.wenda.dao.PositionDao;
import com.linghua.wenda.model.HostHolder;
import com.linghua.wenda.model.baidu.Position;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Modifier;
import java.util.Date;


@Aspect
@Component
public class LogAspect {

    @Autowired
    PositionDao positionDao;

    @Autowired
    HostHolder hostHolder;

    private static final Logger log = LoggerFactory.getLogger(LogAspect.class);

    @Before("execution(* com.linghua.wenda.controller.*.*(..))")
    public void beforeMethod(JoinPoint joinPoint) {
        //类名+方法声明类型+方法名
        log.info("before:" + new Date() + ":"+joinPoint.getSignature().getDeclaringTypeName() + ":" +
                Modifier.toString(joinPoint.getSignature().getModifiers()) + " "+joinPoint.getSignature().getName());
        //获取传入目标方法的参数
//        Object[] args = joinPoint.getArgs();
//        for (int i = 0; i < args.length; i++) {
//            System.out.println("第" + (i+1) + "个参数为:" + args[i]);
//        }
    }

    @After("execution(* com.linghua.wenda.controller.*.*(..))")
    public void afterMethod() {
        log.info("after:" + new Date());
    }
}
