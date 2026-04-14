package com.anilist.backend.server.controller.exceptions.handler;

import java.io.IOException;
import java.time.Instant;

import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.anilist.backend.server.controller.exceptions.StandartError;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class AccesDeniedExceptionHandler implements AccessDeniedHandler {
    
    private final ObjectMapper objectMapper;
    
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
            org.springframework.security.access.AccessDeniedException accessDeniedException)
            throws IOException, ServletException {
               
        StandartError error = new StandartError();
        error.setTimestamp(Instant.now());
        error.setStatus(403);
        error.setError("User Forbidden Access");
        error.setMessage("Você não possui a role necessária para este recurso.");

        response.setStatus(403);
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(error));
    }
}
