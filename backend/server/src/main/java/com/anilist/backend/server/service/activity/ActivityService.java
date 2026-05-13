package com.anilist.backend.server.service.activity;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anilist.backend.server.DTO.activity.ActivityResponseDTO;
import com.anilist.backend.server.models.activity.ActivityModel;
import com.anilist.backend.server.models.activity.EnumActivityType;
import com.anilist.backend.server.models.friendship.UserFriendshipModel;
import com.anilist.backend.server.models.user.UserModel;
import com.anilist.backend.server.repository.activity.ActivityRepository;
import com.anilist.backend.server.repository.friendship.UserFriendshipRepository;
import com.anilist.backend.server.repository.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final UserFriendshipRepository friendshipRepository;
    private final UserRepository userRepository;

    @Transactional
    public void createActivity(UserModel user, EnumActivityType type, String referenceId) {
        ActivityModel activity = new ActivityModel();
        activity.setUser(user);
        activity.setActivityType(type);
        activity.setReferenceId(referenceId);
        activityRepository.save(activity);
    }

    public Page<ActivityResponseDTO> getFeed(String username, Pageable pageable) {
        UserModel user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<UserFriendshipModel> friendships = friendshipRepository.findAllByUser(user);
        
        List<UUID> friendIds = friendships.stream()
                .map(f -> f.getUser1().getId().equals(user.getId()) ? f.getUser2().getId() : f.getUser1().getId())
                .collect(Collectors.toList());
        
        friendIds.add(user.getId()); // Include user's own activities

        return activityRepository.findActivitiesByUserIds(friendIds, pageable)
                .map(ActivityResponseDTO::fromEntity);
    }
}
