package com.luizgmelo.conduit.repositories;

import com.luizgmelo.conduit.models.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserProfileRepository extends JpaRepository<UserProfile, UUID> {
    Optional<UserProfile> findUserProfileByUserUsername(String username);
}
