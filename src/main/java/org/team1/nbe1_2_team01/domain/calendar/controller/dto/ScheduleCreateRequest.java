package org.team1.nbe1_2_team01.domain.calendar.controller.dto;

import java.time.LocalDateTime;
import org.team1.nbe1_2_team01.domain.calendar.entity.Calendar;
import org.team1.nbe1_2_team01.domain.calendar.entity.Schedule;
import org.team1.nbe1_2_team01.domain.calendar.entity.ScheduleType;

public record ScheduleCreateRequest(
        String name,
        ScheduleType scheduleType,
        LocalDateTime startAt,
        LocalDateTime endAt,
        String description
) {

    public Schedule toEntity(Calendar calendar) {
        return Schedule.builder()
                .calendar(calendar)
                .name(name)
                .scheduleType(scheduleType)
                .startAt(startAt)
                .endAt(endAt)
                .description(description)
                .build();
    }
}
