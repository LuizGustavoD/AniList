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
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RolesAllowed("ROLE_ADMIN")
@RestController
@RequestMapping("/api/admin/anime")
public class AdminAnimeController {

    private final AdminAnimeService adminAnimeService;


    @PostMapping()
    public ResponseEntity<?> createAnime(@RequestBody AdminAnimeCreateDTO dto, jakarta.servlet.http.HttpServletRequest request) {
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


    @PutMapping("/{id}")
    public ResponseEntity<?> updateAnime(@PathVariable Long id, @RequestBody AdminAnimeUpdateDTO dto, jakarta.servlet.http.HttpServletRequest request) {
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


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteAnime(@PathVariable Long id, jakarta.servlet.http.HttpServletRequest request) {
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


    @GetMapping("/all")
    public ResponseEntity<?> getAllAnimes() {
        AnimeListResponseDTO list = adminAnimeService.getAllAnimes();
        return ResponseEntity.ok(new SuccessAPIResponse<>(list, "Listagem de animes"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAnimeById(@PathVariable Long id, jakarta.servlet.http.HttpServletRequest request) {
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

    @GetMapping("/mal/{malId}")
    public ResponseEntity<?> getAnimeByMalId(@PathVariable Long malId, jakarta.servlet.http.HttpServletRequest request) {
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
