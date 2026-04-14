package com.anilist.backend.server.service.message;

import org.springframework.stereotype.Service;

import com.anilist.backend.server.DTO.message.DirectMessageDTO;
import com.anilist.backend.server.infra.http.success.SuccessAPIResponse;
import com.anilist.backend.server.models.message.DirectMessageModel;
import com.anilist.backend.server.models.user.UserModel;
import com.anilist.backend.server.repository.message.DirectMessageRepository;
import com.anilist.backend.server.repository.user.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserMessageService {

    private final DirectMessageRepository directMessageRepository;
    private final UserRepository userRepository;

    public SuccessAPIResponse<?> sendDirectMessage(DirectMessageDTO directMessageDTO) {
        try {
            UserModel sender = userRepository.getById(directMessageDTO.senderId());
            UserModel receiver = userRepository.getById(directMessageDTO.receiverId());
            DirectMessageModel directMessage = new DirectMessageModel();
            directMessage.setSender(sender);
            directMessage.setReceiver(receiver);
            directMessage.setContent(directMessageDTO.content());
            directMessageRepository.save(directMessage);


            return new SuccessAPIResponse<>(null, "Message sent successfully.");
        } catch (Exception e) {
            throw new RuntimeException("Error sending direct message: " + e.getMessage());
        }
    }

    public SuccessAPIResponse<?> sendDirectMessage(DirectMessageDTO directMessageDTO, String subject) {
        try {
            // subject = username extraído do token
            UserModel sender = userRepository.findByUsername(subject)
                .orElseThrow(() -> new RuntimeException("Sender not found: " + subject));
            UserModel receiver = userRepository.getById(directMessageDTO.receiverId());
            DirectMessageModel directMessage = new DirectMessageModel();
            directMessage.setSender(sender);
            directMessage.setReceiver(receiver);
            directMessage.setContent(directMessageDTO.content());
            directMessageRepository.save(directMessage);
            return new SuccessAPIResponse<>(null, "Message sent successfully.");
        } catch (Exception e) {
            throw new RuntimeException("Error sending direct message: " + e.getMessage());
        }
    }
}
