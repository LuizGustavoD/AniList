package com.anilist.backend.server.repository.forum;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.anilist.backend.server.models.forum.ThreadCommentModel;

public interface ThreadCommentRepository extends JpaRepository<ThreadCommentModel, Long> {
    List<ThreadCommentModel> findByThreadIdOrderByCreatedAtAsc(Long threadId);
}
