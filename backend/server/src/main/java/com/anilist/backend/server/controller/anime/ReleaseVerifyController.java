package com.anilist.backend.server.controller.anime;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anilist.backend.server.repository.anime.AnimeRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/anime/release-verify")
public class ReleaseVerifyController {

    private final AnimeRepository animeRepository;

    @io.github.resilience4j.ratelimiter.annotation.RateLimiter(name = "animeControllerLimiter")
    @GetMapping()
    public ResponseEntity<?> verifyDateRelease() {
        try {
            // TODO: implement logic
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
