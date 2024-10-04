package org.team1.nbe1_2_team01.domain.calendar.service;

import static org.team1.nbe1_2_team01.global.util.ErrorCode.SCHEDULE_NOT_FOUND;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.team1.nbe1_2_team01.domain.calendar.controller.dto.ScheduleCreateRequest;
import org.team1.nbe1_2_team01.domain.calendar.controller.dto.ScheduleUpdateRequest;
import org.team1.nbe1_2_team01.domain.calendar.entity.Calendar;
import org.team1.nbe1_2_team01.domain.calendar.entity.Schedule;
import org.team1.nbe1_2_team01.domain.calendar.repository.CalendarRepository;
import org.team1.nbe1_2_team01.domain.calendar.repository.ScheduleRepository;
import org.team1.nbe1_2_team01.domain.calendar.service.response.ScheduleIdResponse;
import org.team1.nbe1_2_team01.domain.group.service.GroupAuthService;
import org.team1.nbe1_2_team01.global.exception.AppException;

@Service
@RequiredArgsConstructor
@Transactional
public class ScheduleService {

    private final CalendarRepository calendarRepository;
    private final ScheduleRepository scheduleRepository;
    private final GroupAuthService groupAuthService;

    public ScheduleIdResponse registSchedule(
            Long belongingTeamId,
            ScheduleCreateRequest scheduleCreateRequest
    ) {
        Calendar calendar = calendarRepository.findByBelongingId(belongingTeamId).orElseThrow();

        Schedule Schedule = scheduleCreateRequest.toEntity(calendar);
        Schedule savedSchedule = scheduleRepository.save(Schedule);
        return ScheduleIdResponse.from(savedSchedule.getId());
    }

    public ScheduleIdResponse updateSchedule(
            ScheduleUpdateRequest scheduleUpdateRequest
    ) {
        Schedule schedule = scheduleRepository.findById(scheduleUpdateRequest.id())
                .orElseThrow(() -> new AppException(SCHEDULE_NOT_FOUND));

        schedule.update(scheduleUpdateRequest);

        return ScheduleIdResponse.from(schedule.getId());
    }

    public void deleteSchedule(
            Long scheduleId
    ) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new AppException(SCHEDULE_NOT_FOUND));

        scheduleRepository.delete(schedule);
    }
}
