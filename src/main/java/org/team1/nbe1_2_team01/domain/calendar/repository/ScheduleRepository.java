package org.team1.nbe1_2_team01.domain.calendar.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.team1.nbe1_2_team01.domain.calendar.entity.Schedule;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    @Query("select s "
            + "from Schedule s "
            + "join Calendar c "
            + "join Belonging b "
            + "where b.id = :belongingId")
    List<Schedule> findByBelongingId(Long belongingId);
}
