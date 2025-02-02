package com.example.mimir.common;

import com.example.mimir.exceptions.HttpClientException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import com.example.mimir.dto.Error;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ErrorHandlingFilters {
    public void filterError(HttpClientException error, ServletResponse response) throws IOException {
        Error errorResponse = new Error(error.getStatus(), error.getMessage(), error.getHttpPath());
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setContentType("application/json");
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setStatus(error.getStatus().value());
        httpServletResponse.getWriter().write(convertObjectToJson(errorResponse));
    }

    private String convertObjectToJson(Object object) throws JsonProcessingException {
        if (object == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }
}

