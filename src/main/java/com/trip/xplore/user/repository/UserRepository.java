package com.trip.xplore.user.repository;

import com.trip.xplore.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    // TODO: Add custom queries if needed
} 