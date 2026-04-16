package com.anilist.backend.server.controller.anime;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.anilist.backend.server.DTO.anime.AnimeReviewDTO;
import com.anilist.backend.server.DTO.anime.UserAnimeAddDTO;
import com.anilist.backend.server.DTO.anime.UserAnimeDeleteDTO;
import com.anilist.backend.server.DTO.anime.UserAnimeFavoriteDTO;
import com.anilist.backend.server.DTO.anime.UserAnimeStatusUpdateDTO;
import com.anilist.backend.server.service.anime.UserAnimeRelationshipService;


import com.anilist.backend.server.DTO.response.anime.AnimeListResponseDTO;
import com.anilist.backend.server.DTO.response.anime.AnimeFavoriteResponseDTO;
import com.anilist.backend.server.DTO.response.anime.AnimeStatusUpdateResponseDTO;
import com.anilist.backend.server.infra.http.success.SuccessAPIResponse;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@Tag(name = "Anime", description = "Operações relacionadas à lista de animes do usuário")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/anime")
public class AnimeController {

    private final UserAnimeRelationshipService userAnimeRelationshipService;


    @Operation(summary = "Buscar animes", description = "Busca animes pelo nome.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = SuccessAPIResponse.class)))
    })
    @io.github.resilience4j.ratelimiter.annotation.RateLimiter(name = "animeControllerLimiter")
    @GetMapping("/search")
    public ResponseEntity<?> searchAnime(
            @Parameter(description = "Termo de busca do anime") @RequestParam String query,
            JwtAuthenticationToken authentication) {
        AnimeListResponseDTO results = userAnimeRelationshipService.searchAnimeWithCache(query);
        return ResponseEntity.ok(new SuccessAPIResponse<>(results, "Busca de animes"));
    }


    @Operation(summary = "Adicionar anime à lista", description = "Adiciona um anime à lista do usuário.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Anime adicionado com sucesso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = SuccessAPIResponse.class)))
    })
    @io.github.resilience4j.ratelimiter.annotation.RateLimiter(name = "animeControllerLimiter")
    @PostMapping("/add")
    public ResponseEntity<?> addAnimeToList(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Dados para adicionar anime", required = true,
                content = @Content(schema = @Schema(implementation = UserAnimeAddDTO.class)))
            @Valid @RequestBody UserAnimeAddDTO request,
            JwtAuthenticationToken authentication) {
        String username = authentication.getToken().getSubject();
        SuccessAPIResponse<Void> response = userAnimeRelationshipService.addUserAnimeRelationship(
                new UserAnimeAddDTO(username, request.animeId(), request.status()));
        return ResponseEntity.ok(response);
    }


    @Operation(summary = "Remover anime da lista", description = "Remove um anime da lista do usuário.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Anime removido com sucesso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = SuccessAPIResponse.class)))
    })
    @io.github.resilience4j.ratelimiter.annotation.RateLimiter(name = "animeControllerLimiter")
    @DeleteMapping("/remove")
    public ResponseEntity<?> removeAnimeFromList(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Dados para remover anime", required = true,
                content = @Content(schema = @Schema(implementation = UserAnimeDeleteDTO.class)))
            @Valid @RequestBody UserAnimeDeleteDTO request,
            JwtAuthenticationToken authentication) {
        String username = authentication.getToken().getSubject();
        SuccessAPIResponse<Void> response = userAnimeRelationshipService.deleteUserAnimeRelationship(
                new UserAnimeDeleteDTO(username, request.animeId()));
        return ResponseEntity.ok(response);
    }


    @Operation(summary = "Atualizar status do anime", description = "Atualiza o status do anime na lista do usuário.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Status atualizado com sucesso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = SuccessAPIResponse.class)))
    })
    @io.github.resilience4j.ratelimiter.annotation.RateLimiter(name = "animeControllerLimiter")
    @PutMapping("/update/status")
    public ResponseEntity<?> updateAnimeStatus(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Dados para atualizar status", required = true,
                content = @Content(schema = @Schema(implementation = UserAnimeStatusUpdateDTO.class)))
            @Valid @RequestBody UserAnimeStatusUpdateDTO request,
            JwtAuthenticationToken authentication) {
        String username = authentication.getToken().getSubject();
        AnimeStatusUpdateResponseDTO response = userAnimeRelationshipService.updateAnimeStatus(
                new UserAnimeStatusUpdateDTO(username, request.animeId(), request.status()));
        return ResponseEntity.ok(new SuccessAPIResponse<>(response, response.message()));
    }


    @Operation(summary = "Favoritar/desfavoritar anime", description = "Marca ou desmarca um anime como favorito.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Favorito atualizado/removido",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = SuccessAPIResponse.class)))
    })
    @io.github.resilience4j.ratelimiter.annotation.RateLimiter(name = "animeControllerLimiter")
    @PutMapping("/update/favorite")
    public ResponseEntity<?> toggleFavorite(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Dados para favoritar/desfavoritar", required = true,
                content = @Content(schema = @Schema(implementation = UserAnimeFavoriteDTO.class)))
            @Valid @RequestBody UserAnimeFavoriteDTO request,
            JwtAuthenticationToken authentication) {
        String username = authentication.getToken().getSubject();
        AnimeFavoriteResponseDTO response = userAnimeRelationshipService.toggleFavorite(
                new UserAnimeFavoriteDTO(username, request.animeId(), request.favorite()));
        return ResponseEntity.ok(new SuccessAPIResponse<>(response, response.isFavorite() ? "Favorito atualizado" : "Favorito removido"));
    }


    @Operation(summary = "Adicionar review ao anime", description = "Adiciona uma review ao anime na lista do usuário.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Review adicionada com sucesso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = SuccessAPIResponse.class)))
    })
    @io.github.resilience4j.ratelimiter.annotation.RateLimiter(name = "animeControllerLimiter")
    @PostMapping("/review")
    public ResponseEntity<?> addReview(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Dados da review", required = true,
                content = @Content(schema = @Schema(implementation = AnimeReviewDTO.class)))
            @Valid @RequestBody AnimeReviewDTO request,
            JwtAuthenticationToken authentication) {
        String username = authentication.getToken().getSubject();
        SuccessAPIResponse<Void> response = userAnimeRelationshipService.addReviewToAnime(
                new AnimeReviewDTO(username, request.animeId(), request.reviewText(), request.rating()));
        return ResponseEntity.ok(response);
    }


    @Operation(summary = "Remover review do anime", description = "Remove uma review do anime na lista do usuário.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Review removida com sucesso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = SuccessAPIResponse.class)))
    })
    @io.github.resilience4j.ratelimiter.annotation.RateLimiter(name = "animeControllerLimiter")
    @DeleteMapping("/review")
    public ResponseEntity<?> deleteReview(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Dados para remover review", required = true,
                content = @Content(schema = @Schema(implementation = UserAnimeDeleteDTO.class)))
            @Valid @RequestBody UserAnimeDeleteDTO request,
            JwtAuthenticationToken authentication) {
        String username = authentication.getToken().getSubject();
        SuccessAPIResponse<Void> response = userAnimeRelationshipService.deleteReview(
                new UserAnimeDeleteDTO(username, request.animeId()));
        return ResponseEntity.ok(response);
    }

}
