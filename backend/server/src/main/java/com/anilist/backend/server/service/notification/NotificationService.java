package com.anilist.backend.server.service.notification;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anilist.backend.server.DTO.notification.NotificationResponseDTO;
import com.anilist.backend.server.infra.http.success.SuccessAPIResponse;
import com.anilist.backend.server.models.notification.EnumNotificationType;
import com.anilist.backend.server.models.notification.NotificationModel;
import com.anilist.backend.server.models.user.UserModel;
import com.anilist.backend.server.repository.notification.NotificationRepository;
import com.anilist.backend.server.repository.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @Transactional
    public void createNotification(UserModel user, EnumNotificationType type, String content) {
        NotificationModel notification = new NotificationModel();
        notification.setUser(user);
        notification.setType(type);
        notification.setContent(content);
        notificationRepository.save(notification);
    }

    public List<NotificationResponseDTO> getUserNotifications(String username) {
        UserModel user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        return notificationRepository.findByUserOrderByCreatedAtDesc(user)
                .stream()
                .map(NotificationResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public SuccessAPIResponse<Void> markAsRead(String username, Long notificationId) {
        UserModel user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        notificationRepository.markAsReadByIdAndUser(notificationId, user);
        return new SuccessAPIResponse<>(null, "Notificação marcada como lida");
    }

    @Transactional
    public SuccessAPIResponse<Void> markAllAsRead(String username) {
        UserModel user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        notificationRepository.markAllAsReadByUser(user);
        return new SuccessAPIResponse<>(null, "Todas as notificações marcadas como lidas");
    }

}
