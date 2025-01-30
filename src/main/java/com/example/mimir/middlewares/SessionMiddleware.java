package com.example.mimir.middlewares;

import com.example.mimir.entities.Session;
import com.example.mimir.exceptions.HttpClientException;
import com.example.mimir.exceptions.general.UnknownInternalException;
import com.example.mimir.exceptions.session.InvalidSessionException;
import com.example.mimir.exceptions.session.SessionException;
import com.example.mimir.exceptions.session.SessionExpiredException;
import com.example.mimir.services.SessionService;
import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

// @Component annotation adds this middleware for every route
@Order(1)
public class SessionMiddleware implements Filter {

    private final SessionService sessionService;

    SessionMiddleware(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws ServletException, IOException {
        try {
            HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
            Cookie[] cookies = httpRequest.getCookies();
            System.out.println("MIDDLEWARE");
            if (cookies == null || cookies.length == 0) {
                throw new InvalidSessionException("No cookies found");
            }

            for (Cookie cookie : cookies) {
                System.out.println(cookie.getName() + " : " + cookie.getValue());
                if (cookie.getName().equals("JSESSIONID")) {
                    this.getSessionData(cookie.getValue());
                    filterChain.doFilter(servletRequest, servletResponse);
                    return;
                }
            }
            throw new InvalidSessionException("No session ID found");
        } catch (SessionException e) {
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            response.sendError(e.getStatus().value(), e.getMessage());
        }
    }

    private void getSessionData (String sessionId) throws SessionException {
       Session sessionData = this.sessionService.getSession(sessionId);
       System.out.println(sessionData);

       if (sessionData == null) {
           throw new InvalidSessionException("No session found");
       }

       if (
               sessionData.getSessionData().isExpired() ||
               sessionData.getSessionData().expirationTime() >= System.currentTimeMillis() ||
               sessionData.getSessionData().sessionMaxIdleTime() >= sessionData.getSessionData().lastRequestTime()
       ) {
           throw new SessionExpiredException("Session already expired");
       }
    }
}


@Configuration
class FilterConfig {
    private final SessionService sessionService;

    public FilterConfig(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Bean
    public SessionMiddleware sessionMiddleware() {
        return new SessionMiddleware(sessionService);
    }

    @Bean
    public FilterRegistrationBean<SessionMiddleware> sessionMiddlewareFilter(SessionMiddleware sessionMiddleware) {
        FilterRegistrationBean<SessionMiddleware> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(sessionMiddleware);
        registrationBean.addUrlPatterns("/test"); // Defina as URLs específicas para aplicar o filtro
        registrationBean.setOrder(1); // Define a ordem do filtro
        return registrationBean;
    }
}