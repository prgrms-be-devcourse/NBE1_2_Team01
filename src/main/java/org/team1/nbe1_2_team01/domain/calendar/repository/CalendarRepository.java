package org.team1.nbe1_2_team01.domain.calendar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.team1.nbe1_2_team01.domain.calendar.entity.Calendar;
import org.team1.nbe1_2_team01.domain.group.entity.Belonging;

public interface CalendarRepository extends JpaRepository<Calendar, Long> {
    void deleteByBelonging(Belonging ownerBelonging); // 김민우 작성: 팀 삭제 시 Belonging도 삭제되는데, 이때 연관된 Calendar도 삭제하기 위해 해당 메서드를 추가했습니다.
}
