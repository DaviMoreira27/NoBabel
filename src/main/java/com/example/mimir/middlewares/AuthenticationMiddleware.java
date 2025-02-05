package com.example.mimir.middlewares;

import com.example.mimir.common.ErrorHandlingFilters;
import com.example.mimir.dto.SessionData;
import com.example.mimir.exceptions.AuthenticationException;
import com.example.mimir.exceptions.SessionException;
import com.example.mimir.services.SessionService;
import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.annotation.Order;

import java.io.IOException;
import java.util.Set;

@Order(2)
public class AuthenticationMiddleware implements Filter {

    private final ErrorHandlingFilters errorHandler;
    private final SessionService sessionService;

    private static final Set<String> AUTHENTICATED_ROUTES = Set.of(
            "/api/user/info",
            "/api/test"
    );


    public AuthenticationMiddleware (ErrorHandlingFilters errorHandler, SessionService sessionService) {
        this.errorHandler = errorHandler;
        this.sessionService = sessionService;
    }

    @Override
    public void doFilter (ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        try {
            Cookie getSessionId = this.sessionService.getCookie(httpServletRequest,
                    "JSESSIONID");

            if (getSessionId == null) {
                throw new SessionException.NoCookiesFound("No Cookie Found");
            }

            SessionData getSession = this.sessionService.getSessionData(getSessionId.getValue());

            if (AUTHENTICATED_ROUTES.contains(httpServletRequest.getRequestURI())) {
                if (getSession == null || this.sessionService.checkIfSessionIsInvalid(getSession)) {
                    throw new SessionException.SessionExpiredException("No valid session found");
                }
                if (!getSession.isLogged()) {
                    throw new AuthenticationException.UserNotAuthenticated("User not authenticated");
                }
            }

            filterChain.doFilter(servletRequest, servletResponse);
        } catch (AuthenticationException error) {
            this.errorHandler.filterError(error, servletResponse);
        }
    }
}