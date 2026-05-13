package com.anilist.backend.server.models.moderation;

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
@Table(name = "reports")
public class ReportModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_id", nullable = false)
    private UserModel reporter;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EnumReportTarget targetType;

    @Column(nullable = false)
    private String targetId;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String reason;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EnumReportStatus status = EnumReportStatus.PENDING;

    @CreationTimestamp
    @Column(updatable = false)
    private Instant createdAt;

}
