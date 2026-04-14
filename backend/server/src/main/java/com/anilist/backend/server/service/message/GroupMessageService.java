package com.anilist.backend.server.service.message;

import org.springframework.stereotype.Service;

import com.anilist.backend.server.DTO.message.MessageGroupRequestDTO;
import com.anilist.backend.server.models.group.GroupModel;
import com.anilist.backend.server.models.message.GroupMessageModel;
import com.anilist.backend.server.models.user.UserModel;
import com.anilist.backend.server.repository.group.GroupRepository;
import com.anilist.backend.server.repository.message.GroupMessageRepository;
import com.anilist.backend.server.repository.user.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class GroupMessageService {
    
    private final GroupMessageRepository groupMessageRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    // Mantém apenas o método correto
    public void sendGroupMessage(MessageGroupRequestDTO request, String subject) {
        try {
            GroupModel group = groupRepository.getById(request.groupId());
            UserModel sender = userRepository.findByUsername(subject)
                .orElseThrow(() -> new RuntimeException("Sender not found: " + subject));
            GroupMessageModel groupMessage = new GroupMessageModel();
            groupMessage.setGroup(group);
            groupMessage.setSender(sender);
            groupMessage.setContent(request.content());
            groupMessageRepository.save(groupMessage);
        } catch (Exception e) {
            throw new RuntimeException("Error sending group message: " + e.getMessage());
        }
    }

}
