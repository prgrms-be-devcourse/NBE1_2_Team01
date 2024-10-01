package org.team1.nbe1_2_team01.domain.group.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.team1.nbe1_2_team01.domain.group.entity.Belonging;

import java.util.List;
import java.util.Optional;

public interface BelongingRepository extends JpaRepository<Belonging, Long> {

    @Query("select distinct b.user.id " +
            "from Belonging b " +
            "where b.course = :course and b.user is not null")
    List<Long> findDistinctUserIdsByCourse(@Param("course") String course);

    boolean existsByCourse(String course);

    @Query("select b " +
            "from Belonging b " +
            "join b.team t " +
            "join b.user u " +
            "where t.id = :teamId")
    List<Belonging> findAllByTeamIdWithTeam(@Param("teamId") Long teamId);

    @Modifying
    @Query("delete " +
            "from Belonging b " +
            "where b.team.id = :teamId and b.isOwner = false and b.user.id in :userIds"
    )
    int deleteBelongings(@Param("teamId") Long teamId, @Param("userIds") List<Long> userIds);

    Belonging findByTeamIdAndIsOwner(Long teamId, boolean isOwner);

    Optional<Belonging> findByTeam_IdAndUser_Username(Long teamId, String username);
}
