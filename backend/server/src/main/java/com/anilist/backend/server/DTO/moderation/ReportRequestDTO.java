package com.anilist.backend.server.DTO.moderation;

import com.anilist.backend.server.models.moderation.EnumReportTarget;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ReportRequestDTO(
    @NotNull EnumReportTarget targetType,
    @NotBlank String targetId,
    @NotBlank String reason
) {}
