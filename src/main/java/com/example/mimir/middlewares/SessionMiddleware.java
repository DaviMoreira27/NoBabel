package com.example.mimir.middlewares;

import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Order(1)
public class SessionMiddleware implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        Cookie[] cookies = httpRequest.getCookies();
        try {
            if (cookies.length == 0) {
                // Redo
                throw new ServletException("No cookies found");
            }

            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("JSESSIONID")) {
                    // Proceed
                    filterChain.doFilter(servletRequest, servletResponse);
                }
            }

            throw new ServletException("No session ID found");
        } catch (ServletException error) {
            System.out.println(error.getMessage());
        }

    }
}
