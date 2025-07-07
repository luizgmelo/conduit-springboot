package com.luizgmelo.conduit.services;

import com.luizgmelo.conduit.models.Follow;
import com.luizgmelo.conduit.models.User;
import com.luizgmelo.conduit.repositories.FollowRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;

@Service
public class FollowService {

    private final FollowRepository followRepository;

    public FollowService(FollowRepository followRepository) {
        this.followRepository = followRepository;
    }

    public Set<UUID> findFollowedUserIds(User user, Set<UUID> authorIds) {
        if (user == null || authorIds.isEmpty()) {
            return Collections.emptySet();
        }
        return followRepository.findFollowedUserIds(user.getId(), authorIds);
    }

    public void follow(User follower, User followed) {
        if (!isFollowing(follower, followed)) {
            Follow follow = new Follow(follower, followed);
            followRepository.save(follow);
        }
    }

    @Transactional
    public void unfollow(User follower, User followed) {
        if (isFollowing(follower, followed)) {
            followRepository.deleteFollowerIdAndFollowedId(follower.getId(), followed.getId());
        }
    }

    public boolean isFollowing(User follower, User followed) {
        return followRepository.isFollowing(follower.getId(), followed.getId());
    }
}
