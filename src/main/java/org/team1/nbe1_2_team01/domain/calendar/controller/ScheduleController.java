package org.team1.nbe1_2_team01.domain.calendar.controller;

import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.team1.nbe1_2_team01.domain.calendar.controller.dto.ScheduleCreateRequest;
import org.team1.nbe1_2_team01.domain.calendar.controller.dto.ScheduleDeleteRequest;
import org.team1.nbe1_2_team01.domain.calendar.controller.dto.ScheduleUpdateRequest;
import org.team1.nbe1_2_team01.domain.calendar.service.ScheduleQueryService;
import org.team1.nbe1_2_team01.domain.calendar.service.ScheduleService;
import org.team1.nbe1_2_team01.domain.calendar.service.response.ScheduleIdResponse;
import org.team1.nbe1_2_team01.domain.calendar.service.response.ScheduleResponse;
import org.team1.nbe1_2_team01.domain.group.service.GroupAuthService;
import org.team1.nbe1_2_team01.global.util.Response;
import org.team1.nbe1_2_team01.global.util.SecurityUtil;

@RestController
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;
    private final ScheduleQueryService scheduleQueryService;
    private final GroupAuthService groupAuthService;

    /**
     * 팀 내 일정 조회
     */
    @GetMapping("/schedules/team")
    public Response<List<ScheduleResponse>> getTeamSchedule(
            @RequestBody Long belongingTeamId
    ) {
        var currentUsername = SecurityUtil.getCurrentUsername();

        groupAuthService.validateTeam(currentUsername, belongingTeamId);

        return Response.success(scheduleQueryService.getTeamSchedules(belongingTeamId));
    }

    /**
     * 공지 일정 조회
     */
    @GetMapping("/schedules/common")
    public Response<List<ScheduleResponse>> getNoticeSchedules(
            @RequestBody String course
    ) {
        var currentUsername = SecurityUtil.getCurrentUsername();

        Long validatedBelongingCourseId = groupAuthService.validateCourse(currentUsername, course);

        return Response.success(scheduleQueryService.getNoticeSchedules(validatedBelongingCourseId));
    }

    /**
     * 일정 등록
     */
    @PostMapping("/schedules/regist")
    public ResponseEntity<Response<ScheduleIdResponse>> registSchedule(
            @RequestBody ScheduleCreateRequest scheduleCreateRequest
    ) {
        var register = SecurityUtil.getCurrentUsername();

        groupAuthService.validateTeam(register, scheduleCreateRequest.belongingId());

        var scheduleIdResponse = scheduleService.registSchedule(scheduleCreateRequest.belongingId(), scheduleCreateRequest);
        return ResponseEntity
                .created(URI.create("/schedules/" + scheduleIdResponse.scheduleId()))
                .body(Response.success(scheduleIdResponse));
    }

    /**
     * 일정 수정
     */
    @PatchMapping("/schedules")
    public Response<ScheduleIdResponse> updateSchedule(
            @RequestBody ScheduleUpdateRequest scheduleUpdateRequest
    ) {
        var currentUsername = SecurityUtil.getCurrentUsername();

        groupAuthService.validateTeam(currentUsername, scheduleUpdateRequest.belongingId());

        return Response.success(scheduleService.updateSchedule(scheduleUpdateRequest));
    }

    /**
     * 일정 삭제
     */
    @DeleteMapping("/schedules")
    public ResponseEntity<Void> deleteSchedule(
            @RequestBody ScheduleDeleteRequest scheduleDeleteRequest
    ) {
        var currentUsername = SecurityUtil.getCurrentUsername();

        groupAuthService.validateTeam(currentUsername, scheduleDeleteRequest.belongingId());

        scheduleService.deleteSchedule(scheduleDeleteRequest.id());
        return ResponseEntity.noContent()
                .build();
    }
}
