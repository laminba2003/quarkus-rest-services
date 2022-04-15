package com.quarkus.training.config;

import com.quarkus.training.domain.User;
import lombok.extern.slf4j.Slf4j;
import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import static javax.ws.rs.core.Response.*;

@Provider
@Slf4j
public class ResponseFilter implements ContainerResponseFilter {

    @Inject
    User user;

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
            throws IOException {
        if (responseContext.getStatus() == Status.UNAUTHORIZED.getStatusCode()) {
            if (log.isDebugEnabled()) {
                log.debug("access unauthorized for user : {} ", user);
            }
        } else if (responseContext.getStatus() == Status.FORBIDDEN.getStatusCode()) {
            if (log.isDebugEnabled()) {
                log.debug("access forbidden for user : {} ", user);
            }
        }
    }
}