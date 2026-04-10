package com.anilist.backend.server.security.jwt;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import com.anilist.backend.server.models.user.UserModel;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtService {
    
    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;

    @Value("${jwt.login-expiration}")
    private long loginExpiration;

    @Value("${jwt.confirm-expiration}")
    private long confirmExpiration;

    public String generateResetPasswordToken(UserModel user) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
            .subject(user.getUsername())
            .claim("email", user.getEmail())
            .claim("type", "reset")
            .issuedAt(now)
            .expiresAt(now.plusMillis(confirmExpiration))
            .build();
        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public String generateConfirmToken(UserModel user) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
            .subject(user.getUsername())
            .claim("email", user.getEmail())
            .claim("type", "confirm")
            .issuedAt(now)
            .expiresAt(now.plusMillis(confirmExpiration))
            .build();
        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
    
    public String generateAuthToken(UserModel user) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
            .subject(user.getUsername())
            .claim("role", user.getRole().getRole().name())
            .claim("email", user.getEmail())
            .claim("type", "auth")
            .issuedAt(now)
            .expiresAt(now.plusMillis(loginExpiration))
            .build();
        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public boolean validateToken(String token) {
        try {
            jwtDecoder.decode(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean validateResetPasswordToken(String token) {
        try {
            var decoded = jwtDecoder.decode(token);
            String tokenType = decoded.getClaimAsString("type");
            return "reset".equals(tokenType);
        } catch (Exception e) {
            return false;
        }
    }

    public String getUsernameFromToken(String token) {
        return jwtDecoder.decode(token).getSubject();
    }

    public String getClaimFromToken(String token, String claim) {
        return jwtDecoder.decode(token).getClaimAsString(claim);
    }
}
