package org.team1.nbe1_2_team01.domain.calendar.controller.dto;

public record NoticeScheduleReadRequest(
        String course,
        Long belongingCourseId
) {

}
