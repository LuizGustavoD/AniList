package com.anilist.backend.server.repository.activity;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.anilist.backend.server.models.activity.ActivityModel;

public interface ActivityRepository extends JpaRepository<ActivityModel, Long> {

    @Query("SELECT a FROM ActivityModel a WHERE a.user.id IN :userIds ORDER BY a.createdAt DESC")
    Page<ActivityModel> findActivitiesByUserIds(@Param("userIds") List<java.util.UUID> userIds, Pageable pageable);

}
