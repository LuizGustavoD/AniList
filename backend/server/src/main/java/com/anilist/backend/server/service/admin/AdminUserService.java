package com.anilist.backend.server.service.admin;

import org.springframework.stereotype.Service;

import com.anilist.backend.server.repository.user.UserRepository;
import com.anilist.backend.server.repository.user.RolesRepository;
import com.anilist.backend.server.models.user.UserModel;
import com.anilist.backend.server.DTO.response.user.UserResponseDTO;
import com.anilist.backend.server.DTO.response.user.UserListResponseDTO;
import com.anilist.backend.server.infra.http.success.SuccessAPIResponse;
import com.anilist.backend.server.models.role.RolesModel;
import com.anilist.backend.server.DTO.admin.user.AdminUserCreateDTO;
import com.anilist.backend.server.DTO.admin.user.AdminUserUpdateDTO;
import com.anilist.backend.server.service.exceptions.custom.UserNotFoundException;
import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AdminUserService {
    
    private final UserRepository userRepository;
    private final RolesRepository rolesRepository;

    public UserResponseDTO createUser(AdminUserCreateDTO dto) {
        userRepository.findByEmail(dto.email()).ifPresent(u -> {
            throw new com.anilist.backend.server.service.exceptions.custom.DuplicateEmailException("Email already exists: " + dto.email());
        });
        RolesModel rolesModel = rolesRepository.findByRole(dto.role())
                .orElseThrow(() -> new RuntimeException("Role not found: " + dto.role()));
        UserModel user = new UserModel();
        user.setUsername(dto.username());
        user.setPassword(dto.password());
        user.setEmail(dto.email());
        user.setAbout(dto.about());
        user.setProfilePicture(dto.profilePicture());
        user.setConfirmed(dto.isConfirmed());
        user.setRole(rolesModel);
        UserModel saved = userRepository.save(user);
        return new UserResponseDTO(saved.getId(), saved.getUsername(), saved.getEmail());
    }

    public UserListResponseDTO getAllUsers() {
        List<UserResponseDTO> dtos = userRepository.findAll().stream()
            .map(u -> new UserResponseDTO(u.getId(), u.getUsername(), u.getEmail()))
            .toList();
        return new UserListResponseDTO(dtos);
    }

    public UserResponseDTO getUserById(UUID id) {
        UserModel user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        return new UserResponseDTO(user.getId(), user.getUsername(), user.getEmail());
    }

    public UserResponseDTO getUserByUsername(String username) {
        UserModel user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));
        return new UserResponseDTO(user.getId(), user.getUsername(), user.getEmail());
    }

    public UserResponseDTO getUserByEmail(String email) {
        UserModel user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
        return new UserResponseDTO(user.getId(), user.getUsername(), user.getEmail());
    }

    public UserResponseDTO updateUser(UUID id, AdminUserUpdateDTO dto) {
        UserModel user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        user.setUsername(dto.username());
        user.setEmail(dto.email());
        user.setAbout(dto.about());
        user.setProfilePicture(dto.profilePicture());
        user.setConfirmed(dto.isConfirmed());
        UserModel updated = userRepository.save(user);
        return new UserResponseDTO(updated.getId(), updated.getUsername(), updated.getEmail());
    }

    public SuccessAPIResponse<Void> deleteUserById(UUID id) {
        UserModel user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        userRepository.delete(user);
        return new SuccessAPIResponse<>(null, "User deleted successfully");
    }

    public SuccessAPIResponse<Void> deleteUserByUsername(String username) {
        UserModel user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));
        userRepository.delete(user);
        return new SuccessAPIResponse<>(null, "User deleted successfully");
    }

    public SuccessAPIResponse<Void> deleteUserByEmail(String email) {
        UserModel user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
        userRepository.delete(user);
        return new SuccessAPIResponse<>(null, "User deleted successfully");
    }

}

