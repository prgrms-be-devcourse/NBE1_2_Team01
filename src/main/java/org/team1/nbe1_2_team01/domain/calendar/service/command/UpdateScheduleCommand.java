package org.team1.nbe1_2_team01.domain.calendar.service.command;

import java.time.LocalDateTime;
import org.team1.nbe1_2_team01.domain.calendar.entity.ScheduleType;

public record UpdateScheduleCommand(
        Long id,
        String name,
        ScheduleType scheduleType,
        LocalDateTime startAt,
        LocalDateTime endAt,
        String description
) {
}
