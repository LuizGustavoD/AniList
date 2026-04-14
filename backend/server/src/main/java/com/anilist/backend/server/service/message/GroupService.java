package com.anilist.backend.server.service.message;

import org.springframework.stereotype.Service;

import com.anilist.backend.server.repository.group.GroupRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class GroupService {
    
    private final GroupRepository groupRepository;



}
