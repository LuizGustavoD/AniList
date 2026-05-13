package com.anilist.backend.server.service.moderation;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anilist.backend.server.DTO.moderation.ReportRequestDTO;
import com.anilist.backend.server.DTO.moderation.UserBlockRequestDTO;
import com.anilist.backend.server.infra.http.success.SuccessAPIResponse;
import com.anilist.backend.server.models.moderation.ReportModel;
import com.anilist.backend.server.models.moderation.UserBlockModel;
import com.anilist.backend.server.models.user.UserModel;
import com.anilist.backend.server.repository.moderation.ReportRepository;
import com.anilist.backend.server.repository.moderation.UserBlockRepository;
import com.anilist.backend.server.repository.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ModerationService {

    private final UserBlockRepository userBlockRepository;
    private final ReportRepository reportRepository;
    private final UserRepository userRepository;

    @Transactional
    public SuccessAPIResponse<Void> blockUser(String username, UserBlockRequestDTO request) {
        UserModel blocker = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        UserModel blocked = userRepository.findByUsername(request.blockedUsername())
                .orElseThrow(() -> new RuntimeException("Blocked user not found"));

        if (blocker.getId().equals(blocked.getId())) {
            throw new RuntimeException("Cannot block yourself");
        }

        if (!userBlockRepository.existsByBlockerAndBlocked(blocker, blocked)) {
            UserBlockModel block = new UserBlockModel();
            block.setBlocker(blocker);
            block.setBlocked(blocked);
            userBlockRepository.save(block);
        }

        return new SuccessAPIResponse<>(null, "Usuário bloqueado com sucesso");
    }

    @Transactional
    public SuccessAPIResponse<Void> unblockUser(String username, String blockedUsername) {
        UserModel blocker = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        UserModel blocked = userRepository.findByUsername(blockedUsername)
                .orElseThrow(() -> new RuntimeException("Blocked user not found"));

        userBlockRepository.deleteByBlockerAndBlocked(blocker, blocked);
        return new SuccessAPIResponse<>(null, "Usuário desbloqueado com sucesso");
    }

    @Transactional
    public SuccessAPIResponse<Void> submitReport(String username, ReportRequestDTO request) {
        UserModel reporter = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ReportModel report = new ReportModel();
        report.setReporter(reporter);
        report.setTargetType(request.targetType());
        report.setTargetId(request.targetId());
        report.setReason(request.reason());
        
        reportRepository.save(report);

        return new SuccessAPIResponse<>(null, "Denúncia enviada com sucesso");
    }
}
