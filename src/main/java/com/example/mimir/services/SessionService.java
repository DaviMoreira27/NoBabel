package com.example.mimir.services;


import com.example.mimir.dto.SessionData;
import com.example.mimir.entities.Session;
import com.example.mimir.exceptions.DatabaseException;
import com.example.mimir.repositories.SessionRepository;
import io.lettuce.core.RedisConnectionException;
import io.lettuce.core.RedisException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;


@Service
public class SessionService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final SessionRepository sessionRepository;

    // Dependency Injection
    public SessionService (RedisTemplate<String, Object> redisTemplate, SessionRepository sessionRepository) {
        this.redisTemplate = redisTemplate;
        this.sessionRepository = sessionRepository;
    }

    public String setSessionData(boolean isLogged, String ipAddress, String userAgent,
                               @Nullable String email) {
        String sessionId = UUID.randomUUID().toString();
        Date nowDate = new Date();
        long expirationDate = new Date(nowDate.getTime() + 3 * 24 * 60 * 60 * 1000).getTime(); // 3
        // Days
        long maxIdleDate = new Date(nowDate.getTime() + 24 * 60 * 60 * 1000).getTime(); // 1 Day
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

    protected void setSession(String sessionId, SessionData sessionData) {
        try {
            Session sessionEntity = new Session();
            sessionEntity.setSessionData(sessionData);
            sessionEntity.setUserId(sessionId);
            sessionRepository.save(sessionEntity);
        } catch (RedisException error) {
            if (error.getCause() instanceof RedisConnectionException) {
                throw new DatabaseException.DatabaseConnectionException("Database connection error");
            }
            throw new DatabaseException.UnknownDatabaseException("Unknown database error");
        }
    }

    public Session getSession(String sessionId) {
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