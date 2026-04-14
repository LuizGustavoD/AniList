package com.anilist.backend.server.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import lombok.RequiredArgsConstructor;
import com.anilist.backend.server.DTO.external.jikan.*;

@RequiredArgsConstructor
@Component
public class ExternalApiClient {

    private static final String BASE_URL = "https://api.jikan.moe/v4";
    private final RestTemplate restTemplate;

    public JikanAnimeSearchResponseDTO searchAnime(String query, Integer page) {
        String url = UriComponentsBuilder.fromUriString(BASE_URL + "/anime")
                .queryParam("q", query)
                .queryParam("page", page != null ? page : 1)
                .toUriString();
        return restTemplate.getForObject(url, JikanAnimeSearchResponseDTO.class);
    }

    public JikanAnimeDetailsResponseDTO getAnimeDetails(long malId) {
        String url = BASE_URL + "/anime/" + malId;
        return restTemplate.getForObject(url, JikanAnimeDetailsResponseDTO.class);
    }

    public JikanAnimeEpisodesResponseDTO getAnimeEpisodes(long malId, Integer page) {
        String url = UriComponentsBuilder.fromUriString(BASE_URL + "/anime/" + malId + "/episodes")
                .queryParam("page", page != null ? page : 1)
                .toUriString();
        return restTemplate.getForObject(url, JikanAnimeEpisodesResponseDTO.class);
    }

    public JikanAnimeCharactersResponseDTO getAnimeCharacters(long malId) {
        String url = BASE_URL + "/anime/" + malId + "/characters";
        return restTemplate.getForObject(url, JikanAnimeCharactersResponseDTO.class);
    }

    public JikanAnimePicturesResponseDTO getAnimePictures(long malId) {
        String url = BASE_URL + "/anime/" + malId + "/pictures";
        return restTemplate.getForObject(url, JikanAnimePicturesResponseDTO.class);
    }

    public JikanTopAnimeResponseDTO getTopAnime(Integer page) {
        String url = UriComponentsBuilder.fromUriString(BASE_URL + "/top/anime")
                .queryParam("page", page != null ? page : 1)
                .toUriString();
        return restTemplate.getForObject(url, JikanTopAnimeResponseDTO.class);
    }

    public JikanSeasonAnimeResponseDTO getCurrentSeasonAnime(Integer page) {
        String url = UriComponentsBuilder.fromUriString(BASE_URL + "/seasons/now")
                .queryParam("page", page != null ? page : 1)
                .toUriString();
        return restTemplate.getForObject(url, JikanSeasonAnimeResponseDTO.class);
    }

    public JikanSeasonAnimeResponseDTO getSeasonAnime(int year, String season, Integer page) {
        String url = UriComponentsBuilder.fromUriString(BASE_URL + "/seasons/" + year + "/" + season)
                .queryParam("page", page != null ? page : 1)
                .toUriString();
        return restTemplate.getForObject(url, JikanSeasonAnimeResponseDTO.class);
    }

}
