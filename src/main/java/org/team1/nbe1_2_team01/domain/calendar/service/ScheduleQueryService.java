package org.team1.nbe1_2_team01.domain.calendar.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.team1.nbe1_2_team01.domain.calendar.entity.Schedule;
import org.team1.nbe1_2_team01.domain.calendar.repository.ScheduleRepository;
import org.team1.nbe1_2_team01.domain.calendar.service.response.ScheduleResponse;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ScheduleQueryService {

    private final ScheduleRepository scheduleRepository;

    // 공지 일정 조회
    public List<ScheduleResponse> getNoticeSchedules(
            Long validatedBelongingCourseId
    ) {
        List<Schedule> schedules = scheduleRepository.findByBelongingId(validatedBelongingCourseId);

        return schedules.stream()
                .map(schedule -> ScheduleResponse.from(schedule.getCalendar(), schedule))
                .toList();
    }

    // 팀 일정 조회
    public List<ScheduleResponse> getTeamSchedules(
            Long belongingTeamId
    ) {
        List<Schedule> schedules = scheduleRepository.findByBelongingId(belongingTeamId);

        return schedules.stream()
                .map(schedule -> ScheduleResponse.from(schedule.getCalendar(), schedule))
                .toList();
    }
}
