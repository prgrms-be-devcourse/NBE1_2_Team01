package org.team1.nbe1_2_team01.domain.calendar.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.team1.nbe1_2_team01.domain.calendar.entity.Calendar;
import org.team1.nbe1_2_team01.domain.calendar.repository.CalendarRepository;
import org.team1.nbe1_2_team01.domain.group.entity.Belonging;

@Service
@RequiredArgsConstructor
public class CalendarService {

    private final CalendarRepository calendarRepository;

    /**
     * 소속 내 캘린더 생성
     * @param belongingId 캘린더가 만들어질 소속 id
     * @return 만들어진 캘린더 id
     */
    public Long createCalendar(
            Long currentUserId,
            Long belongingId) {
        // 팀장인지 여부 조사
        // belongingService.validateOwner(currentUserId);

        Belonging belonging = new Belonging(belongingId);
        Calendar calendar = Calendar.builder()
                .belonging(belonging)
                .build();

        calendarRepository.save(calendar);
        return calendar.getId();
    }
}
