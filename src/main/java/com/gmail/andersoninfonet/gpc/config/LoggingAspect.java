package com.gmail.andersoninfonet.gpc.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private final Logger logger = LoggerFactory.getLogger(LoggingAspect.class.getName());

    @Around("execution(* com.gmail.andersoninfonet.gpc.resources..*(..))")
    public Object logMethodCall(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        Object result = joinPoint.proceed(args);
        logger.info("GPC - Metodo {} chamado com os argumentos {} and retornando {}", methodName, args, result);
        return result;
    }
}
