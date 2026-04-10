package com.anilist.backend.server.repository.group;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anilist.backend.server.models.group.GroupModel;

public interface GroupRepository extends JpaRepository<GroupModel, Long> {
}
