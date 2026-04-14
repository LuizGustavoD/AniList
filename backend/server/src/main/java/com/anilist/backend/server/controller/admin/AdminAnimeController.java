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
import com.anilist.backend.server.infra.http.error.ErrorAPIResponse;
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
    public ResponseEntity<?> createAnime(@RequestBody AdminAnimeCreateDTO dto) {
        try {
            AnimeResponseDTO createdAnime = adminAnimeService.createAnime(dto);
            return ResponseEntity.ok(new SuccessAPIResponse<>(createdAnime, "Anime created successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ErrorAPIResponse<>(List.of(e.getMessage()), "Failed to create anime"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ErrorAPIResponse<>(List.of(e.getMessage()), "Erro interno ao criar anime"));
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateAnime(@PathVariable Long id, @RequestBody AdminAnimeUpdateDTO dto) {
        try {
            AnimeResponseDTO updatedAnime = adminAnimeService.updateAnime(id, dto);
            return ResponseEntity.ok(new SuccessAPIResponse<>(updatedAnime, "Anime updated successfully"));
        } catch (AnimeNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorAPIResponse<>(List.of(e.getMessage()), "Anime not found"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ErrorAPIResponse<>(List.of(e.getMessage()), "Failed to update anime"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ErrorAPIResponse<>(List.of(e.getMessage()), "Erro interno ao atualizar anime"));
        }
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteAnime(@PathVariable Long id) {
        try {
            SuccessAPIResponse<Void> response = adminAnimeService.deleteAnimeById(id);
            return ResponseEntity.ok(response);
        } catch (AnimeNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorAPIResponse<>(List.of(e.getMessage()), "Anime not found"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ErrorAPIResponse<>(List.of(e.getMessage()), "Failed to delete anime"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ErrorAPIResponse<>(List.of(e.getMessage()), "Erro interno ao deletar anime"));
        }
    }


    @GetMapping("/all")
    public ResponseEntity<?> getAllAnimes() {
        AnimeListResponseDTO list = adminAnimeService.getAllAnimes();
        return ResponseEntity.ok(new SuccessAPIResponse<>(list, "Listagem de animes"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAnimeById(@PathVariable Long id) {
        try {
            AnimeResponseDTO anime = adminAnimeService.getAnimeById(id);
            return ResponseEntity.ok(new SuccessAPIResponse<>(anime, "Anime encontrado"));
        } catch (AnimeNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorAPIResponse<>(List.of(e.getMessage()), "Anime não encontrado"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ErrorAPIResponse<>(List.of(e.getMessage()), "Erro interno ao buscar anime"));
        }
    }

    @GetMapping("/mal/{malId}")
    public ResponseEntity<?> getAnimeByMalId(@PathVariable Long malId) {
        try {
            AnimeResponseDTO anime = adminAnimeService.getAnimeByMalId(malId);
            return ResponseEntity.ok(new SuccessAPIResponse<>(anime, "Anime encontrado"));
        } catch (AnimeNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorAPIResponse<>(List.of(e.getMessage()), "Anime não encontrado"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ErrorAPIResponse<>(List.of(e.getMessage()), "Erro interno ao buscar anime"));
        }
    }
}
