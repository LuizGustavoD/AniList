package com.anilist.backend.server.repository.notification;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.anilist.backend.server.models.notification.NotificationModel;
import com.anilist.backend.server.models.user.UserModel;

public interface NotificationRepository extends JpaRepository<NotificationModel, Long> {

    List<NotificationModel> findByUserOrderByCreatedAtDesc(UserModel user);

    @Modifying
    @Query("UPDATE NotificationModel n SET n.isRead = true WHERE n.user = :user AND n.isRead = false")
    void markAllAsReadByUser(@Param("user") UserModel user);

    @Modifying
    @Query("UPDATE NotificationModel n SET n.isRead = true WHERE n.id = :id AND n.user = :user")
    void markAsReadByIdAndUser(@Param("id") Long id, @Param("user") UserModel user);

}
