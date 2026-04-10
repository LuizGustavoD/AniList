package com.anilist.backend.server.controller.anime;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anilist.backend.server.DTO.anime.AnimeReviewDTO;
import com.anilist.backend.server.DTO.anime.UserAnimeAddDTO;
import com.anilist.backend.server.DTO.anime.UserAnimeDeleteDTO;
import com.anilist.backend.server.DTO.anime.UserAnimeFavoriteDTO;
import com.anilist.backend.server.DTO.anime.UserAnimeStatusUpdateDTO;
import com.anilist.backend.server.service.anime.UserAnimeRelationshipService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/anime")
public class AnimeController {

    private final UserAnimeRelationshipService userAnimeRelationshipService;

    @PostMapping("/add")
    public ResponseEntity<String> addAnimeToList(@Valid @RequestBody UserAnimeAddDTO request, JwtAuthenticationToken authentication) {
        String username = authentication.getToken().getSubject();
        return ResponseEntity.ok(userAnimeRelationshipService.addUserAnimeRelationship(
                new UserAnimeAddDTO(username, request.animeId(), request.status())));
    }

    @DeleteMapping("/remove")
    public ResponseEntity<String> removeAnimeFromList(@Valid @RequestBody UserAnimeDeleteDTO request, JwtAuthenticationToken authentication) {
        String username = authentication.getToken().getSubject();
        return ResponseEntity.ok(userAnimeRelationshipService.deleteUserAnimeRelationship(
                new UserAnimeDeleteDTO(username, request.animeId())));
    }

    @PutMapping("/update/status")
    public ResponseEntity<String> updateAnimeStatus(@Valid @RequestBody UserAnimeStatusUpdateDTO request, JwtAuthenticationToken authentication) {
        String username = authentication.getToken().getSubject();
        return ResponseEntity.ok(userAnimeRelationshipService.updateAnimeStatus(
                new UserAnimeStatusUpdateDTO(username, request.animeId(), request.status())));
    }

    @PutMapping("/update/favorite")
    public ResponseEntity<String> toggleFavorite(@Valid @RequestBody UserAnimeFavoriteDTO request, JwtAuthenticationToken authentication) {
        String username = authentication.getToken().getSubject();
        return ResponseEntity.ok(userAnimeRelationshipService.toggleFavorite(
                new UserAnimeFavoriteDTO(username, request.animeId(), request.favorite())));
    }

    @PostMapping("/review")
    public ResponseEntity<String> addReview(@Valid @RequestBody AnimeReviewDTO request, JwtAuthenticationToken authentication) {
        String username = authentication.getToken().getSubject();
        return ResponseEntity.ok(userAnimeRelationshipService.addReviewToAnime(
                new AnimeReviewDTO(username, request.animeId(), request.reviewText(), request.rating())));
    }

    @DeleteMapping("/review")
    public ResponseEntity<String> deleteReview(@Valid @RequestBody UserAnimeDeleteDTO request, JwtAuthenticationToken authentication) {
        String username = authentication.getToken().getSubject();
        return ResponseEntity.ok(userAnimeRelationshipService.deleteReview(
                new UserAnimeDeleteDTO(username, request.animeId())));
    }

}
