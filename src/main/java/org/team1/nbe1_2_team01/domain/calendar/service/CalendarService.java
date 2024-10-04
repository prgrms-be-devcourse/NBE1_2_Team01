package org.team1.nbe1_2_team01.domain.calendar.service;

import static org.team1.nbe1_2_team01.global.util.ErrorCode.BELONGING_NOT_FOUND;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.team1.nbe1_2_team01.domain.calendar.entity.Calendar;
import org.team1.nbe1_2_team01.domain.calendar.repository.CalendarRepository;
import org.team1.nbe1_2_team01.domain.group.entity.Belonging;
import org.team1.nbe1_2_team01.domain.group.repository.BelongingRepository;
import org.team1.nbe1_2_team01.global.exception.AppException;

@Service
@Transactional
@RequiredArgsConstructor
public class CalendarService {

    private final CalendarRepository calendarRepository;
    private final BelongingRepository belongingRepository;

    /**
     * 소속 내 캘린더 생성
     * @param belongingId 캘린더가 만들어질 소속 id
     * @return 만들어진 캘린더 id
     */
    public Long createCalendar(
            Long belongingId
    ) {
        Belonging belonging = belongingRepository.findById(belongingId)
                .orElseThrow(() -> new AppException(BELONGING_NOT_FOUND));

        Calendar calendar = Calendar.createCalendarOf(belonging);

        Calendar savedCalendar = calendarRepository.save(calendar);
        return savedCalendar.getId();
    }
}
