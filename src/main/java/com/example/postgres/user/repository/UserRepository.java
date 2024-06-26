package com.example.postgres.user.repository;

import com.example.postgres.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    // Block 09 Spring Security
    Optional<UserEntity> findByEmail(String email);
}
