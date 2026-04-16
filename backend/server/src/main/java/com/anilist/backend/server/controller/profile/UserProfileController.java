package com.anilist.backend.server.controller.profile;

import com.anilist.backend.server.DTO.response.profile.UserProfileResponseDTO;
import com.anilist.backend.server.DTO.response.profile.UserProfilePictureUpdateResponseDTO;
import com.anilist.backend.server.infra.http.success.SuccessAPIResponse;


import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;


import com.anilist.backend.server.DTO.profile.ChangeUserAtributesDTO;
import com.anilist.backend.server.service.profile.UserProfileService;

import lombok.RequiredArgsConstructor;


@Tag(name = "Perfil", description = "Operações relacionadas ao perfil do usuário")
@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;


    @Operation(summary = "Obter perfil do usuário logado", description = "Retorna os dados do perfil do usuário autenticado.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Perfil retornado com sucesso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = SuccessAPIResponse.class)))
    })
    @io.github.resilience4j.ratelimiter.annotation.RateLimiter(name = "userProfileLimiter")
    @GetMapping("/me")
    public ResponseEntity<?> getMyProfile(JwtAuthenticationToken authentication) {
        String username = authentication.getToken().getSubject();
        UserProfileResponseDTO dto = userProfileService.getProfile(username);
        return ResponseEntity.ok(new SuccessAPIResponse<>(dto, "Perfil do usuário"));
    }


    @Operation(summary = "Enviar foto de perfil", description = "Faz upload de uma nova foto de perfil para o usuário autenticado.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Foto enviada com sucesso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = SuccessAPIResponse.class)))
    })
    @io.github.resilience4j.ratelimiter.annotation.RateLimiter(name = "userProfileLimiter")
    @PostMapping(value = "/picture", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadProfilePicture(
            JwtAuthenticationToken authentication,
            @RequestParam("file") MultipartFile file) {
        String username = authentication.getToken().getSubject();
        UserProfilePictureUpdateResponseDTO dto = userProfileService.uploadProfilePicture(username, file);
        return ResponseEntity.ok(new SuccessAPIResponse<>(dto, dto.message()));
    }


    @Operation(summary = "Remover foto de perfil", description = "Remove a foto de perfil do usuário autenticado.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Foto removida com sucesso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = SuccessAPIResponse.class)))
    })
    @io.github.resilience4j.ratelimiter.annotation.RateLimiter(name = "userProfileLimiter")
    @DeleteMapping("/picture")
    public ResponseEntity<?> deleteProfilePicture(JwtAuthenticationToken authentication) {
        String username = authentication.getToken().getSubject();
        UserProfilePictureUpdateResponseDTO dto = userProfileService.deleteProfilePicture(username);
        return ResponseEntity.ok(new SuccessAPIResponse<>(dto, dto.message()));
    }


    @Operation(summary = "Obter foto de perfil", description = "Retorna a foto de perfil pelo nome do arquivo.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Foto retornada com sucesso",
            content = @Content(mediaType = "image/jpeg"))
    })
    @io.github.resilience4j.ratelimiter.annotation.RateLimiter(name = "userProfileLimiter")
    @GetMapping("/picture/{filename}")
    public ResponseEntity<Resource> getProfilePicture(@PathVariable String filename) {
        Resource resource = userProfileService.loadProfilePicture(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
                .header(HttpHeaders.CACHE_CONTROL, "max-age=86400")
                .body(resource);
    }


    @Operation(summary = "Atualizar atributos do usuário", description = "Atualiza os dados do perfil do usuário autenticado.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Atributos atualizados com sucesso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = SuccessAPIResponse.class)))
    })
    @io.github.resilience4j.ratelimiter.annotation.RateLimiter(name = "userProfileLimiter")
    @PostMapping("/update")
    public ResponseEntity<?> updateUserAttributes(
            JwtAuthenticationToken authentication,
            @RequestBody ChangeUserAtributesDTO request) {
        String username = authentication.getToken().getSubject();
        SuccessAPIResponse<Void> response = userProfileService.changeUserAttibutes(request, username);
        return ResponseEntity.ok(response);
    }

}

