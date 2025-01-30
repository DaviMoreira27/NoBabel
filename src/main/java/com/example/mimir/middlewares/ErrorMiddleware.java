package com.example.mimir.middlewares;

import com.example.mimir.exceptions.HttpClientException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import com.example.mimir.dto.Error;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.rmi.ServerException;

@Component
@Order(-1)
public class ErrorMiddleware extends OncePerRequestFilter {
    private final ObjectMapper objectMapper;

    public ErrorMiddleware(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain) throws IOException {
        try {
            chain.doFilter(request, response);
        } catch (HttpClientException e) {
            Error error = new Error(e.getStatus(), e.getMessage(), e.getHttpPath());

            response.setStatus(e.getStatus().value());
            response.setContentType("application/json");
            response.getWriter().write(objectMapper.writeValueAsString(error));
        } catch (ServletException e) {
            if (e.getCause() instanceof HttpClientException clientException) {
                Error error = new Error(
                        clientException.getStatus(),
                        clientException.getMessage(),
                        clientException.getHttpPath()
                );

                response.setStatus(clientException.getStatus().value());
                response.setContentType("application/json");
                response.getWriter().write(objectMapper.writeValueAsString(error));
            }
        }
    }
}