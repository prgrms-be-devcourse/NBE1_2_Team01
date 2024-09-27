package org.team1.nbe1_2_team01.domain.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.team1.nbe1_2_team01.domain.chat.entity.Participant;
import org.team1.nbe1_2_team01.domain.chat.entity.ParticipantPK;

import java.util.List;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, ParticipantPK> {

    List<Participant> findByUserId(Long userId);
}
