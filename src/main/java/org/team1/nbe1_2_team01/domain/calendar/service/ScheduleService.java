package org.team1.nbe1_2_team01.domain.calendar.service;

import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.team1.nbe1_2_team01.domain.attendance.exception.AccessDeniedException;
import org.team1.nbe1_2_team01.domain.calendar.entity.Calendar;
import org.team1.nbe1_2_team01.domain.calendar.entity.Schedule;
import org.team1.nbe1_2_team01.domain.calendar.repository.CalendarRepository;
import org.team1.nbe1_2_team01.domain.calendar.repository.ScheduleRepository;
import org.team1.nbe1_2_team01.domain.calendar.service.command.AddScheduleCommand;
import org.team1.nbe1_2_team01.domain.calendar.service.command.UpdateScheduleCommand;
import org.team1.nbe1_2_team01.domain.group.entity.Belonging;
import org.team1.nbe1_2_team01.domain.group.repository.BelongingRepository;

@Service
@RequiredArgsConstructor
@Transactional
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
        validateOwner(belonging);

        Calendar calendar = calendarRepository.findByBelongingId(belongingId).orElseThrow();

        Schedule Schedule = addScheduleCommand.toEntity(calendar.getId());
        Schedule savedSchedule = scheduleRepository.save(Schedule);
        return savedSchedule.getId();
    }

    /**
     * 일정 수정
     * @param currentUserId 현재 요청 유저 id
     * @param belongingId 현재 접속된 소속 id
     * @param updateScheduleCommand 수정 일정 데이터
     */
    public void updateSchedule(
            Long currentUserId,
            Long belongingId,
            UpdateScheduleCommand updateScheduleCommand
    ) {
        Belonging belonging = belongingRepository.findById(belongingId).orElseThrow();
        validateBelonging(currentUserId, belonging);
        validateOwner(belonging);

        Schedule schedule = scheduleRepository.findById(updateScheduleCommand.id())
                .orElseThrow(() -> new NoSuchElementException("일정을 찾을 수 없습니다."));

        schedule.update(updateScheduleCommand);
    }

    /**
     * 일정 삭제
     * @param currentUserId 현재 요청 유저 id
     * @param belongingId 현재 접속된 소속 id
     * @param scheduleId 삭제할 일정 id
     */
    public void deleteSchedule(
            Long currentUserId,
            Long belongingId,
            Long scheduleId
    ) {
        Belonging belonging = belongingRepository.findById(belongingId).orElseThrow();
        validateBelonging(currentUserId, belonging);
        validateOwner(belonging);

        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new NoSuchElementException("일정을 찾을 수 없습니다."));

        scheduleRepository.delete(schedule);
    }

    /**
     * 임시 검증 메서드 - 소속이 아니면 접근 불가
     * Belonging Entity에 구현 예정
     */
    private void validateBelonging(Long currentUserId, Belonging belonging) {
        if (belonging.getUser().getId() != currentUserId) {
            throw new AccessDeniedException("접근할 수 없습니다.");
        }
    }

    private void validateOwner(Belonging belonging) {
        if (!belonging.isOwner()) {
            throw new AccessDeniedException("팀원은 수행할 수 없습니다.");
        }
    }
}
