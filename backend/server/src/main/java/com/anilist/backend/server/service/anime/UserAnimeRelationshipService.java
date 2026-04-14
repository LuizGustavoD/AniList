package com.anilist.backend.server.service.anime;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

import com.anilist.backend.server.DTO.anime.AnimeReviewDTO;
import com.anilist.backend.server.DTO.anime.UserAnimeAddDTO;
import com.anilist.backend.server.DTO.anime.UserAnimeDeleteDTO;
import com.anilist.backend.server.DTO.anime.UserAnimeFavoriteDTO;
import com.anilist.backend.server.DTO.anime.UserAnimeStatusUpdateDTO;
import com.anilist.backend.server.DTO.external.jikan.JikanAnimeSearchResponseDTO;
import com.anilist.backend.server.DTO.response.anime.AnimeResponseDTO;
import com.anilist.backend.server.DTO.response.anime.AnimeStatusUpdateResponseDTO;
import com.anilist.backend.server.DTO.response.anime.AnimeFavoriteResponseDTO;
import com.anilist.backend.server.DTO.response.anime.AnimeListResponseDTO;
import com.anilist.backend.server.infra.http.success.SuccessAPIResponse;
import com.anilist.backend.server.models.anime.AnimeModel;
import com.anilist.backend.server.models.anime.AnimeReviewsModel;
import com.anilist.backend.server.models.anime.AnimeUsersModel;
import com.anilist.backend.server.models.user.UserModel;
import com.anilist.backend.server.repository.anime.AnimeRepository;
import com.anilist.backend.server.repository.anime.AnimeReviewsRepository;
import com.anilist.backend.server.repository.anime.AnimeUsersRepository;
import com.anilist.backend.server.repository.user.UserRepository;
import com.anilist.backend.server.client.ExternalApiClient;


@RequiredArgsConstructor
@Service
public class UserAnimeRelationshipService {

    private final AnimeRepository animeRepository;
    private final AnimeUsersRepository animeUsersRepository;
    private final AnimeReviewsRepository animeReviewsRepository;
    private final UserRepository userRepository;

    private final ExternalApiClient externalApiClient;
    public AnimeListResponseDTO searchAnimeWithCache(String query) {
        List<AnimeModel> localResults = animeRepository.findAll().stream()
            .filter(a -> a.getTitle() != null && a.getTitle().toLowerCase().contains(query.toLowerCase()))
            .collect(Collectors.toList());
        List<AnimeModel> results = new ArrayList<>(localResults);
        if (results.isEmpty()) {
            JikanAnimeSearchResponseDTO apiResponse = externalApiClient.searchAnime(query, 1);
            if (apiResponse != null && apiResponse.data() != null) {
                for (var dto : apiResponse.data()) {
                    if (animeRepository.existsByMalId(dto.mal_id())) {
                        animeRepository.findByMalId(dto.mal_id()).ifPresent(results::add);
                        continue;
                    }
                    AnimeModel anime = new AnimeModel();
                    anime.setMalId(dto.mal_id());
                    anime.setTitle(dto.title());
                    anime.setDescription(null);
                    anime.setImageUrl(dto.images() != null && dto.images().jpg() != null ? dto.images().jpg().image_url() : null);
                    anime.setReleaseDate(null);
                    results.add(animeRepository.save(anime));
                }
            }
        }
        List<AnimeResponseDTO> dtos = results.stream()
            .map(a -> new AnimeResponseDTO(a.getId(), a.getTitle(), a.getImageUrl()))
            .toList();
        return new AnimeListResponseDTO(dtos);
    }

    public SuccessAPIResponse<Void> addUserAnimeRelationship(UserAnimeAddDTO request) {
        Long animeIdLong;
        try {
            animeIdLong = Long.parseLong(request.animeId());
        } catch (NumberFormatException e) {
            return new SuccessAPIResponse<>(null, "Invalid anime ID format");
        }

        var animeOpt = animeRepository.findByMalId(animeIdLong);
        if (animeOpt.isEmpty()) {
            return new SuccessAPIResponse<>(null, "Anime not found");
        }
        var anime = animeOpt.get();

        var userOpt = userRepository.findByUsername(request.username());
        if (userOpt.isEmpty()) {
            return new SuccessAPIResponse<>(null, "User not found");
        }
        var user = userOpt.get();

        if (animeUsersRepository.existsByUserAndAnime(user, anime)) {
            return new SuccessAPIResponse<>(null, "Anime already in user's list");
        }

        AnimeUsersModel userAnime = new AnimeUsersModel();
        userAnime.setUser(user);
        userAnime.setAnime(anime);
        userAnime.setStatus(request.status());
        animeUsersRepository.save(userAnime);

        return new SuccessAPIResponse<>(null, "Anime added to list successfully");
    }

    public SuccessAPIResponse<Void> deleteUserAnimeRelationship(UserAnimeDeleteDTO request) {
        Long animeIdLong;
        try {
            animeIdLong = Long.parseLong(request.animeId());
        } catch (NumberFormatException e) {
            return new SuccessAPIResponse<>(null, "Invalid anime ID format");
        }

        var animeOpt = animeRepository.findByMalId(animeIdLong);
        if (animeOpt.isEmpty()) {
            return new SuccessAPIResponse<>(null, "Anime not found");
        }
        var anime = animeOpt.get();

        var userOpt = userRepository.findByUsername(request.username());
        if (userOpt.isEmpty()) {
            return new SuccessAPIResponse<>(null, "User not found");
        }
        var user = userOpt.get();

        var userAnimeOpt = animeUsersRepository.findByUserAndAnime(user, anime);
        if (userAnimeOpt.isEmpty()) {
            return new SuccessAPIResponse<>(null, "User-anime relationship not found");
        }
        var userAnime = userAnimeOpt.get();

        var reviewOpt = animeReviewsRepository.findByUserAndAnime(user, anime);
        reviewOpt.ifPresent(animeReviewsRepository::delete);

        animeUsersRepository.delete(userAnime);

        return new SuccessAPIResponse<>(null, "User-anime relationship deleted successfully");
    }

