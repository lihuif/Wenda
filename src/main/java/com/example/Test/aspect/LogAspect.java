package com.example.Test.aspect;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;


@Aspect
@Component
public class LogAspect {
    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);


    @Before("execution(* com.example.Test.controller.*.*(..))")
    public void beforeMethod(){
        logger.info("before method "+ new Date());
    }

    @After("execution(* com.example.Test.controller.*.*(..))")
    public void afterMethod(){
        logger.info("after method "+ new Date());
    }
}
