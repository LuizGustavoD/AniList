package com.anilist.backend.server.models.activity;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;

import com.anilist.backend.server.models.user.UserModel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "activities")
public class ActivityModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserModel user;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EnumActivityType activityType;

    @Column
    private String referenceId;

    @CreationTimestamp
    @Column(updatable = false)
    private Instant createdAt;

}
