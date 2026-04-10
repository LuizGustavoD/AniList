package com.anilist.backend.server.service.profile;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.anilist.backend.server.DTO.profile.UserFriendshipRequestDTO;
import com.anilist.backend.server.models.friendship.EnumFriendshipRequestStatus;
import com.anilist.backend.server.models.friendship.UserFriendshipRequestModel;
import com.anilist.backend.server.models.user.UserModel;
import com.anilist.backend.server.repository.friendship.UserFriendshipRepository;
import com.anilist.backend.server.repository.friendship.UserFriendshipRequestRepository;
import com.anilist.backend.server.repository.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserFriendshipService {
    
    private final SimpMessagingTemplate messagingTemplate;
    private final UserFriendshipRepository userFriendshipRepository;
    private final UserFriendshipRequestRepository userFriendshipRequestRepository;
    private final UserRepository userRepository;

    public String userFriendshipRequestSend(UserFriendshipRequestDTO request){
        try{

            UserModel sender = new UserModel();
            sender = userRepository.findByUsername(request.senderUsername())
                .orElseThrow(() -> new RuntimeException("Sender user not found"));

            UserModel receiver = new UserModel();
            receiver = userRepository.findByUsername(request.receiverUsername())
                .orElseThrow(() -> new RuntimeException("Receiver user not found"));

            if (userFriendshipRepository.existsFriendship(sender, receiver)) {
                return "You are already friends with this user.";
            }

            if (userFriendshipRequestRepository.existsBySenderAndReceiverAndStatus(sender, receiver, EnumFriendshipRequestStatus.PENDING)) {
                return "You have already sent a friend request to this user.";
            }

            UserFriendshipRequestModel newRequest = new UserFriendshipRequestModel();

            newRequest.setSender(sender);
            newRequest.setReceiver(receiver);
            newRequest.setStatus(EnumFriendshipRequestStatus.PENDING);
            userFriendshipRequestRepository.save(newRequest);

            messagingTemplate.convertAndSend("/topic/friend-requests/" + receiver.getUsername(), "New friend request from " + sender.getUsername());

            return "Friend request sent successfully.";
        } catch (Exception e) {
            return "Error sending friendship request: " + e.getMessage();
        }
    }
}
