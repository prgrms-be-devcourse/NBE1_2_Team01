package org.team1.nbe1_2_team01.domain.calendar.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.team1.nbe1_2_team01.domain.calendar.entity.Schedule;
import org.team1.nbe1_2_team01.domain.calendar.repository.ScheduleRepository;
import org.team1.nbe1_2_team01.domain.calendar.service.response.ScheduleResponse;
import org.team1.nbe1_2_team01.domain.group.service.GroupAuthService;
import org.team1.nbe1_2_team01.domain.group.service.response.GroupAuthResponse;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ScheduleQueryService {

    private final ScheduleRepository scheduleRepository;
    private final GroupAuthService groupAuthService;

    // 공지 일정 조회
    public List<ScheduleResponse> getNoticeSchedules(
            String currentUsername,
            String course
    ) {
        Long validatedCourseBelongingId =
                groupAuthService.validateCourse(currentUsername, course);

        List<Schedule> schedules = scheduleRepository.findByBelongingId(validatedCourseBelongingId);

        return schedules.stream()
                .map(schedule -> ScheduleResponse.from(schedule.getCalendar(), schedule))
                .toList();
    }

    // 팀 일정 조회
    public List<ScheduleResponse> getTeamSchedules(
            String currentUsername,
            Long belongingId
    ) {
        GroupAuthResponse groupAuthResponse = groupAuthService.validateTeam(currentUsername, belongingId);

        List<Schedule> schedules = scheduleRepository.findByBelongingId(groupAuthResponse.belongingId());

        return schedules.stream()
                .map(schedule -> ScheduleResponse.from(schedule.getCalendar(), schedule))
                .toList();
    }
}
