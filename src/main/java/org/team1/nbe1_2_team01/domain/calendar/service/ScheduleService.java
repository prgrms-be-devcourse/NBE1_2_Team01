package org.team1.nbe1_2_team01.domain.calendar.service;

import static org.team1.nbe1_2_team01.global.util.ErrorCode.SCHEDULE_ACCESS_DENIED;
import static org.team1.nbe1_2_team01.global.util.ErrorCode.SCHEDULE_NOT_FOUND;
import static org.team1.nbe1_2_team01.global.util.ErrorCode.USER_NOT_OWNER;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.team1.nbe1_2_team01.domain.calendar.controller.dto.ScheduleCreateRequest;
import org.team1.nbe1_2_team01.domain.calendar.controller.dto.ScheduleUpdateRequest;
import org.team1.nbe1_2_team01.domain.calendar.entity.Calendar;
import org.team1.nbe1_2_team01.domain.calendar.entity.Schedule;
import org.team1.nbe1_2_team01.domain.calendar.repository.CalendarRepository;
import org.team1.nbe1_2_team01.domain.calendar.repository.ScheduleRepository;
import org.team1.nbe1_2_team01.domain.group.entity.Belonging;
import org.team1.nbe1_2_team01.domain.group.repository.BelongingRepository;
import org.team1.nbe1_2_team01.global.exception.AppException;

@Service
@RequiredArgsConstructor
@Transactional
public class ScheduleService {

    private final CalendarRepository calendarRepository;
    private final ScheduleRepository scheduleRepository;
    private final BelongingRepository belongingRepository;

    public Long registSchedule(
            Long currentUserId,
            Long belongingId,
            ScheduleCreateRequest scheduleCreateRequest
    ) {
        validatePermission(currentUserId, belongingId);

        Calendar calendar = calendarRepository.findByBelongingId(belongingId).orElseThrow();

        Schedule Schedule = scheduleCreateRequest.toEntity(calendar);
        Schedule savedSchedule = scheduleRepository.save(Schedule);
        return savedSchedule.getId();
    }

    public void updateSchedule(
            Long currentUserId,
            Long belongingId,
            ScheduleUpdateRequest scheduleUpdateRequest
    ) {
        validatePermission(currentUserId, belongingId);

        Schedule schedule = scheduleRepository.findById(scheduleUpdateRequest.id())
                .orElseThrow(() -> new AppException(SCHEDULE_NOT_FOUND));

        schedule.update(scheduleUpdateRequest);
    }

    public void deleteSchedule(
            Long currentUserId,
            Long belongingId,
            Long scheduleId
    ) {
        validatePermission(currentUserId, belongingId);

        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new AppException(SCHEDULE_NOT_FOUND));

        scheduleRepository.delete(schedule);
    }

    // 타 도메인 검증 메서드 - 소속이 아니면 접근 불가
    private void validatePermission(Long currentUserId, Long belongingId) {
        Belonging belonging = belongingRepository.findById(belongingId).orElseThrow();

        validateBelonging(currentUserId, belonging);
        validateOwner(belonging);
    }

    private void validateBelonging(Long currentUserId, Belonging belonging) {
        if (!belonging.getUser().getId().equals(currentUserId)) {
            throw new AppException(SCHEDULE_ACCESS_DENIED);
        }
    }

    private void validateOwner(Belonging belonging) {
        if (!belonging.isOwner()) {
            throw new AppException(USER_NOT_OWNER);
        }
    }
}
