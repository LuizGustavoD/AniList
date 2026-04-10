package com.anilist.backend.server.repository.anime;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anilist.backend.server.models.anime.AnimeModel;
import com.anilist.backend.server.models.anime.AnimeReviewsModel;
import com.anilist.backend.server.models.user.UserModel;

public interface AnimeReviewsRepository extends JpaRepository<AnimeReviewsModel, Long> {
    List<AnimeReviewsModel> findByAnime(AnimeModel anime);
    List<AnimeReviewsModel> findByUser(UserModel user);
    Optional<AnimeReviewsModel> findByUserAndAnime(UserModel user, AnimeModel anime);
    boolean existsByUserAndAnime(UserModel user, AnimeModel anime);
}
