package com.anilist.backend.server.DTO.response.anime;

import java.util.UUID;
import com.anilist.backend.server.models.anime.EnumAnimeStatus;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resposta de atualização de status do anime")
public record AnimeStatusUpdateResponseDTO(
    @Schema(description = "ID do usuário", example = "uuid-1234") UUID userId,
    @Schema(description = "ID do anime", example = "10") Long animeId,
    @Schema(description = "Status do anime", example = "WATCHING") String status,
    @Schema(description = "Mensagem de status", example = "Status atualizado com sucesso.") String message
) {
    public AnimeStatusUpdateResponseDTO(UUID userId, Long animeId, EnumAnimeStatus status, String message) {
        this(userId, animeId, status.name(), message);
    }
    public AnimeStatusUpdateResponseDTO(UUID userId, Long animeId, EnumAnimeStatus status) {
        this(userId, animeId, status.name(), "Anime status updated successfully.");
    }
}
