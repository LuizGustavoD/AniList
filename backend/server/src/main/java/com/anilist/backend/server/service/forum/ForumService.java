package com.anilist.backend.server.service.forum;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anilist.backend.server.DTO.forum.ThreadCommentCreateDTO;
import com.anilist.backend.server.DTO.forum.ThreadCommentResponseDTO;
import com.anilist.backend.server.DTO.forum.ThreadCreateDTO;
import com.anilist.backend.server.DTO.forum.ThreadResponseDTO;
import com.anilist.backend.server.infra.http.success.SuccessAPIResponse;
import com.anilist.backend.server.models.anime.AnimeModel;
import com.anilist.backend.server.models.forum.ThreadCommentModel;
import com.anilist.backend.server.models.forum.ThreadModel;
import com.anilist.backend.server.models.user.UserModel;
import com.anilist.backend.server.repository.anime.AnimeRepository;
import com.anilist.backend.server.repository.forum.ThreadCommentRepository;
import com.anilist.backend.server.repository.forum.ThreadRepository;
import com.anilist.backend.server.repository.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ForumService {

    private final ThreadRepository threadRepository;
    private final ThreadCommentRepository threadCommentRepository;
    private final UserRepository userRepository;
    private final AnimeRepository animeRepository;

    @Transactional
    public SuccessAPIResponse<Void> createThread(String username, ThreadCreateDTO request) {
        UserModel author = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        AnimeModel anime = null;
        if (request.animeId() != null) {
            anime = animeRepository.findById(request.animeId())
                    .orElse(null);
        }

        ThreadModel thread = new ThreadModel();
        thread.setAuthor(author);
        thread.setAnime(anime);
        thread.setTitle(request.title());
        thread.setContent(request.content());
        
        threadRepository.save(thread);
        return new SuccessAPIResponse<>(null, "Tópico criado com sucesso");
    }

    @Transactional
    public SuccessAPIResponse<Void> addComment(String username, Long threadId, ThreadCommentCreateDTO request) {
        UserModel author = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ThreadModel thread = threadRepository.findById(threadId)
                .orElseThrow(() -> new RuntimeException("Thread not found"));

        ThreadCommentModel comment = new ThreadCommentModel();
        comment.setAuthor(author);
        comment.setThread(thread);
        comment.setContent(request.content());

        threadCommentRepository.save(comment);
        return new SuccessAPIResponse<>(null, "Comentário adicionado");
    }

    public Page<ThreadResponseDTO> getThreads(Long animeId, Pageable pageable) {
        Page<ThreadModel> threads;
        if (animeId != null) {
            threads = threadRepository.findByAnimeId(animeId, pageable);
        } else {
            threads = threadRepository.findAllByOrderByCreatedAtDesc(pageable);
        }

        return threads.map(this::mapToResponseDTO);
    }

    public ThreadResponseDTO getThread(Long threadId) {
        ThreadModel thread = threadRepository.findById(threadId)
                .orElseThrow(() -> new RuntimeException("Thread not found"));
        return mapToResponseDTO(thread);
    }

    private ThreadResponseDTO mapToResponseDTO(ThreadModel thread) {
        List<ThreadCommentResponseDTO> comments = threadCommentRepository.findByThreadIdOrderByCreatedAtAsc(thread.getId())
                .stream()
                .map(c -> new ThreadCommentResponseDTO(
                        c.getId(),
                        c.getAuthor().getUsername(),
                        c.getAuthor().getProfilePicture(),
                        c.getContent(),
                        c.getCreatedAt()
                ))
                .collect(Collectors.toList());

        return new ThreadResponseDTO(
                thread.getId(),
                thread.getAuthor().getUsername(),
                thread.getAuthor().getProfilePicture(),
                thread.getAnime() != null ? thread.getAnime().getId() : null,
                thread.getTitle(),
                thread.getContent(),
                thread.getCreatedAt(),
                comments
        );
    }
}
