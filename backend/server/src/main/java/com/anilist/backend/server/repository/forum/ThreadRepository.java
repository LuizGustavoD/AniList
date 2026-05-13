package com.anilist.backend.server.repository.forum;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.anilist.backend.server.models.forum.ThreadModel;

public interface ThreadRepository extends JpaRepository<ThreadModel, Long> {
    Page<ThreadModel> findByAnimeId(Long animeId, Pageable pageable);
    Page<ThreadModel> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
