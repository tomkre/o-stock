package com.optimagrowth.license.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
@Slf4j
public class UserContextFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        UserContext userContext = UserContextHolder.getContext();
        userContext.setCorrelationId(httpServletRequest.getHeader(UserContext.CORRELATION_ID));
        userContext.setUserId(httpServletRequest.getHeader(UserContext.USER_ID));
        userContext.setAuthToken(httpServletRequest.getHeader(UserContext.AUTH_TOKEN));
        userContext.setOrganizationId(httpServletRequest.getHeader(UserContext.ORGANIZATION_ID));
        log.debug("UserContextFilter Correlation id: {}", userContext.getCorrelationId());
        chain.doFilter(httpServletRequest, response);
    }

}
