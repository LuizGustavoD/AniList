package com.anilist.backend.server.service.profile;

import java.util.HashMap;
import java.util.Map;

import org.springframework.core.io.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.anilist.backend.server.DTO.profile.ChangeUserAtributesDTO;
import com.anilist.backend.server.models.user.UserModel;
import com.anilist.backend.server.repository.user.UserRepository;
import com.anilist.backend.server.service.storage.FileStorageService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;

    public Map<String, Object> getProfile(String username) {
        UserModel user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Map<String, Object> response = new HashMap<>();
        response.put("id", user.getId());
        response.put("username", user.getUsername());
        response.put("email", user.getEmail());
        response.put("role", user.getRole().getRole().name());
        response.put("createdAt", user.getCreatedAt().toString());
        response.put("profilePictureUrl", user.getProfilePicture() != null
                ? "/api/profile/picture/" + user.getProfilePicture()
                : null);

        return response;
    }

    public Map<String, String> uploadProfilePicture(String username, MultipartFile file) {
        UserModel user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (user.getProfilePicture() != null) {
            fileStorageService.delete(user.getProfilePicture());
        }

        String filename = fileStorageService.store(file);
        user.setProfilePicture(filename);
        userRepository.save(user);

        return Map.of(
                "message", "Foto de perfil atualizada",
                "profilePictureUrl", "/api/profile/picture/" + filename
        );
    }

    public Map<String, String> deleteProfilePicture(String username) {
        UserModel user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (user.getProfilePicture() != null) {
            fileStorageService.delete(user.getProfilePicture());
            user.setProfilePicture(null);
            userRepository.save(user);
        }

        return Map.of("message", "Foto de perfil removida");
    }

    public Resource loadProfilePicture(String filename) {
        return fileStorageService.load(filename);
    }

    public String changeUserAttibutes(ChangeUserAtributesDTO request, String username) {
        UserModel user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            return "Incorrect password. User attributes not updated.";
        }

        if (request.email() != null) {
            user.setEmail(request.email());
        }
        if (request.about() != null) {
            user.setAbout(request.about());
        }
        if (request.profilePictureUrl() != null) {
            user.setProfilePicture(request.profilePictureUrl());
        }

        userRepository.save(user);
        return "User attributes updated successfully";

    }
}
