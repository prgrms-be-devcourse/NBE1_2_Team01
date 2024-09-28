package org.team1.nbe1_2_team01.domain.calendar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.team1.nbe1_2_team01.domain.calendar.entity.Calendar;

public interface CalendarRepository extends JpaRepository<Calendar, Long> {
}