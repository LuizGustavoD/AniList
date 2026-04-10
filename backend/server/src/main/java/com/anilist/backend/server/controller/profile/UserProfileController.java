package com.anilist.backend.server.controller.profile;

import java.util.Map;

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
    public ResponseEntity<Map<String, Object>> getMyProfile(JwtAuthenticationToken authentication) {
        String username = authentication.getToken().getSubject();
        return ResponseEntity.ok(userProfileService.getProfile(username));
    }

    @PostMapping(value = "/picture", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> uploadProfilePicture(
            JwtAuthenticationToken authentication,
            @RequestParam("file") MultipartFile file) {
        String username = authentication.getToken().getSubject();
        return ResponseEntity.ok(userProfileService.uploadProfilePicture(username, file));
    }

    @DeleteMapping("/picture")
    public ResponseEntity<Map<String, String>> deleteProfilePicture(JwtAuthenticationToken authentication) {
        String username = authentication.getToken().getSubject();
        return ResponseEntity.ok(userProfileService.deleteProfilePicture(username));
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
    public ResponseEntity<String> updateUserAttributes(JwtAuthenticationToken authentication, @RequestBody ChangeUserAtributesDTO request) {
        String username = authentication.getToken().getSubject();

        return ResponseEntity.ok(userProfileService.changeUserAttibutes(request, username));
    }

}

