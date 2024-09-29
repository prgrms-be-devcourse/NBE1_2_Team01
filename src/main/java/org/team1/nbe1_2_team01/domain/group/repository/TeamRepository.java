package org.team1.nbe1_2_team01.domain.group.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.team1.nbe1_2_team01.domain.group.entity.Team;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Long> {

    List<Team> findByCreationWaiting(boolean waiting);

    @Modifying
    @Query("UPDATE Team t SET t.name = :name WHERE t.id = :id AND t.teamType = 'PROJECT'")
    int updateProjectTeamNameById(@Param("id") Long id, @Param("name") String name);

    @Modifying
    @Query("UPDATE Team t SET t.name = :name WHERE t.id = :id AND t.teamType = 'STUDY'")
    int updateStudyTeamNameById(@Param("id") Long id, @Param("name") String name);

}
