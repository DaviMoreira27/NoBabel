package com.example.mimir.services;


import com.example.mimir.dto.SessionData;
import com.example.mimir.entities.Session;
import com.example.mimir.exceptions.database.DatabaseConnectionException;
import com.example.mimir.exceptions.database.UnknownDatabaseException;
import io.lettuce.core.RedisConnectionException;
import io.lettuce.core.RedisException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;


@Service
public class SessionService {
    private RedisTemplate<String, Object> redisTemplate = null;

    // Dependency Injection
    public SessionService (RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void setSession(String sessionId, SessionData sessionData) {
        try {
            redisTemplate.opsForSet().add("session:" + sessionId, sessionData);
        } catch (RedisException error) {
            if (error.getCause() instanceof RedisConnectionException) {
                throw new DatabaseConnectionException("Database connection error");
            }
            throw new UnknownDatabaseException("Unknown database error");
        }
    }

    public Session getSession(String sessionId) {
        try {
            return (Session) redisTemplate.opsForValue().get("session:" + sessionId);
        } catch (RedisException error) {
            if (error.getCause() instanceof RedisConnectionException) {
                throw new DatabaseConnectionException("Database connection error");
            }
            throw new UnknownDatabaseException("Unknown database error");
        }
    }
}