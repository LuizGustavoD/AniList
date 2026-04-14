package com.anilist.backend.server.controller.admin;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anilist.backend.server.DTO.admin.user.AdminUserCreateDTO;
import com.anilist.backend.server.DTO.admin.user.AdminUserUpdateDTO;
import com.anilist.backend.server.service.admin.AdminUserService;
import com.anilist.backend.server.service.exceptions.custom.UserNotFoundException;
import com.anilist.backend.server.DTO.response.user.UserResponseDTO;
import com.anilist.backend.server.DTO.response.user.UserListResponseDTO;
import com.anilist.backend.server.infra.http.success.SuccessAPIResponse;
import com.anilist.backend.server.infra.http.error.ErrorAPIResponse;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin/user")
public class AdminUserController {

    private final AdminUserService adminUserService;

    @GetMapping("/status")
    public ResponseEntity<?> getStatus() {
        return ResponseEntity.ok("AdminUserController is up and running!");
    }


    @PostMapping()
    public ResponseEntity<?> createUser(@RequestBody AdminUserCreateDTO dto) {
        try {
            UserResponseDTO createdUser = adminUserService.createUser(dto);
            return ResponseEntity.ok(new SuccessAPIResponse<>(createdUser, "User created successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ErrorAPIResponse<>(List.of(e.getMessage()), "Failed to create user"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ErrorAPIResponse<>(List.of(e.getMessage()), "Erro interno ao criar usuário"));
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable UUID id, @RequestBody AdminUserUpdateDTO dto) {
        try {
            UserResponseDTO updatedUser = adminUserService.updateUser(id, dto);
            return ResponseEntity.ok(new SuccessAPIResponse<>(updatedUser, "User updated successfully"));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorAPIResponse<>(List.of(e.getMessage()), "User not found"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ErrorAPIResponse<>(List.of(e.getMessage()), "Failed to update user"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ErrorAPIResponse<>(List.of(e.getMessage()), "Erro interno ao atualizar usuário"));
        }
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable UUID id) {
        try {
            SuccessAPIResponse<Void> response = adminUserService.deleteUserById(id);
            return ResponseEntity.ok(response);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorAPIResponse<>(List.of(e.getMessage()), "User not found"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ErrorAPIResponse<>(List.of(e.getMessage()), "Failed to delete user"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ErrorAPIResponse<>(List.of(e.getMessage()), "Erro interno ao deletar usuário"));
        }
    }


    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers() {
        UserListResponseDTO list = adminUserService.getAllUsers();
        return ResponseEntity.ok(new SuccessAPIResponse<>(list, "Listagem de usuários"));
    }
}
