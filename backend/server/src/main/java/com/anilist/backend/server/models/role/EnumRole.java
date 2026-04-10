package com.anilist.backend.server.models.role;

import org.springframework.security.core.GrantedAuthority;

public enum EnumRole implements GrantedAuthority {
    USER,
    ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }
    
}
