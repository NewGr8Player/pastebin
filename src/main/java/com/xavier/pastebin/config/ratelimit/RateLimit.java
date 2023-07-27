package com.xavier.pastebin.config.ratelimit;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimit {
    @AliasFor(value = "times")
    int value() default 30;

    @AliasFor(value = "value")
    int times() default 30;

    long duration() default 30;

    TimeUnit timeUnit() default TimeUnit.SECONDS;
}
