package com.example.mimir.repositories;

import com.example.mimir.entities.Session;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface SessionRepository extends CrudRepository<Session, String> {

    public Session findOneBySessionId(String sessionId);
}
