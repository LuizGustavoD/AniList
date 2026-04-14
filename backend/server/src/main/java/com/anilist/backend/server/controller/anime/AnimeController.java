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

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.anilist.backend.server.DTO.response.anime.AnimeListResponseDTO;
import com.anilist.backend.server.DTO.response.anime.AnimeFavoriteResponseDTO;
import com.anilist.backend.server.DTO.response.anime.AnimeStatusUpdateResponseDTO;
import com.anilist.backend.server.infra.http.success.SuccessAPIResponse;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/anime")
public class AnimeController {

    private final UserAnimeRelationshipService userAnimeRelationshipService;

    @GetMapping("/search")
    public ResponseEntity<?> searchAnime(@RequestParam String query, JwtAuthenticationToken authentication) {
        AnimeListResponseDTO results = userAnimeRelationshipService.searchAnimeWithCache(query);
        return ResponseEntity.ok(new SuccessAPIResponse<>(results, "Busca de animes"));
    }

    @PostMapping("/add")
    public ResponseEntity<?> addAnimeToList(@Valid @RequestBody UserAnimeAddDTO request, JwtAuthenticationToken authentication) {
        String username = authentication.getToken().getSubject();
        SuccessAPIResponse<Void> response = userAnimeRelationshipService.addUserAnimeRelationship(
                new UserAnimeAddDTO(username, request.animeId(), request.status()));
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<?> removeAnimeFromList(@Valid @RequestBody UserAnimeDeleteDTO request, JwtAuthenticationToken authentication) {
        String username = authentication.getToken().getSubject();
        SuccessAPIResponse<Void> response = userAnimeRelationshipService.deleteUserAnimeRelationship(
                new UserAnimeDeleteDTO(username, request.animeId()));
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/status")
    public ResponseEntity<?> updateAnimeStatus(@Valid @RequestBody UserAnimeStatusUpdateDTO request, JwtAuthenticationToken authentication) {
        String username = authentication.getToken().getSubject();
        AnimeStatusUpdateResponseDTO response = userAnimeRelationshipService.updateAnimeStatus(
                new UserAnimeStatusUpdateDTO(username, request.animeId(), request.status()));
        return ResponseEntity.ok(new SuccessAPIResponse<>(response, response.message()));
    }

    @PutMapping("/update/favorite")
    public ResponseEntity<?> toggleFavorite(@Valid @RequestBody UserAnimeFavoriteDTO request, JwtAuthenticationToken authentication) {
        String username = authentication.getToken().getSubject();
        AnimeFavoriteResponseDTO response = userAnimeRelationshipService.toggleFavorite(
                new UserAnimeFavoriteDTO(username, request.animeId(), request.favorite()));
        return ResponseEntity.ok(new SuccessAPIResponse<>(response, response.isFavorite() ? "Favorito atualizado" : "Favorito removido"));
    }

    @PostMapping("/review")
    public ResponseEntity<?> addReview(@Valid @RequestBody AnimeReviewDTO request, JwtAuthenticationToken authentication) {
        String username = authentication.getToken().getSubject();
        SuccessAPIResponse<Void> response = userAnimeRelationshipService.addReviewToAnime(
                new AnimeReviewDTO(username, request.animeId(), request.reviewText(), request.rating()));
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/review")
    public ResponseEntity<?> deleteReview(@Valid @RequestBody UserAnimeDeleteDTO request, JwtAuthenticationToken authentication) {
        String username = authentication.getToken().getSubject();
        SuccessAPIResponse<Void> response = userAnimeRelationshipService.deleteReview(
                new UserAnimeDeleteDTO(username, request.animeId()));
        return ResponseEntity.ok(response);
    }

}
