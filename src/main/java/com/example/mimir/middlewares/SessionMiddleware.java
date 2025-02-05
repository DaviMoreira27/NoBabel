package com.example.mimir.middlewares;

import com.example.mimir.common.ErrorHandlingFilters;
import com.example.mimir.dto.SessionData;
import com.example.mimir.exceptions.DatabaseException;
import com.example.mimir.exceptions.GeneralException;
import com.example.mimir.exceptions.HttpClientException;
import com.example.mimir.exceptions.SessionException;
import com.example.mimir.services.SessionService;
import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// @Component annotation adds this middleware for every route
@Order(1)
public class SessionMiddleware extends OncePerRequestFilter implements Filter {
    private final SessionService sessionService;
    private final ErrorHandlingFilters errorHandling;

    public SessionMiddleware (SessionService sessionService, ErrorHandlingFilters errorHandlingFilters) {
        this.sessionService = sessionService;
        this.errorHandling = errorHandlingFilters;
    }

    private void createNewAnonymousSession(HttpServletRequest httpRequest,
                                           HttpServletResponse httpResponse, boolean isLogged) throws DatabaseException,
            IOException {

        try {
            Cookie newSessionCookie = this.sessionService.setDataSession(httpRequest, isLogged, null);
            httpResponse.addCookie(newSessionCookie);
        } catch (DatabaseException e) {
            this.errorHandling.filterError(e, httpResponse);
        } catch (RuntimeException e) {
            GeneralException.UnknownInternalException error =
                    new GeneralException.UnknownInternalException(e.getMessage());
            this.errorHandling.filterError(error, httpResponse);
        }

    }

    @Override
    public void doFilterInternal(HttpServletRequest httpRequest, HttpServletResponse httpResponse,
                         FilterChain filterChain) throws ServletException, IOException, DatabaseException {
        try {
            Cookie sessionCookie = this.sessionService.getCookie(httpRequest, "JSESSIONID");

            if (sessionCookie == null) {
                this.createNewAnonymousSession(httpRequest, httpResponse, true);
                filterChain.doFilter(httpRequest, httpResponse);
                return;
            }
            SessionData sessionData =
                    this.sessionService.getSessionData(sessionCookie.getValue());

            if (sessionData == null) {
                this.createNewAnonymousSession(httpRequest, httpResponse, false);
                filterChain.doFilter(httpRequest, httpResponse);
                return;
            }

            if (!this.sessionService.checkIpAndUserAgentIsValid(httpRequest, sessionData)) {
                this.sessionService.removeSessionData(sessionCookie.getValue());
                throw new SessionException.InvalidSessionException("It was not possible to " +
                        "confirm the user identity");
            }

            filterChain.doFilter(httpRequest, httpResponse);
        } catch (HttpClientException error) {
            this.errorHandling.filterError(error, httpResponse);
        } catch (RuntimeException e) {
            GeneralException.UnknownInternalException error =
                    new GeneralException.UnknownInternalException(e.getMessage());
            this.errorHandling.filterError(error, httpResponse);
        }
    }

}