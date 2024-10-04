package org.team1.nbe1_2_team01.domain.calendar.controller.dto;

import lombok.Builder;

@Builder
public record ScheduleDeleteRequest(
        Long belongingId,
        Long id
) {

}
