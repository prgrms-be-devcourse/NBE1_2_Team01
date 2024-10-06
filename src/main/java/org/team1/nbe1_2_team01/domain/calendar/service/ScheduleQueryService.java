package org.team1.nbe1_2_team01.domain.calendar.service;

import static org.team1.nbe1_2_team01.global.util.ErrorCode.SCHEDULE_NOT_FOUND;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.team1.nbe1_2_team01.domain.calendar.entity.Schedule;
import org.team1.nbe1_2_team01.domain.calendar.repository.ScheduleRepository;
import org.team1.nbe1_2_team01.domain.calendar.service.response.ScheduleResponse;
import org.team1.nbe1_2_team01.global.exception.AppException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ScheduleQueryService {

    private final ScheduleRepository scheduleRepository;

    public List<ScheduleResponse> getNoticeSchedules(
            Long validatedBelongingCourseId
    ) {
        List<Schedule> schedules = scheduleRepository.findByBelongingId(validatedBelongingCourseId);

        return schedules.stream()
                .map(schedule -> ScheduleResponse.from(schedule.getCalendar(), schedule))
                .toList();
    }

    public List<ScheduleResponse> getTeamSchedules(
            Long belongingTeamId
    ) {
        List<Schedule> schedules = scheduleRepository.findByBelongingId(belongingTeamId);

        return schedules.stream()
                .map(schedule -> ScheduleResponse.from(schedule.getCalendar(), schedule))
                .toList();
    }

    public List<ScheduleResponse> getAllSchedules() {
        List<Schedule> schedules = scheduleRepository.findAll();

        return schedules.stream()
                .map(schedule -> ScheduleResponse.from(schedule.getCalendar(), schedule))
                .toList();
    }

    public ScheduleResponse getSchedule(
            Long scheduleId
    ) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new AppException(SCHEDULE_NOT_FOUND));

        return ScheduleResponse.from(schedule.getCalendar(), schedule);
    }
}
