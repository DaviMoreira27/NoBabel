package com.example.mimir.middlewares;

import com.example.mimir.common.ErrorHandlingFilters;
import com.example.mimir.entities.Session;
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

@Order(3)
public class UpdateLastRequestMiddleware extends OncePerRequestFilter implements Filter {

    private final SessionService sessionService;
    private final ErrorHandlingFilters errorHandler;

    public UpdateLastRequestMiddleware (SessionService sessionService, ErrorHandlingFilters errorHandler) {
        this.sessionService = sessionService;
        this.errorHandler = errorHandler;
    }


    @Override
    public void doFilterInternal (HttpServletRequest httpRequest, HttpServletResponse servletResponse,
                                  FilterChain filterChain) throws IOException, ServletException {
        try {
            Cookie sessionId = this.sessionService.getCookie(httpRequest, "JSESSIONID");

            if (sessionId == null) {
                throw new SessionException.InvalidSessionException("No session ID found");
            }

            this.sessionService.setNewLastRequestTime(httpRequest,
                    sessionId.getValue());

            filterChain.doFilter(httpRequest, servletResponse);
        } catch (HttpClientException error) {
            this.errorHandler.filterError(error, servletResponse);

        } catch (RuntimeException e) {
            GeneralException.UnknownInternalException error =
                    new GeneralException.UnknownInternalException(e.getMessage());
            this.errorHandler.filterError(error, servletResponse);
        }
    }

}
