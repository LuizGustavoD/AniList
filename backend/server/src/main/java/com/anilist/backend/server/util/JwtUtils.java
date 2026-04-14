package com.anilist.backend.server.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.util.UUID;

public class JwtUtils {
    public static UUID extractUserId(String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        Claims claims = Jwts.parserBuilder()
                .build()
                .parseClaimsJws(token)
                .getBody();
        String userId = claims.getSubject();
        return UUID.fromString(userId);
    }
}
