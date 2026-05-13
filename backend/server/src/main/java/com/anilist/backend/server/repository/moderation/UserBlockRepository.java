package com.anilist.backend.server.repository.moderation;

import org.springframework.data.jpa.repository.JpaRepository;
import com.anilist.backend.server.models.moderation.UserBlockModel;
import com.anilist.backend.server.models.user.UserModel;

public interface UserBlockRepository extends JpaRepository<UserBlockModel, Long> {
    boolean existsByBlockerAndBlocked(UserModel blocker, UserModel blocked);
    void deleteByBlockerAndBlocked(UserModel blocker, UserModel blocked);
}
