package com.anilist.backend.server.controller.exceptions.handler;

import java.io.IOException;
import java.time.Instant;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.anilist.backend.server.controller.exceptions.StandartError;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Component
public class InvalidTokenExceptionHandler implements AuthenticationEntryPoint  {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
        StandartError error = new StandartError();
        error.setTimestamp(Instant.now());
        error.setStatus(401);
        error.setError("Unauthorized");
        error.setMessage("Token inválido, expirado ou ausente.");

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        
        objectMapper.registerModule(new JavaTimeModule()); 
        response.getWriter().write(objectMapper.writeValueAsString(error));
    }
    
}
