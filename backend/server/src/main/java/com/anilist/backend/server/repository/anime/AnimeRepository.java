package com.anilist.backend.server.repository.anime;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anilist.backend.server.models.anime.AnimeModel;

public interface AnimeRepository extends JpaRepository<AnimeModel, Long> {
    Optional<AnimeModel> findByMalId(Long malId);
    boolean existsByMalId(Long malId);
}
