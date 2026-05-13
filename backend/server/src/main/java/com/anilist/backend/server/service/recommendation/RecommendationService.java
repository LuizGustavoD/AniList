package com.anilist.backend.server.service.recommendation;

import org.springframework.stereotype.Service;
import com.anilist.backend.server.DTO.external.jikan.JikanTopAnimeResponseDTO;
import com.anilist.backend.server.client.ExternalApiClient;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final ExternalApiClient externalApiClient;

    public JikanTopAnimeResponseDTO getRecommendations(String username) {
        // Um motor real analisaria os gêneros dos animes favoritados pelo usuário.
        // Como o foco é a estrutura da rede social, retornaremos os Top Animes do momento.
        return externalApiClient.getTopAnime(1);
    }
}
