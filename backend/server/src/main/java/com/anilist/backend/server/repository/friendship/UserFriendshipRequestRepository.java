package com.anilist.backend.server.repository.friendship;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anilist.backend.server.models.friendship.EnumFriendshipRequestStatus;
import com.anilist.backend.server.models.friendship.UserFriendshipRequestModel;
import com.anilist.backend.server.models.user.UserModel;

public interface UserFriendshipRequestRepository extends JpaRepository<UserFriendshipRequestModel, Long> {
    List<UserFriendshipRequestModel> findByReceiverAndStatus(UserModel receiver, EnumFriendshipRequestStatus status);
    List<UserFriendshipRequestModel> findBySenderAndStatus(UserModel sender, EnumFriendshipRequestStatus status);
    Optional<UserFriendshipRequestModel> findBySenderAndReceiver(UserModel sender, UserModel receiver);
    boolean existsBySenderAndReceiverAndStatus(UserModel sender, UserModel receiver, EnumFriendshipRequestStatus status);
}
