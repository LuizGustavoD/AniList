package com.anilist.backend.server.repository.message;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anilist.backend.server.models.message.DirectMessageModel;
import com.anilist.backend.server.models.user.UserModel;

public interface DirectMessageRepository extends JpaRepository<DirectMessageModel, Long> {
    List<DirectMessageModel> findBySenderAndReceiverOrderByCreatedAtAsc(UserModel sender, UserModel receiver);
    List<DirectMessageModel> findBySenderOrReceiverOrderByCreatedAtAsc(UserModel sender, UserModel receiver);
}
