package com.anilist.backend.server.controller.admin;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.anilist.backend.server.DTO.admin.anime.AdminAnimeCreateDTO;
import com.anilist.backend.server.DTO.admin.anime.AdminAnimeUpdateDTO;

import com.anilist.backend.server.DTO.response.anime.AnimeResponseDTO;
import com.anilist.backend.server.DTO.response.anime.AnimeListResponseDTO;
import com.anilist.backend.server.infra.http.success.SuccessAPIResponse;
import com.anilist.backend.server.controller.exceptions.StandartError;
import com.anilist.backend.server.service.admin.AdminAnimeService;
import com.anilist.backend.server.service.exceptions.custom.AnimeNotFoundException;

import jakarta.annotation.security.RolesAllowed;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;


@Tag(name = "Admin Anime", description = "Operações administrativas de anime")
@RequiredArgsConstructor
@RolesAllowed("ROLE_ADMIN")
@RestController
@RequestMapping("/api/admin/anime")
public class AdminAnimeController {

    private final AdminAnimeService adminAnimeService;



    @Operation(summary = "Criar anime", description = "Cria um novo anime administrativo.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Anime criado com sucesso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = SuccessAPIResponse.class))),
        @ApiResponse(responseCode = "400", description = "Erro de validação ou negócio",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandartError.class))),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandartError.class)))
    })
    
    @RateLimiter(name = "adminAnimeCreateLimiter")
    @PostMapping()
    public ResponseEntity<?> createAnime(
            @RequestBody AdminAnimeCreateDTO dto,
            jakarta.servlet.http.HttpServletRequest request) {
        try {
            AnimeResponseDTO createdAnime = adminAnimeService.createAnime(dto);
            return ResponseEntity.ok(new SuccessAPIResponse<>(createdAnime, "Anime created successfully"));
        } catch (RuntimeException e) {
            StandartError error = new StandartError();
            error.setTimestamp(java.time.Instant.now());
            error.setStatus(400);
            error.setError("Bad Request");
            error.setMessage("Failed to create anime");
            error.setPath(request.getRequestURI());
            error.setErrors(List.of(e.getMessage()));
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            StandartError error = new StandartError();
            error.setTimestamp(java.time.Instant.now());
            error.setStatus(500);
            error.setError("Internal Server Error");
            error.setMessage("Erro interno ao criar anime");
            error.setPath(request.getRequestURI());
            error.setErrors(List.of(e.getMessage()));
            return ResponseEntity.status(500).body(error);
        }
    }



    @Operation(summary = "Atualizar anime", description = "Atualiza os dados de um anime administrativo.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Anime atualizado com sucesso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = SuccessAPIResponse.class))),
        @ApiResponse(responseCode = "400", description = "Erro de validação ou negócio",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandartError.class))),
        @ApiResponse(responseCode = "404", description = "Anime não encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandartError.class))),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandartError.class)))
    })
    @io.github.resilience4j.ratelimiter.annotation.RateLimiter(name = "adminAnimeCreateLimiter")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateAnime(
            @Schema(description = "ID do anime") @PathVariable Long id,
            @RequestBody AdminAnimeUpdateDTO dto,
            jakarta.servlet.http.HttpServletRequest request) {
        try {
            AnimeResponseDTO updatedAnime = adminAnimeService.updateAnime(id, dto);
            return ResponseEntity.ok(new SuccessAPIResponse<>(updatedAnime, "Anime updated successfully"));
        } catch (AnimeNotFoundException e) {
            StandartError error = new StandartError();
            error.setTimestamp(java.time.Instant.now());
            error.setStatus(404);
            error.setError("Not Found");
            error.setMessage("Anime not found");
            error.setPath(request.getRequestURI());
            error.setErrors(List.of(e.getMessage()));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (RuntimeException e) {
            StandartError error = new StandartError();
            error.setTimestamp(java.time.Instant.now());
            error.setStatus(400);
            error.setError("Bad Request");
            error.setMessage("Failed to update anime");
            error.setPath(request.getRequestURI());
            error.setErrors(List.of(e.getMessage()));
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            StandartError error = new StandartError();
            error.setTimestamp(java.time.Instant.now());
            error.setStatus(500);
            error.setError("Internal Server Error");
            error.setMessage("Erro interno ao atualizar anime");
            error.setPath(request.getRequestURI());
            error.setErrors(List.of(e.getMessage()));
            return ResponseEntity.status(500).body(error);
        }
    }



    @Operation(summary = "Deletar anime", description = "Remove um anime administrativo pelo ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Anime removido com sucesso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = SuccessAPIResponse.class))),
        @ApiResponse(responseCode = "400", description = "Erro de validação ou negócio",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandartError.class))),
        @ApiResponse(responseCode = "404", description = "Anime não encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandartError.class))),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandartError.class)))
    })
    @io.github.resilience4j.ratelimiter.annotation.RateLimiter(name = "adminAnimeCreateLimiter")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteAnime(
            @Schema(description = "ID do anime") @PathVariable Long id,
            jakarta.servlet.http.HttpServletRequest request) {
        try {
            SuccessAPIResponse<Void> response = adminAnimeService.deleteAnimeById(id);
            return ResponseEntity.ok(response);
        } catch (AnimeNotFoundException e) {
            StandartError error = new StandartError();
            error.setTimestamp(java.time.Instant.now());
            error.setStatus(404);
            error.setError("Not Found");
            error.setMessage("Anime not found");
            error.setPath(request.getRequestURI());
            error.setErrors(List.of(e.getMessage()));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (RuntimeException e) {
            StandartError error = new StandartError();
            error.setTimestamp(java.time.Instant.now());
            error.setStatus(400);
            error.setError("Bad Request");
            error.setMessage("Failed to delete anime");
            error.setPath(request.getRequestURI());
            error.setErrors(List.of(e.getMessage()));
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            StandartError error = new StandartError();
            error.setTimestamp(java.time.Instant.now());
            error.setStatus(500);
            error.setError("Internal Server Error");
            error.setMessage("Erro interno ao deletar anime");
            error.setPath(request.getRequestURI());
            error.setErrors(List.of(e.getMessage()));
            return ResponseEntity.status(500).body(error);
        }
    }



    @Operation(summary = "Listar todos os animes", description = "Retorna a lista de todos os animes administrativos.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = SuccessAPIResponse.class)))
    })
    @io.github.resilience4j.ratelimiter.annotation.RateLimiter(name = "adminAnimeCreateLimiter")
    @GetMapping("/all")
    public ResponseEntity<?> getAllAnimes() {
        AnimeListResponseDTO list = adminAnimeService.getAllAnimes();
        return ResponseEntity.ok(new SuccessAPIResponse<>(list, "Listagem de animes"));
    }


    @Operation(summary = "Buscar anime por ID", description = "Retorna um anime administrativo pelo ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Anime encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = SuccessAPIResponse.class))),
        @ApiResponse(responseCode = "404", description = "Anime não encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandartError.class))),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandartError.class)))
    })
    @io.github.resilience4j.ratelimiter.annotation.RateLimiter(name = "adminAnimeCreateLimiter")
    @GetMapping("/{id}")
    public ResponseEntity<?> getAnimeById(
            @Schema(description = "ID do anime") @PathVariable Long id,
            jakarta.servlet.http.HttpServletRequest request) {
        try {
            AnimeResponseDTO anime = adminAnimeService.getAnimeById(id);
            return ResponseEntity.ok(new SuccessAPIResponse<>(anime, "Anime encontrado"));
        } catch (AnimeNotFoundException e) {
            StandartError error = new StandartError();
            error.setTimestamp(java.time.Instant.now());
            error.setStatus(404);
            error.setError("Not Found");
            error.setMessage("Anime não encontrado");
            error.setPath(request.getRequestURI());
            error.setErrors(List.of(e.getMessage()));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (Exception e) {
            StandartError error = new StandartError();
            error.setTimestamp(java.time.Instant.now());
            error.setStatus(500);
            error.setError("Internal Server Error");
            error.setMessage("Erro interno ao buscar anime");
            error.setPath(request.getRequestURI());
            error.setErrors(List.of(e.getMessage()));
            return ResponseEntity.status(500).body(error);
        }
    }


    @Operation(summary = "Buscar anime por MAL ID", description = "Retorna um anime administrativo pelo ID do MyAnimeList.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Anime encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = SuccessAPIResponse.class))),
        @ApiResponse(responseCode = "404", description = "Anime não encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandartError.class))),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandartError.class)))
    })
    @io.github.resilience4j.ratelimiter.annotation.RateLimiter(name = "adminAnimeCreateLimiter")
    @GetMapping("/mal/{malId}")
    public ResponseEntity<?> getAnimeByMalId(
            @Schema(description = "ID do MAL") @PathVariable Long malId,
            jakarta.servlet.http.HttpServletRequest request) {
        try {
            AnimeResponseDTO anime = adminAnimeService.getAnimeByMalId(malId);
            return ResponseEntity.ok(new SuccessAPIResponse<>(anime, "Anime encontrado"));
        } catch (AnimeNotFoundException e) {
            StandartError error = new StandartError();
            error.setTimestamp(java.time.Instant.now());
            error.setStatus(404);
            error.setError("Not Found");
            error.setMessage("Anime não encontrado");
            error.setPath(request.getRequestURI());
            error.setErrors(List.of(e.getMessage()));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (Exception e) {
            StandartError error = new StandartError();
            error.setTimestamp(java.time.Instant.now());
            error.setStatus(500);
            error.setError("Internal Server Error");
            error.setMessage("Erro interno ao buscar anime");
            error.setPath(request.getRequestURI());
            error.setErrors(List.of(e.getMessage()));
            return ResponseEntity.status(500).body(error);
        }
    }
}
