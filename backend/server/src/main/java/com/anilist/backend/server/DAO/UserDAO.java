package com.anilist.backend.server.DAO;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class UserDAO {

    private final String username;
    private final String email;
    private final String password;
     
    
}
