package com.example.mimir.middlewares;

import com.example.mimir.common.ErrorHandlingFilters;
import com.example.mimir.services.SessionService;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {
    private final SessionService sessionService;
    private final ErrorHandlingFilters errorHandling;

    public FilterConfig(SessionService sessionService, ErrorHandlingFilters errorHandling) {
        this.sessionService = sessionService;
        this.errorHandling = errorHandling;
    }

    @Bean
    public FilterRegistrationBean<SessionMiddleware> sessionMiddleware() {
        FilterRegistrationBean<SessionMiddleware> registration = new FilterRegistrationBean<>();
        registration.setFilter(new SessionMiddleware(sessionService, errorHandling));
        return registration;
    }

    @Bean
    public FilterRegistrationBean<AuthenticationMiddleware> authenticationMiddleware() {
        FilterRegistrationBean<AuthenticationMiddleware> registration = new FilterRegistrationBean<>();
        registration.setFilter(new AuthenticationMiddleware(errorHandling, sessionService));
        registration.addUrlPatterns("/api/*");
        return registration;
    }

    @Bean
    public FilterRegistrationBean<UpdateLastRequestMiddleware> updateLastRequestMiddlewareFilterRegistrationBean() {
        FilterRegistrationBean<UpdateLastRequestMiddleware> registration = new FilterRegistrationBean<>();
        registration.setFilter(new UpdateLastRequestMiddleware(sessionService, errorHandling));
        return registration;
    }

}