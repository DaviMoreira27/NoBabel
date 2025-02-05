package com.example.mimir.services;


import com.example.mimir.dto.SessionData;
import com.example.mimir.entities.Session;
import com.example.mimir.exceptions.DatabaseException;
import com.example.mimir.exceptions.SessionException;
import com.example.mimir.repositories.SessionRepository;
import io.lettuce.core.RedisConnectionException;
import io.lettuce.core.RedisException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;


@Service
public class SessionService {
    private final SessionRepository sessionRepository;

    // Dependency Injection
    public SessionService (SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public Cookie getCookie (HttpServletRequest httpRequest, String name) {
        Cookie[] cookies = httpRequest.getCookies();
        if (cookies == null || cookies.length == 0) {
            return null;
        }

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(name)) {
                return cookie;
            }
        }

        throw new SessionException.NoCookiesFound("No cookies found");
    }

    public Cookie setDataSession (HttpServletRequest httpRequest, boolean isLogged,
                                  @Nullable String email) throws DatabaseException {
        String ipAddress = httpRequest.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
            ipAddress = httpRequest.getRemoteAddr();
        }

        String userAgent = httpRequest.getHeader("User-Agent");
        String sessionId = this.parseSessionData(isLogged, ipAddress, userAgent, email);

        return new Cookie("JSESSIONID", sessionId);
    }

    public SessionData getSessionData (String sessionId) throws SessionException {
        Session session = this.getSession(sessionId);

        if (session != null) {
            return session.getSessionData();
        }
        return null;
    }

    private Session findSession (String sessionId) throws SessionException {
        return this.getSession(sessionId);
    }

    public void removeSessionData (String sessionId) throws SessionException {
        try {
            SessionData sessionData = this.getSessionData(sessionId);

            if (sessionData != null) {
                this.sessionRepository.deleteById(sessionId);
            }
        } catch (RedisException error) {
            if (error.getCause() instanceof RedisConnectionException) {
                throw new DatabaseException.DatabaseConnectionException("Database connection error");
            }
            throw new DatabaseException.UnknownDatabaseException("Unknown database error");
        }
    }

    public boolean checkIfSessionIsInvalid(SessionData sessionData) {
        return sessionData.isExpired() ||
                System.currentTimeMillis() >= sessionData.expirationTime() ||
                (System.currentTimeMillis() - sessionData.lastRequestTime()) >= sessionData.sessionMaxIdleTime();
    }

    public boolean checkIpAndUserAgentIsValid (HttpServletRequest httpRequest,
                                           SessionData sessionData) {
        return Objects.equals(sessionData.ipAddress(), httpRequest.getRemoteAddr()) &&
                Objects.equals(sessionData.userAgent(), httpRequest.getHeader("User-Agent"));
    }

    public void setNewLastRequestTime (HttpServletRequest httpRequest, String sessionId) {
        try {
            Session session = this.getSession(sessionId);

            if (session == null) {
                return;
            }

            Date nowDate = new Date();

            SessionData newSessionData = new SessionData(
                    session.getSessionData().email(),
                    session.getSessionData().expirationTime(),
                    session.getSessionData().sessionStartTime(),
                    session.getSessionData().sessionMaxIdleTime(),
                    session.getSessionData().isExpired(),
                    session.getSessionData().userAgent(),
                    session.getSessionData().ipAddress(),
                    nowDate.getTime(),
                    session.getSessionData().isLogged()
            );

            session.setSessionData(newSessionData);
        } catch (RedisException error) {
            if (error.getCause() instanceof RedisConnectionException) {
                throw new DatabaseException.DatabaseConnectionException("Database connection error");
            }
            throw new DatabaseException.UnknownDatabaseException("Unknown database error");
        }
    }


    private String parseSessionData(boolean isLogged, String ipAddress, String userAgent,
                                    @Nullable String email) throws DatabaseException {
        // TODO: Modify expiration time and idle time if the user is logged
        String sessionId = UUID.randomUUID().toString();
        Date nowDate = new Date();
        // 2 Days
        long expirationDate = new Date(nowDate.getTime() + 48 * 60 * 60 * 1000).getTime();
        long maxIdleDate = 8 * 60 * 60 * 1000; // Max idle date
        SessionData sessionData = new SessionData(
                email,
                expirationDate,
                nowDate.getTime(),
                maxIdleDate,
                false,
                userAgent,
                ipAddress,
                nowDate.getTime(),
                isLogged
        );

        this.setSession(sessionId, sessionData);
        return sessionId;
    }

    private void setSession(String sessionId, SessionData sessionData) {
        try {
            Session sessionEntity = new Session();
            sessionEntity.setSessionData(sessionData);
            sessionEntity.setSessionId(sessionId);
            sessionRepository.save(sessionEntity);
        } catch (RedisException error) {
            if (error.getCause() instanceof RedisConnectionException) {
                throw new DatabaseException.DatabaseConnectionException("Database connection error");
            }
            throw new DatabaseException.UnknownDatabaseException("Unknown database error");
        }
    }

    private Session getSession(String sessionId) {
        try {
            return sessionRepository.findOneBySessionId(sessionId);
        } catch (RedisException error) {
            if (error.getCause() instanceof RedisConnectionException) {
                throw new DatabaseException.DatabaseConnectionException("Database connection error");
            }
            throw new DatabaseException.UnknownDatabaseException("Unknown database error");
        }
    }
}