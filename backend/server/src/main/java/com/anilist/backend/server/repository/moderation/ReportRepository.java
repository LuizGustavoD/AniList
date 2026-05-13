package com.anilist.backend.server.repository.moderation;

import org.springframework.data.jpa.repository.JpaRepository;
import com.anilist.backend.server.models.moderation.ReportModel;

public interface ReportRepository extends JpaRepository<ReportModel, Long> {

}
