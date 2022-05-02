package com.quarkus.training.config;

import com.quarkus.training.domain.User;
import lombok.extern.slf4j.Slf4j;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.util.Arrays;

@Logged
@Interceptor
@Slf4j
public class LoggingInterceptor {

    @Inject
    User user;

    @AroundInvoke
    public Object logAround(InvocationContext ctx) throws Exception {
        log.debug("Enter: {}.{}() with argument[s] = {} and user = {}", ctx.getMethod().getReturnType().getName(),
                ctx.getMethod().getName(), Arrays.toString(ctx.getParameters()), user);
        try {
            Object result = ctx.proceed();
            log.debug("Exit: {}.{}() with result = {}", ctx.getMethod().getReturnType().getName(),
                    ctx.getMethod().getName(), result);
            return result;
        } catch (Exception e) {
            log.error("Exception in {}.{}() with message = {}", ctx.getMethod().getReturnType().getName(),
                    ctx.getMethod().getName(), e.getMessage());
            throw e;
        }
    }

}