package com.anilist.backend.server.repository.message;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anilist.backend.server.models.group.GroupModel;
import com.anilist.backend.server.models.message.GroupMessageModel;

public interface GroupMessageRepository extends JpaRepository<GroupMessageModel, Long> {
    List<GroupMessageModel> findByGroupOrderByCreatedAtAsc(GroupModel group);
}
