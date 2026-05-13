package com.anilist.backend.server.service.anime;

import java.time.Instant;
import java.time.chrono.ChronoLocalDate;

import org.springframework.stereotype.Service;

import com.anilist.backend.server.DTO.anime.AnimeVerifyEpReleaseDateDTO;
import com.anilist.backend.server.repository.anime.AnimeRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class VerifyDateEpReleaseService {
    
    private final AnimeRepository animeRepository;

    public Instant verifyAnimeEpDateRelesae(AnimeVerifyEpReleaseDateDTO request){
        ChronoLocalDate now = ChronoLocalDate.from(Instant.now());

        var anime = animeRepository.findByTitle(request.animeTitle())
            .orElseThrow(() -> new RuntimeException("Anime not found"));

        if (anime.getReleaseDate().isAfter(now)) {
            return anime.getReleaseDate().atStartOfDay().toInstant(java.time.ZoneOffset.UTC);
        }
        
        return null;
    }

}
