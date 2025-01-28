package com.example.mimir.services;

import com.example.mimir.exceptions.http.client.SessionExpiredException;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSession;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;


public class Session implements HttpSession {
    private final String id;
    private final long creationTime;
    private final long lastAccessedTime;
    private int maxInactiveInterval = 1800; // Tempo padrão: 30 minutos
    private final Map<String, Object> attributes;
    private boolean isValid = true;

    public Session() {
        this.id = UUID.randomUUID().toString();
        this.creationTime = System.currentTimeMillis();
        this.lastAccessedTime = this.creationTime;
        this.attributes = new HashMap<>();
    }

    @Override
    public long getCreationTime() {
        return creationTime;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public long getLastAccessedTime() {
        return lastAccessedTime;
    }

    @Override
    public void setMaxInactiveInterval(int interval) {
        this.maxInactiveInterval = interval;
    }

    @Override
    public int getMaxInactiveInterval() {
        return maxInactiveInterval;
    }

    @Override
    public Object getAttribute(String name) {
        if (!isValid) {
            throw new SessionExpiredException("A sessão foi invalidada.") {
            };
        }
        return attributes.get(name);
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        if (!isValid) {
            throw new SessionExpiredException("A sessão foi invalidada.") {
            };
        }
        return new Enumeration<>() {
            private final Iterator<String> iterator = attributes.keySet().iterator();

            @Override
            public boolean hasMoreElements() {
                return iterator.hasNext();
            }

            @Override
            public String nextElement() {
                return iterator.next();
            }
        };
    }

    @Override
    public void setAttribute(String name, Object value) {
        if (!isValid) {
            throw new SessionExpiredException("A sessão foi invalidada.") {
            };
        }
        attributes.put(name, value);
    }

    @Override
    public void removeAttribute(String name) {
        if (!isValid) {
            throw new SessionExpiredException("A sessão foi invalidada.") {
            };
        }
        attributes.remove(name);
    }

    @Override
    public void invalidate() {
        isValid = false;
        attributes.clear();
    }

    @Override
    public boolean isNew() {
        return System.currentTimeMillis() - creationTime < 1000;
    }

    @Override
    public ServletContext getServletContext() {
        return null;
    }
}
