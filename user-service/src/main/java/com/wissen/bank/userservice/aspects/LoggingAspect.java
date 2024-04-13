package com.wissen.bank.userservice.aspects;

import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Aspect
@Component
public class LoggingAspect {
    
    private Logger LOGGER = LoggerFactory.getLogger(LoggingAspect.class);

    @Pointcut("execution(* com.wissen.bank.userservice.controllers.*.*(..))")
    public void controllerPointcut() {}

    @AfterThrowing(pointcut = "controllerPointcut()", throwing = "exception")
    public void logException(ResponseStatusException exception) {
        LOGGER.error("Exception occurred: ", exception.getMessage());
    }

}
