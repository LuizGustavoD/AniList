package com.anilist.backend.server.repository.friendship;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.anilist.backend.server.models.friendship.UserFriendshipModel;
import com.anilist.backend.server.models.user.UserModel;

public interface UserFriendshipRepository extends JpaRepository<UserFriendshipModel, Long> {
    @Query("SELECT f FROM UserFriendshipModel f WHERE f.user1 = :user OR f.user2 = :user")
    List<UserFriendshipModel> findAllByUser(@Param("user") UserModel user);

    boolean existsByUser1AndUser2(UserModel user1, UserModel user2);

    @Query("SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END FROM UserFriendshipModel f WHERE (f.user1 = :u1 AND f.user2 = :u2) OR (f.user1 = :u2 AND f.user2 = :u1)")
    boolean existsFriendship(@Param("u1") UserModel u1, @Param("u2") UserModel u2);

    @Query("DELETE FROM UserFriendshipModel f WHERE (f.user1 = :u1 AND f.user2 = :u2) OR (f.user1 = :u2 AND f.user2 = :u1)")
    void deleteFriendship(@Param("u1") UserModel u1, @Param("u2") UserModel u2);
}
