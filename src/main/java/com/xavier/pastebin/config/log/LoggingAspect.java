package com.xavier.pastebin.config.log;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    /**
     * Key for getting user agent.
     */
    private static final String HEADER_FIELD_USER_AGENT = "User-Agent";

    private HttpServletRequest request;

    /**
     * Logs a message before executing the method.
     *
     * @param joinPoint The JoinPoint object representing the intercepted method.
     */
    @Before("execution(* com.xavier.pastebin.controller.*.*(..))")
    public void logBefore(JoinPoint joinPoint) {
        if (log.isDebugEnabled()) {
            log.info("Before executing: {} -> user agent: {},ip: {}",
                    joinPoint.getSignature().toShortString(),
                    request.getHeader(HEADER_FIELD_USER_AGENT),
                    request.getRemoteAddr()
            );
        }
    }

    @Autowired
    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }
}

