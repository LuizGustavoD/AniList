package com.anilist.backend.server.service.profile;

import com.anilist.backend.server.DTO.response.profile.UserProfilePictureUpdateResponseDTO;
import com.anilist.backend.server.DTO.response.profile.UserProfileResponseDTO;
import com.anilist.backend.server.infra.http.success.SuccessAPIResponse;

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

        public UserProfileResponseDTO getProfile(String username) {
        UserModel user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        String avatarUrl = user.getProfilePicture() != null ? "/api/profile/picture/" + user.getProfilePicture() : null;
        return new UserProfileResponseDTO(user.getId(), user.getUsername(), user.getEmail(), avatarUrl, user.getAbout());
        }

    public UserProfilePictureUpdateResponseDTO uploadProfilePicture(String username, MultipartFile file) {
        UserModel user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (user.getProfilePicture() != null) {
            fileStorageService.delete(user.getProfilePicture());
        }

        String filename = fileStorageService.store(file);
        user.setProfilePicture(filename);
        userRepository.save(user);

        return new UserProfilePictureUpdateResponseDTO(user.getId(), "/api/profile/picture/" + filename, "Foto de perfil atualizada");
    }

    public UserProfilePictureUpdateResponseDTO deleteProfilePicture(String username) {
        UserModel user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (user.getProfilePicture() != null) {
            fileStorageService.delete(user.getProfilePicture());
            user.setProfilePicture(null);
            userRepository.save(user);
        }

        return new UserProfilePictureUpdateResponseDTO(user.getId(), null, "Foto de perfil removida");
    }

    public Resource loadProfilePicture(String filename) {
        return fileStorageService.load(filename);
    }

    public SuccessAPIResponse<Void> changeUserAttibutes(ChangeUserAtributesDTO request, String username) {
        UserModel user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            return new SuccessAPIResponse<>(null, "Incorrect password. User attributes not updated.");
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
        return new SuccessAPIResponse<>(null, "User attributes updated successfully");
    }
}
