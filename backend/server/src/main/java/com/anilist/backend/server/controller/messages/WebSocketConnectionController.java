package com.anilist.backend.server.controller.messages;


import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Controller;

import com.anilist.backend.server.DTO.message.DirectMessageDTO;
import com.anilist.backend.server.DTO.message.MessageGroupRequestDTO;
import com.anilist.backend.server.service.message.UserMessageService;
import com.anilist.backend.server.service.message.GroupMessageService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class WebSocketConnectionController {

    private final SimpMessagingTemplate messagingTemplate;
    private final UserMessageService userMessageService;
    private final GroupMessageService groupMessageService;


    @MessageMapping("/private")
    public void sendPrivateMessage(DirectMessageDTO message, JwtAuthenticationToken token) {
        String userName = token.getToken().getSubject();
        userMessageService.sendDirectMessage(message, userName);
        messagingTemplate.convertAndSend("/topic/private." + message.receiverId(), message);
    }

    @MessageMapping("/group")
    public void sendGroupMessage(MessageGroupRequestDTO request, JwtAuthenticationToken token) {
        String userName = token.getToken().getSubject();
        groupMessageService.sendGroupMessage(request, userName);
        messagingTemplate.convertAndSend("/topic/group." + request.groupId(), request);
    }

}
