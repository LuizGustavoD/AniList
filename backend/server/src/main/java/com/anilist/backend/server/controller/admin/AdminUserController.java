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
import com.anilist.backend.server.controller.exceptions.StandartError;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;


@Tag(name = "Admin Usuário", description = "Operações administrativas de usuário")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin/user")
public class AdminUserController {

    private final AdminUserService adminUserService;


    @Operation(summary = "Status do controller", description = "Verifica se o controller de admin usuário está ativo.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Controller ativo",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class)))
    })
    @io.github.resilience4j.ratelimiter.annotation.RateLimiter(name = "adminUserCreateLimiter")
    @GetMapping("/status")
    public ResponseEntity<?> getStatus() {
        return ResponseEntity.ok("AdminUserController is up and running!");
    }



    @Operation(summary = "Criar usuário", description = "Cria um novo usuário administrativo.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuário criado com sucesso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = SuccessAPIResponse.class))),
        @ApiResponse(responseCode = "400", description = "Erro de validação ou negócio",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandartError.class))),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandartError.class)))
    })
    @io.github.resilience4j.ratelimiter.annotation.RateLimiter(name = "adminUserCreateLimiter")
    @PostMapping()
    public ResponseEntity<?> createUser(
            @RequestBody AdminUserCreateDTO dto,
            jakarta.servlet.http.HttpServletRequest request) {
        try {
            UserResponseDTO createdUser = adminUserService.createUser(dto);
            return ResponseEntity.ok(new SuccessAPIResponse<>(createdUser, "User created successfully"));
        } catch (RuntimeException e) {
            StandartError error = new StandartError();
            error.setTimestamp(java.time.Instant.now());
            error.setStatus(400);
            error.setError("Bad Request");
            error.setMessage("Failed to create user");
            error.setPath(request.getRequestURI());
            error.setErrors(List.of(e.getMessage()));
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            StandartError error = new StandartError();
            error.setTimestamp(java.time.Instant.now());
            error.setStatus(500);
            error.setError("Internal Server Error");
            error.setMessage("Erro interno ao criar usuário");
            error.setPath(request.getRequestURI());
            error.setErrors(List.of(e.getMessage()));
            return ResponseEntity.status(500).body(error);
        }
    }



    @Operation(summary = "Atualizar usuário", description = "Atualiza os dados de um usuário administrativo.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = SuccessAPIResponse.class))),
        @ApiResponse(responseCode = "400", description = "Erro de validação ou negócio",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandartError.class))),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandartError.class))),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandartError.class)))
    })
    @io.github.resilience4j.ratelimiter.annotation.RateLimiter(name = "adminUserCreateLimiter")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(
            @Schema(description = "ID do usuário") @PathVariable UUID id,
            @RequestBody AdminUserUpdateDTO dto,
            jakarta.servlet.http.HttpServletRequest request) {
        try {
            UserResponseDTO updatedUser = adminUserService.updateUser(id, dto);
            return ResponseEntity.ok(new SuccessAPIResponse<>(updatedUser, "User updated successfully"));
        } catch (UserNotFoundException e) {
            StandartError error = new StandartError();
            error.setTimestamp(java.time.Instant.now());
            error.setStatus(404);
            error.setError("Not Found");
            error.setMessage("User not found");
            error.setPath(request.getRequestURI());
            error.setErrors(List.of(e.getMessage()));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (RuntimeException e) {
            StandartError error = new StandartError();
            error.setTimestamp(java.time.Instant.now());
            error.setStatus(400);
            error.setError("Bad Request");
            error.setMessage("Failed to update user");
            error.setPath(request.getRequestURI());
            error.setErrors(List.of(e.getMessage()));
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            StandartError error = new StandartError();
            error.setTimestamp(java.time.Instant.now());
            error.setStatus(500);
            error.setError("Internal Server Error");
            error.setMessage("Erro interno ao atualizar usuário");
            error.setPath(request.getRequestURI());
            error.setErrors(List.of(e.getMessage()));
            return ResponseEntity.status(500).body(error);
        }
    }



    @Operation(summary = "Deletar usuário", description = "Remove um usuário administrativo pelo ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuário removido com sucesso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = SuccessAPIResponse.class))),
        @ApiResponse(responseCode = "400", description = "Erro de validação ou negócio",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandartError.class))),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandartError.class))),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandartError.class)))
    })
    @io.github.resilience4j.ratelimiter.annotation.RateLimiter(name = "adminUserCreateLimiter")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(
            @Schema(description = "ID do usuário") @PathVariable UUID id,
            jakarta.servlet.http.HttpServletRequest request) {
        try {
            SuccessAPIResponse<Void> response = adminUserService.deleteUserById(id);
            return ResponseEntity.ok(response);
        } catch (UserNotFoundException e) {
            StandartError error = new StandartError();
            error.setTimestamp(java.time.Instant.now());
            error.setStatus(404);
            error.setError("Not Found");
            error.setMessage("User not found");
            error.setPath(request.getRequestURI());
            error.setErrors(List.of(e.getMessage()));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (RuntimeException e) {
            StandartError error = new StandartError();
            error.setTimestamp(java.time.Instant.now());
            error.setStatus(400);
            error.setError("Bad Request");
            error.setMessage("Failed to delete user");
            error.setPath(request.getRequestURI());
            error.setErrors(List.of(e.getMessage()));
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            StandartError error = new StandartError();
            error.setTimestamp(java.time.Instant.now());
            error.setStatus(500);
            error.setError("Internal Server Error");
            error.setMessage("Erro interno ao deletar usuário");
            error.setPath(request.getRequestURI());
            error.setErrors(List.of(e.getMessage()));
            return ResponseEntity.status(500).body(error);
        }
    }


    @Operation(summary = "Listar todos os usuários", description = "Retorna a lista de todos os usuários administrativos.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = SuccessAPIResponse.class)))
    })
    @io.github.resilience4j.ratelimiter.annotation.RateLimiter(name = "adminUserCreateLimiter")
    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers() {
        UserListResponseDTO list = adminUserService.getAllUsers();
        return ResponseEntity.ok(new SuccessAPIResponse<>(list, "Listagem de usuários"));
    }
}
