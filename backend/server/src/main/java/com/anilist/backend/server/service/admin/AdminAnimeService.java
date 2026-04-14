package com.anilist.backend.server.service.admin;

import org.springframework.stereotype.Service;

import com.anilist.backend.server.repository.anime.AnimeRepository;
import com.anilist.backend.server.repository.anime.AnimeUsersRepository;
import com.anilist.backend.server.repository.anime.AnimeReviewsRepository;
import com.anilist.backend.server.models.anime.AnimeModel;
import com.anilist.backend.server.DTO.admin.anime.AdminAnimeCreateDTO;
import com.anilist.backend.server.DTO.admin.anime.AdminAnimeUpdateDTO;
import com.anilist.backend.server.DTO.response.anime.AnimeResponseDTO;
import com.anilist.backend.server.DTO.response.anime.AnimeListResponseDTO;
import com.anilist.backend.server.infra.http.success.SuccessAPIResponse;
import com.anilist.backend.server.models.anime.AnimeUsersModel;
import com.anilist.backend.server.models.anime.EnumAnimeStatus;
import com.anilist.backend.server.models.anime.AnimeReviewsModel;
import com.anilist.backend.server.models.user.UserModel;
import com.anilist.backend.server.service.exceptions.custom.AnimeNotFoundException;
import java.util.List;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AdminAnimeService {
    
    private final AnimeRepository animeRepository;
    private final AnimeUsersRepository animeUsersRepository;
    private final AnimeReviewsRepository animeReviewsRepository;


    public AnimeResponseDTO createAnime(AdminAnimeCreateDTO dto) {
        AnimeModel anime = new AnimeModel();
        anime.setMalId(dto.malId());
        anime.setTitle(dto.title());
        anime.setDescription(dto.description());
        anime.setImageUrl(dto.imageUrl());
        anime.setReleaseDate(dto.releaseDate());
        AnimeModel saved = animeRepository.save(anime);
        return new AnimeResponseDTO(saved.getId(), saved.getTitle(), saved.getImageUrl());
    }


    public AnimeListResponseDTO getAllAnimes() {
        List<AnimeResponseDTO> dtos = animeRepository.findAll().stream()
            .map(a -> new AnimeResponseDTO(a.getId(), a.getTitle(), a.getImageUrl()))
            .toList();
        return new AnimeListResponseDTO(dtos);
    }


    public AnimeResponseDTO getAnimeById(Long id) {
        AnimeModel anime = animeRepository.findById(id)
                .orElseThrow(() -> new AnimeNotFoundException("Anime not found with id: " + id));
        return new AnimeResponseDTO(anime.getId(), anime.getTitle(), anime.getImageUrl());
    }


    public AnimeResponseDTO getAnimeByMalId(Long malId) {
        AnimeModel anime = animeRepository.findByMalId(malId)
                .orElseThrow(() -> new AnimeNotFoundException("Anime not found with malId: " + malId));
        return new AnimeResponseDTO(anime.getId(), anime.getTitle(), anime.getImageUrl());
    }


    public AnimeResponseDTO updateAnime(Long id, AdminAnimeUpdateDTO dto) {
        AnimeModel anime = animeRepository.findById(id)
                .orElseThrow(() -> new AnimeNotFoundException("Anime not found with id: " + id));
        anime.setTitle(dto.title());
        anime.setDescription(dto.description());
        anime.setImageUrl(dto.imageUrl());
        anime.setReleaseDate(dto.releaseDate());
        anime.setMalId(dto.malId());
        AnimeModel updated = animeRepository.save(anime);
        return new AnimeResponseDTO(updated.getId(), updated.getTitle(), updated.getImageUrl());
    }


    public SuccessAPIResponse<Void> deleteAnimeById(Long id) {
        AnimeModel anime = animeRepository.findById(id)
                .orElseThrow(() -> new AnimeNotFoundException("Anime not found with id: " + id));
        animeRepository.delete(anime);
        return new SuccessAPIResponse<>(null, "Anime deleted successfully");
    }

    public AnimeUsersModel createUserAnime(AnimeUsersModel userAnime) {
        return animeUsersRepository.save(userAnime);
    }

    public List<AnimeUsersModel> getAllUserAnimes() {
        return animeUsersRepository.findAll();
    }

    public List<AnimeUsersModel> getUserAnimesByUser(UserModel user) {
        return animeUsersRepository.findByUser(user);
    }

    public List<AnimeUsersModel> getUserAnimesByUserAndStatus(UserModel user, EnumAnimeStatus status) {
        return animeUsersRepository.findByUserAndStatus(user, status);
    }

    public List<AnimeUsersModel> getUserFavorites(UserModel user) {
        return animeUsersRepository.findByUserAndIsFavoriteTrue(user);
    }

    public void deleteUserAnime(Long id) {
        animeUsersRepository.deleteById(id);
    }

    public AnimeReviewsModel createReview(AnimeReviewsModel review) {
        return animeReviewsRepository.save(review);
    }

    public List<AnimeReviewsModel> getAllReviews() {
        return animeReviewsRepository.findAll();
    }

    public List<AnimeReviewsModel> getReviewsByAnime(AnimeModel anime) {
        return animeReviewsRepository.findByAnime(anime);
    }

    public List<AnimeReviewsModel> getReviewsByUser(UserModel user) {
        return animeReviewsRepository.findByUser(user);
    }

    public void deleteReview(Long id) {
        animeReviewsRepository.deleteById(id);
    }
}
