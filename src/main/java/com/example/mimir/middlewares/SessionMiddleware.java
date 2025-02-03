package com.example.mimir.middlewares;

import com.example.mimir.common.ErrorHandlingFilters;
import com.example.mimir.entities.Session;
import com.example.mimir.exceptions.DatabaseException;
import com.example.mimir.exceptions.SessionException;
import com.example.mimir.services.SessionService;
import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.io.IOException;

// @Component annotation adds this middleware for every route
@Order(1)
public class SessionMiddleware implements Filter {
    private final SessionService sessionService;
    private final ErrorHandlingFilters errorHandling;

    public SessionMiddleware (SessionService sessionService, ErrorHandlingFilters errorHandlingFilters) {
        this.sessionService = sessionService;
        this.errorHandling = errorHandlingFilters;
    }

    private Cookie setSession (HttpServletRequest httpRequest) throws DatabaseException {
        String ipAddress = httpRequest.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
            ipAddress = httpRequest.getRemoteAddr();
        }

        String userAgent = httpRequest.getHeader("User-Agent");
        String sessionId = this.sessionService.setSessionData(false, ipAddress, userAgent, null);

        return new Cookie("JSESSIONID", sessionId);
    }

    private void getSessionData (String sessionId, HttpServletRequest httpRequest) throws SessionException {
        Session sessionData = this.sessionService.getSession(sessionId);

        System.out.println("SESSION DATA" + sessionData.getSessionData());

        if (sessionData.getSessionData() == null) {
            this.setSession(httpRequest);
            return;
        }

        if (this.checkIfSessionIsValid(sessionData) && sessionData.getSessionData().isLogged()) {
            throw new SessionException.SessionExpiredException("Session already expired, please " +
                    "make a new login");
        }

        if (checkIfSessionIsValid(sessionData)) {
            this.setSession(httpRequest);
        }
    }

    private boolean checkIfSessionIsValid (Session sessionData) {
        return sessionData.getSessionData().isExpired() &&
                sessionData.getSessionData().expirationTime() >= System.currentTimeMillis() &&
                sessionData.getSessionData().sessionMaxIdleTime() >= sessionData.getSessionData().lastRequestTime();
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws ServletException, IOException {
        try {
            HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
            HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

            Cookie[] cookies = httpRequest.getCookies();
            if (cookies == null || cookies.length == 0) {
                Cookie sessionCookie = this.setSession(httpRequest);
                httpResponse.addCookie(sessionCookie);
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }

            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("JSESSIONID")) {
                    this.getSessionData(cookie.getValue(), httpRequest);
                    filterChain.doFilter(servletRequest, servletResponse);
                    return;
                }
            }
            throw new SessionException.InvalidSessionException("No session ID found");
        } catch (SessionException error) {
            this.errorHandling.filterError(error, servletResponse);
        }
    }
}


@Configuration
class FilterConfig {
    private final SessionService sessionService;
    private final ErrorHandlingFilters errorHandling;

    public FilterConfig(SessionService sessionService, ErrorHandlingFilters errorHandling) {
        this.sessionService = sessionService;
        this.errorHandling = errorHandling;
    }

    @Bean
    public SessionMiddleware sessionMiddleware() {
        return new SessionMiddleware(sessionService, errorHandling);
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