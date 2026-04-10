package com.anilist.backend.server.repository.group;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anilist.backend.server.models.group.GroupMembershipModel;
import com.anilist.backend.server.models.group.GroupModel;
import com.anilist.backend.server.models.user.UserModel;

public interface GroupMembershipRepository extends JpaRepository<GroupMembershipModel, Long> {
    List<GroupMembershipModel> findByUser(UserModel user);
    List<GroupMembershipModel> findByGroup(GroupModel group);
    Optional<GroupMembershipModel> findByGroupAndUser(GroupModel group, UserModel user);
    boolean existsByGroupAndUser(GroupModel group, UserModel user);
}
