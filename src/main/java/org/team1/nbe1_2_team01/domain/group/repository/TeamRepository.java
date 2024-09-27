package org.team1.nbe1_2_team01.domain.group.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.team1.nbe1_2_team01.domain.group.entity.Team;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Long> {

    List<Team> findByCreationWaiting(boolean waiting);

}
