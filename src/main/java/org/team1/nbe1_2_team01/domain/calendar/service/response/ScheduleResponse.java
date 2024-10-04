package org.team1.nbe1_2_team01.domain.calendar.service.response;

import java.time.LocalDateTime;
import lombok.Builder;
import org.team1.nbe1_2_team01.domain.calendar.entity.Calendar;
import org.team1.nbe1_2_team01.domain.calendar.entity.Schedule;
import org.team1.nbe1_2_team01.domain.calendar.entity.ScheduleType;

@Builder
public record ScheduleResponse(
        Long id,
        Long calendarId,
        String name,
        ScheduleType scheduleType,
        LocalDateTime startAt,
        LocalDateTime endAt,
        String description
) {

    public static ScheduleResponse from(Calendar calendar, Schedule schedule) {
        return ScheduleResponse.builder()
                .id(schedule.getId())
                .calendarId(calendar.getId())
                .name(schedule.getName())
                .scheduleType(schedule.getScheduleType())
                .startAt(schedule.getStartAt())
                .endAt(schedule.getEndAt())
                .description(schedule.getDescription())
                .build();
    }
}
