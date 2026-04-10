package com.anilist.backend.server.models.anime;

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
import jakarta.persistence.UniqueConstraint;
import lombok.Data;

@Data
@Entity
@Table(name = "user_anime", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "anime_id"})
})
public class AnimeUsersModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserModel user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "anime_id", nullable = false)
    private AnimeModel anime;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EnumAnimeStatus status = EnumAnimeStatus.WATCHING;

    @Column(nullable = false)
    private boolean isFavorite = false;

    @CreationTimestamp
    @Column(updatable = false)
    private Instant createdAt;

}
