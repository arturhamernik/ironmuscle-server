package com.muscle.user.repository;

import com.muscle.user.entity.IronUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<IronUser, Long> {
    Optional<IronUser> findByUsername(String username);
    Optional<IronUser> findByEmail(String email);
}
