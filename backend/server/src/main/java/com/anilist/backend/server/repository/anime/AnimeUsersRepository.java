package com.anilist.backend.server.repository.anime;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anilist.backend.server.models.anime.AnimeModel;
import com.anilist.backend.server.models.anime.AnimeUsersModel;
import com.anilist.backend.server.models.anime.EnumAnimeStatus;
import com.anilist.backend.server.models.user.UserModel;

public interface AnimeUsersRepository extends JpaRepository<AnimeUsersModel, Long> {
    List<AnimeUsersModel> findByUser(UserModel user);
    List<AnimeUsersModel> findByUserAndStatus(UserModel user, EnumAnimeStatus status);
    List<AnimeUsersModel> findByUserAndIsFavoriteTrue(UserModel user);
    Optional<AnimeUsersModel> findByUserAndAnime(UserModel user, AnimeModel anime);
    boolean existsByUserAndAnime(UserModel user, AnimeModel anime);
}
