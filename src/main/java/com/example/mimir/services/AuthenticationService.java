package com.example.mimir.services;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.SecureRandom;

@Service
public class AuthenticationService {
    public void generateState(HttpSession  session) {
        String state = new BigInteger(255, new SecureRandom()).toString(32);
        session.setAttribute("state", state);
    }
}
