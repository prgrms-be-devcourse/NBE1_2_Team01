package org.team1.nbe1_2_team01.domain.group.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.team1.nbe1_2_team01.domain.group.entity.Belonging;

import java.util.List;

public interface BelongingRepository extends JpaRepository<Belonging, Long> {

    @Query("select distinct b.user.id " +
            "from Belonging b " +
            "where b.course = :course and b.user is not null")
    List<Long> findDistinctUserIdsByCourse(@Param("course") String course);

    boolean existsByCourse(String course);

}