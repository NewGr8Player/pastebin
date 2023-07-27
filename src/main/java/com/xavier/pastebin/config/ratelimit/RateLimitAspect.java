package com.xavier.pastebin.config.ratelimit;

import com.xavier.pastebin.exceptions.RateLimitException;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Aspect
@Component
public class RateLimitAspect {

    private static final String KEY_PREFIX = "rate_limit:";
    private RedisTemplate<String, String> redisTemplate;

    private HttpServletRequest request;

    /**
     * Applies rate limiting to the method annotated with @RateLimit.
     *
     * @param joinPoint the ProceedingJoinPoint representing the intercepted method
     * @param rateLimit the RateLimit annotation that specifies the rate limit configuration
     * @return the result of the intercepted method execution
     * @throws RuntimeException if over the rate limit
     * @throws Throwable        if an error occurs during method execution
     */
    @Around("@annotation(rateLimit)")
    public Object rateLimit(ProceedingJoinPoint joinPoint, RateLimit rateLimit) throws Throwable {
        String ip = obtainIpAddress();
        String key = KEY_PREFIX + ip;
        long limit = rateLimit.value();
        long duration = rateLimit.duration();
        TimeUnit timeUnit = rateLimit.timeUnit();

        if (acquireToken(key, limit, duration, timeUnit)) {
            return joinPoint.proceed();
        } else {
            throw new RateLimitException("请求频率超限");
        }
    }

    /**
     * Acquires a token for the given key with rate limit and duration.
     *
     * @param key      the key to acquire token for
     * @param limit    the maximum number of tokens allowed within the given duration
     * @param duration the duration for which the tokens are valid
     * @param timeUnit the time unit of the duration
     * @return {@code true} if the token is acquired successfully, {@code false} otherwise
     */
    private boolean acquireToken(String key, long limit, long duration, TimeUnit timeUnit) {
        String rate = redisTemplate.opsForValue().get(key);

        if (rate == null) {
            redisTemplate.opsForValue().set(key, "1", duration, timeUnit);
            return true;
        }

        if (Long.parseLong(rate) < limit) {
            redisTemplate.opsForValue().increment(key, 1);
            return true;
        }

        return false;
    }

    /**
     * Obtains the IP address of the client making the request.
     *
     * @return The IP address of the client.
     */
    private String obtainIpAddress() {
        return request.getRemoteAddr();
    }

    @Autowired
    public void setRedisTemplate(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Autowired
    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }
}
