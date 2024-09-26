package org.team1.nbe1_2_team01.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.team1.nbe1_2_team01.domain.user.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByRefreshToken(String refreshToken);
}