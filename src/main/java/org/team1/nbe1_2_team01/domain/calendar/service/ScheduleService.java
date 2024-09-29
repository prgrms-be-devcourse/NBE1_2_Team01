package org.team1.nbe1_2_team01.domain.calendar.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.team1.nbe1_2_team01.domain.attendance.exception.AccessDeniedException;
import org.team1.nbe1_2_team01.domain.calendar.entity.Calendar;
import org.team1.nbe1_2_team01.domain.calendar.entity.Schedule;
import org.team1.nbe1_2_team01.domain.calendar.repository.CalendarRepository;
import org.team1.nbe1_2_team01.domain.calendar.repository.ScheduleRepository;
import org.team1.nbe1_2_team01.domain.calendar.service.command.AddScheduleCommand;
import org.team1.nbe1_2_team01.domain.group.entity.Belonging;
import org.team1.nbe1_2_team01.domain.group.repository.BelongingRepository;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final CalendarRepository calendarRepository;
    private final ScheduleRepository scheduleRepository;
    private final BelongingRepository belongingRepository;

    /**
     * 일정 등록
     * @param currentUserId 현재 요청 유저 id
     * @param belongingId 현재 접속된 소속 id
     * @param addScheduleCommand 등록할 일정 데이터
     * @return 등록된 일정 id
     */
    public Long registSchedule(
            Long currentUserId,
            Long belongingId,
            AddScheduleCommand addScheduleCommand
    ) {
        Belonging belonging = belongingRepository.findById(belongingId).orElseThrow();
        validateBelonging(currentUserId, belonging);

        Calendar calendar = calendarRepository.findByBelongingId(belongingId).orElseThrow();

        Schedule Schedule = addScheduleCommand.toEntity(calendar.getId());
        Schedule savedSchedule = scheduleRepository.save(Schedule);
        return savedSchedule.getId();
    }

    /**
     * 임시 검증 메서드
     * Belonging Entity에 구현 예정
     */
    private void validateBelonging(Long currentUserId, Belonging belonging) {
        if (belonging.getUser().getId() != currentUserId) {
            throw new AccessDeniedException("접근할 수 없습니다.");
        }
    }
}
