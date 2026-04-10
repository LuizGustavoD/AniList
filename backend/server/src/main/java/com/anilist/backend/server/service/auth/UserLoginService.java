package com.anilist.backend.server.service.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.anilist.backend.server.DTO.auth.UserLoginDTO;
import com.anilist.backend.server.repository.user.UserRepository;
import com.anilist.backend.server.security.jwt.JwtService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserLoginService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public String login(UserLoginDTO userLoginDTO) {
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userLoginDTO.username(), userLoginDTO.password())
            );
        } catch (DisabledException e) {
            throw new IllegalStateException("Email not confirmed. Please check your email.");
        }

        var user = userRepository.findByUsername(userLoginDTO.username())
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return jwtService.generateAuthToken(user);
    }
}
