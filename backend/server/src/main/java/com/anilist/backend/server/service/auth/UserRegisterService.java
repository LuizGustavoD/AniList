package com.anilist.backend.server.service.auth;

import com.anilist.backend.server.DTO.auth.UserRegisterDTO;
import com.anilist.backend.server.models.role.EnumRole;
import com.anilist.backend.server.models.role.RolesModel;
import com.anilist.backend.server.models.user.UserModel;
import com.anilist.backend.server.repository.user.RolesRepository;
import com.anilist.backend.server.repository.user.UserRepository;
import com.anilist.backend.server.security.jwt.JwtService;
import com.anilist.backend.server.service.mail.MailSenderService;

import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserRegisterService {

    private final UserRepository userRepository;
    private final RolesRepository rolesRepository;
    private final JwtService jwtService;
    private final MailSenderService mailSenderService;
    private final PasswordEncoder passwordEncoder;

    private final String confirmationBaseUrl = "http://localhost:8080/api/auth/register/confirm-email?token=";

    public String registerUser(UserRegisterDTO userRegisterDTO) {
        if (userRepository.existsByUsername(userRegisterDTO.username())) {
            throw new IllegalArgumentException("Username already exists");
        }

        if (userRepository.existsByEmail(userRegisterDTO.email())) {
            throw new IllegalArgumentException("Email already exists");
        }

        RolesModel userRole = rolesRepository.findByRole(EnumRole.USER)
                .orElseThrow(() -> new RuntimeException("Default role USER not found in database"));

        UserModel newUser = new UserModel();
        newUser.setUsername(userRegisterDTO.username());
        newUser.setPassword(passwordEncoder.encode(userRegisterDTO.password()));
        newUser.setEmail(userRegisterDTO.email());
        newUser.setRole(userRole);

        userRepository.save(newUser);

        String token = jwtService.generateConfirmToken(newUser);
        String confirmationLink = confirmationBaseUrl + token;
        try {
            mailSenderService.sendConfirmationEmail(newUser.getEmail(), newUser.getUsername(), confirmationLink);
        } catch (jakarta.mail.MessagingException e) {
            throw new RuntimeException("Failed to send confirmation email", e);
        }

        return "User registered successfully. Please check your email to confirm your account.";
    }

    public String confirmEmail(String token) {
        String tokenType = jwtService.getClaimFromToken(token, "type");
        if (!"confirm".equals(tokenType)) {
            throw new IllegalArgumentException("Invalid token type");
        }

        String username = jwtService.getUsernameFromToken(token);
        UserModel user = userRepository.findByUsername(username)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (user.isConfirmed()) {
            throw new IllegalStateException("Email already confirmed");
        }

        user.setConfirmed(true);
        userRepository.save(user);

        return jwtService.generateAuthToken(user);
    }
    
}
