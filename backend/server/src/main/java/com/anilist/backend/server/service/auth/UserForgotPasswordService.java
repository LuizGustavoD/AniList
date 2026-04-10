package com.anilist.backend.server.service.auth;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.anilist.backend.server.DTO.auth.UserForgotPasswordDTO;
import com.anilist.backend.server.DTO.auth.UserForgotPasswordVerifyAccountDTO;
import com.anilist.backend.server.models.user.UserModel;
import com.anilist.backend.server.repository.user.UserRepository;
import com.anilist.backend.server.security.jwt.JwtService;
import com.anilist.backend.server.service.mail.MailSenderService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserForgotPasswordService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final MailSenderService mailSenderService;

    private String forgotPasswordBaseUrl = "http://localhost:8080/api/auth/reset-password/confirm?token=";

    public String sendForgotRequest (UserForgotPasswordVerifyAccountDTO request){
        UserModel user = userRepository.findByEmail(request.email())
            .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + request.email()));

        String token = jwtService.generateResetPasswordToken(user);
        String confirmationLink = forgotPasswordBaseUrl + token;

        try {
            mailSenderService.sendPasswordResetEmail(user.getEmail(), user.getUsername(), confirmationLink);
        } catch (jakarta.mail.MessagingException e) {
            throw new RuntimeException("Failed to send password reset email", e);
        }

        return "Password reset email sent successfully. Please check your email.";
    }

    public String resetPassword (UserForgotPasswordDTO request, String token){
        
        if (!jwtService.validateResetPasswordToken(token)) {
            throw new IllegalArgumentException("Invalid or expired token");
        }

        String email = jwtService.getClaimFromToken(token, "email");
        UserModel user = userRepository.findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);

        return "Password reset successfully.";

    }

}