    public AnimeStatusUpdateResponseDTO updateAnimeStatus(UserAnimeStatusUpdateDTO request) {
        Long animeIdLong;
        try {
            animeIdLong = Long.parseLong(request.animeId());
        } catch (NumberFormatException e) {
            return new AnimeStatusUpdateResponseDTO(null, null, (String) null, "Invalid anime ID format");
        }

        var animeOpt = animeRepository.findByMalId(animeIdLong);
        if (animeOpt.isEmpty()) {
            return new AnimeStatusUpdateResponseDTO(null, null, (String) null, "Anime not found");
        }

        var userOpt = userRepository.findByUsername(request.username());
        if (userOpt.isEmpty()) {
            return new AnimeStatusUpdateResponseDTO(null, null, (String) null, "User not found");
        }

        var userAnimeOpt = animeUsersRepository.findByUserAndAnime(userOpt.get(), animeOpt.get());
        if (userAnimeOpt.isEmpty()) {
            return new AnimeStatusUpdateResponseDTO(userOpt.get().getId(), animeOpt.get().getId(), (String) null, "Anime not in user's list");
        }

        var userAnime = userAnimeOpt.get();
        userAnime.setStatus(request.status());
        animeUsersRepository.save(userAnime);

        return new AnimeStatusUpdateResponseDTO(userOpt.get().getId(), animeOpt.get().getId(), request.status(), "Anime status updated to " + request.status());
    }

    public AnimeFavoriteResponseDTO toggleFavorite(UserAnimeFavoriteDTO request) {
        Long animeIdLong;
        try {
            animeIdLong = Long.parseLong(request.animeId());
        } catch (NumberFormatException e) {
            return new AnimeFavoriteResponseDTO(null, null, false);
        }

        var animeOpt = animeRepository.findByMalId(animeIdLong);
        if (animeOpt.isEmpty()) {
            return new AnimeFavoriteResponseDTO(null, null, false);
        }

        var userOpt = userRepository.findByUsername(request.username());
        if (userOpt.isEmpty()) {
            return new AnimeFavoriteResponseDTO(null, null, false);
        }

        var userAnimeOpt = animeUsersRepository.findByUserAndAnime(userOpt.get(), animeOpt.get());
        if (userAnimeOpt.isEmpty()) {
            return new AnimeFavoriteResponseDTO(null, animeOpt.get().getId(), false);
        }

        var userAnime = userAnimeOpt.get();
        userAnime.setFavorite(request.favorite());
        animeUsersRepository.save(userAnime);

        return new AnimeFavoriteResponseDTO(null, animeOpt.get().getId(), request.favorite());
    }

    public SuccessAPIResponse<Void> addReviewToAnime(AnimeReviewDTO request) {
        Long animeIdLong;
        try {
            animeIdLong = Long.parseLong(request.animeId());
        } catch (NumberFormatException e) {
            return new SuccessAPIResponse<>(null, "Invalid anime ID format");
        }

        var animeOpt = animeRepository.findByMalId(animeIdLong);
        if (animeOpt.isEmpty()) {
            return new SuccessAPIResponse<>(null, "Anime not found");
        }
        AnimeModel anime = animeOpt.get();

        var userOpt = userRepository.findByUsername(request.username());
        if (userOpt.isEmpty()) {
            return new SuccessAPIResponse<>(null, "User not found");
        }
        UserModel user = userOpt.get();

        if (!animeUsersRepository.existsByUserAndAnime(user, anime)) {
            return new SuccessAPIResponse<>(null, "Anime must be in user's list before reviewing");
        }

        var existingReview = animeReviewsRepository.findByUserAndAnime(user, anime);
        if (existingReview.isPresent()) {
            var review = existingReview.get();
            review.setRating(request.rating());
            review.setComment(request.reviewText());
            animeReviewsRepository.save(review);
            return new SuccessAPIResponse<>(null, "Review updated successfully");
        }

        AnimeReviewsModel review = new AnimeReviewsModel();
        review.setUser(user);
        review.setAnime(anime);
        review.setRating(request.rating());
        review.setComment(request.reviewText());
        animeReviewsRepository.save(review);

        return new SuccessAPIResponse<>(null, "Review added successfully");
    }

    public SuccessAPIResponse<Void> deleteReview(UserAnimeDeleteDTO request) {
        Long animeIdLong;
        try {
            animeIdLong = Long.parseLong(request.animeId());
        } catch (NumberFormatException e) {
            return new SuccessAPIResponse<>(null, "Invalid anime ID format");
        }

        var animeOpt = animeRepository.findByMalId(animeIdLong);
        if (animeOpt.isEmpty()) {
            return new SuccessAPIResponse<>(null, "Anime not found");
        }

        var userOpt = userRepository.findByUsername(request.username());
        if (userOpt.isEmpty()) {
            return new SuccessAPIResponse<>(null, "User not found");
        }

        var reviewOpt = animeReviewsRepository.findByUserAndAnime(userOpt.get(), animeOpt.get());
        if (reviewOpt.isEmpty()) {
            return new SuccessAPIResponse<>(null, "Review not found");
        }

        animeReviewsRepository.delete(reviewOpt.get());
        return new SuccessAPIResponse<>(null, "Review deleted successfully");
    }
}
