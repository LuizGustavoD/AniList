package com.anilist.backend.server.repository.user;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anilist.backend.server.models.role.EnumRole;
import com.anilist.backend.server.models.role.RolesModel;

public interface RolesRepository extends JpaRepository<RolesModel, UUID> {
    Optional<RolesModel> findByRole(EnumRole role);
}
