package com.example.mimir.middlewares;

import jakarta.servlet.*;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
@Order(1)
public class SessionMiddleware implements Filter {

    /**
     * @param servletRequest
     * @param servletResponse
     * @param filterChain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(HttpRequest servletRequest, HttpResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String sessionId = servletResponse.sslSession().;
    }
}
