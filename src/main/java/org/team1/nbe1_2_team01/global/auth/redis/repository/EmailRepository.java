package org.team1.nbe1_2_team01.global.auth.redis.repository;

import org.springframework.data.repository.CrudRepository;
import org.team1.nbe1_2_team01.global.auth.redis.token.EmailToken;

import java.util.Optional;
import java.util.UUID;

public interface EmailRepository extends CrudRepository<EmailToken,String> {
    boolean existsByCode(String code);
    Optional<EmailToken> findByCode(String code);

}
