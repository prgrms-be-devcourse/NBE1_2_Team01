package org.team1.nbe1_2_team01.domain.calendar.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.team1.nbe1_2_team01.domain.attendance.exception.AccessDeniedException;
import org.team1.nbe1_2_team01.domain.calendar.entity.Calendar;
import org.team1.nbe1_2_team01.domain.calendar.repository.CalendarRepository;
import org.team1.nbe1_2_team01.domain.group.entity.Belonging;
import org.team1.nbe1_2_team01.domain.group.repository.BelongingRepository;

@Service
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
            Long currentUserId,
            Long belongingId) {
        Belonging belonging = belongingRepository.findById(belongingId).orElseThrow();
        validateOwner(belonging);

        Calendar calendar = Calendar.builder()
                .belonging(belonging)
                .build();

        Calendar savedCalendar = calendarRepository.save(calendar);
        return savedCalendar.getId();
    }

    // 타 도메인 검증 메서드 - 소속이 아니면 접근 불가
    private void validateOwner(Belonging belonging) {
        if (!belonging.isOwner()) {
            throw new AccessDeniedException("팀원은 수행할 수 없습니다.");
        }
    }
}
