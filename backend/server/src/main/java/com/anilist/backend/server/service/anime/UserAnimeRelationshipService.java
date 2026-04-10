package com.anilist.backend.server.service.anime;

import org.springframework.stereotype.Service;

import com.anilist.backend.server.DTO.anime.AnimeReviewDTO;
import com.anilist.backend.server.DTO.anime.UserAnimeAddDTO;
import com.anilist.backend.server.DTO.anime.UserAnimeDeleteDTO;
import com.anilist.backend.server.DTO.anime.UserAnimeFavoriteDTO;
import com.anilist.backend.server.DTO.anime.UserAnimeStatusUpdateDTO;
import com.anilist.backend.server.models.anime.AnimeModel;
import com.anilist.backend.server.models.anime.AnimeReviewsModel;
import com.anilist.backend.server.models.anime.AnimeUsersModel;
import com.anilist.backend.server.models.user.UserModel;
import com.anilist.backend.server.repository.anime.AnimeRepository;
import com.anilist.backend.server.repository.anime.AnimeReviewsRepository;
import com.anilist.backend.server.repository.anime.AnimeUsersRepository;
import com.anilist.backend.server.repository.user.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserAnimeRelationshipService {

    private final AnimeRepository animeRepository;
    private final AnimeUsersRepository animeUsersRepository;
    private final AnimeReviewsRepository animeReviewsRepository;
    private final UserRepository userRepository;

    public String addUserAnimeRelationship(UserAnimeAddDTO request) {
        Long animeIdLong;
        try {
            animeIdLong = Long.parseLong(request.animeId());
        } catch (NumberFormatException e) {
            return "Invalid anime ID format";
        }

        var animeOpt = animeRepository.findByMalId(animeIdLong);
        if (animeOpt.isEmpty()) {
            return "Anime not found";
        }
        var anime = animeOpt.get();

        var userOpt = userRepository.findByUsername(request.username());
        if (userOpt.isEmpty()) {
            return "User not found";
        }
        var user = userOpt.get();

        if (animeUsersRepository.existsByUserAndAnime(user, anime)) {
            return "Anime already in user's list";
        }

        AnimeUsersModel userAnime = new AnimeUsersModel();
        userAnime.setUser(user);
        userAnime.setAnime(anime);
        userAnime.setStatus(request.status());
        animeUsersRepository.save(userAnime);

        return "Anime added to list successfully";
    }

    public String deleteUserAnimeRelationship(UserAnimeDeleteDTO request) {
        Long animeIdLong;
        try {
            animeIdLong = Long.parseLong(request.animeId());
        } catch (NumberFormatException e) {
            return "Invalid anime ID format";
        }

        var animeOpt = animeRepository.findByMalId(animeIdLong);
        if (animeOpt.isEmpty()) {
            return "Anime not found";
        }
        var anime = animeOpt.get();

        var userOpt = userRepository.findByUsername(request.username());
        if (userOpt.isEmpty()) {
            return "User not found";
        }
        var user = userOpt.get();

        var userAnimeOpt = animeUsersRepository.findByUserAndAnime(user, anime);
        if (userAnimeOpt.isEmpty()) {
            return "User-anime relationship not found";
        }
        var userAnime = userAnimeOpt.get();

        var reviewOpt = animeReviewsRepository.findByUserAndAnime(user, anime);
        reviewOpt.ifPresent(animeReviewsRepository::delete);

        animeUsersRepository.delete(userAnime);

        return "User-anime relationship deleted successfully";
    }

    public String updateAnimeStatus(UserAnimeStatusUpdateDTO request) {
        Long animeIdLong;
        try {
            animeIdLong = Long.parseLong(request.animeId());
        } catch (NumberFormatException e) {
            return "Invalid anime ID format";
        }

        var animeOpt = animeRepository.findByMalId(animeIdLong);
        if (animeOpt.isEmpty()) {
            return "Anime not found";
        }

        var userOpt = userRepository.findByUsername(request.username());
        if (userOpt.isEmpty()) {
            return "User not found";
        }

        var userAnimeOpt = animeUsersRepository.findByUserAndAnime(userOpt.get(), animeOpt.get());
        if (userAnimeOpt.isEmpty()) {
            return "Anime not in user's list";
        }

        var userAnime = userAnimeOpt.get();
        userAnime.setStatus(request.status());
        animeUsersRepository.save(userAnime);

        return "Anime status updated to " + request.status();
    }

    public String toggleFavorite(UserAnimeFavoriteDTO request) {
        Long animeIdLong;
        try {
            animeIdLong = Long.parseLong(request.animeId());
        } catch (NumberFormatException e) {
            return "Invalid anime ID format";
        }

        var animeOpt = animeRepository.findByMalId(animeIdLong);
        if (animeOpt.isEmpty()) {
            return "Anime not found";
        }

        var userOpt = userRepository.findByUsername(request.username());
        if (userOpt.isEmpty()) {
            return "User not found";
        }

        var userAnimeOpt = animeUsersRepository.findByUserAndAnime(userOpt.get(), animeOpt.get());
        if (userAnimeOpt.isEmpty()) {
            return "Anime not in user's list";
        }

        var userAnime = userAnimeOpt.get();
        userAnime.setFavorite(request.favorite());
        animeUsersRepository.save(userAnime);

        return request.favorite() ? "Anime added to favorites" : "Anime removed from favorites";
    }

    public String addReviewToAnime(AnimeReviewDTO request) {
        Long animeIdLong;
        try {
            animeIdLong = Long.parseLong(request.animeId());
        } catch (NumberFormatException e) {
            return "Invalid anime ID format";
        }

        var animeOpt = animeRepository.findByMalId(animeIdLong);
        if (animeOpt.isEmpty()) {
            return "Anime not found";
        }
        AnimeModel anime = animeOpt.get();

        var userOpt = userRepository.findByUsername(request.username());
        if (userOpt.isEmpty()) {
            return "User not found";
        }
        UserModel user = userOpt.get();

        if (!animeUsersRepository.existsByUserAndAnime(user, anime)) {
            return "Anime must be in user's list before reviewing";
        }

        var existingReview = animeReviewsRepository.findByUserAndAnime(user, anime);
        if (existingReview.isPresent()) {
            var review = existingReview.get();
            review.setRating(request.rating());
            review.setComment(request.reviewText());
            animeReviewsRepository.save(review);
            return "Review updated successfully";
        }

        AnimeReviewsModel review = new AnimeReviewsModel();
        review.setUser(user);
        review.setAnime(anime);
        review.setRating(request.rating());
        review.setComment(request.reviewText());
        animeReviewsRepository.save(review);

        return "Review added successfully";
    }

    public String deleteReview(UserAnimeDeleteDTO request) {
        Long animeIdLong;
        try {
            animeIdLong = Long.parseLong(request.animeId());
        } catch (NumberFormatException e) {
            return "Invalid anime ID format";
        }

        var animeOpt = animeRepository.findByMalId(animeIdLong);
        if (animeOpt.isEmpty()) {
            return "Anime not found";
        }

        var userOpt = userRepository.findByUsername(request.username());
        if (userOpt.isEmpty()) {
            return "User not found";
        }

        var reviewOpt = animeReviewsRepository.findByUserAndAnime(userOpt.get(), animeOpt.get());
        if (reviewOpt.isEmpty()) {
            return "Review not found";
        }

        animeReviewsRepository.delete(reviewOpt.get());
        return "Review deleted successfully";
    }
}
