package org.team1.nbe1_2_team01.global.auth.jwt.respository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.team1.nbe1_2_team01.global.auth.jwt.token.RefreshToken;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
    Optional<RefreshToken> findByToken(String refreshToken);
}
