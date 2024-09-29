package org.team1.nbe1_2_team01.domain.calendar.service.command;

import java.time.LocalDateTime;
import org.team1.nbe1_2_team01.domain.calendar.entity.Calendar;
import org.team1.nbe1_2_team01.domain.calendar.entity.Schedule;
import org.team1.nbe1_2_team01.domain.calendar.entity.ScheduleType;

public record AddScheduleCommand(
        String name,
        ScheduleType scheduleType,
        LocalDateTime startAt,
        LocalDateTime endAt,
        String description
) {

    public Schedule toEntity(Long calendarId) {
        Calendar calendar = new Calendar(calendarId);

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
