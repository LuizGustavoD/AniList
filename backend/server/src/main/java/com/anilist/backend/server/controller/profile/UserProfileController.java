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

import com.anilist.backend.server.DTO.profile.ChangeUserAtributesDTO;
import com.anilist.backend.server.service.profile.UserProfileService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;

    @GetMapping("/me")
    public ResponseEntity<?> getMyProfile(JwtAuthenticationToken authentication) {
        String username = authentication.getToken().getSubject();
        UserProfileResponseDTO dto = userProfileService.getProfile(username);
        return ResponseEntity.ok(new SuccessAPIResponse<>(dto, "Perfil do usuário"));
    }

    @PostMapping(value = "/picture", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadProfilePicture(
            JwtAuthenticationToken authentication,
            @RequestParam("file") MultipartFile file) {
        String username = authentication.getToken().getSubject();
        UserProfilePictureUpdateResponseDTO dto = userProfileService.uploadProfilePicture(username, file);
        return ResponseEntity.ok(new SuccessAPIResponse<>(dto, dto.message()));
    }

    @DeleteMapping("/picture")
    public ResponseEntity<?> deleteProfilePicture(JwtAuthenticationToken authentication) {
        String username = authentication.getToken().getSubject();
        UserProfilePictureUpdateResponseDTO dto = userProfileService.deleteProfilePicture(username);
        return ResponseEntity.ok(new SuccessAPIResponse<>(dto, dto.message()));
    }

    @GetMapping("/picture/{filename}")
    public ResponseEntity<Resource> getProfilePicture(@PathVariable String filename) {
        Resource resource = userProfileService.loadProfilePicture(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
                .header(HttpHeaders.CACHE_CONTROL, "max-age=86400")
                .body(resource);
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateUserAttributes(JwtAuthenticationToken authentication, @RequestBody ChangeUserAtributesDTO request) {
        String username = authentication.getToken().getSubject();
        SuccessAPIResponse<Void> response = userProfileService.changeUserAttibutes(request, username);
        return ResponseEntity.ok(response);
    }

}

