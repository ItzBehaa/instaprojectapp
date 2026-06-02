package com.example.instaproject.repository;

import com.example.instaproject.models.Follow;
import com.example.instaproject.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {
    Optional<Follow> findByFollowerAndFollowing(User follower, User following);
    boolean existsByFollowerAndFollowing(User follower, User following);
    int countByFollower(User follower);
    int countByFollowing(User following);
}
