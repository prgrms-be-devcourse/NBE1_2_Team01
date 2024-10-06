package org.team1.nbe1_2_team01.domain.calendar.controller;

import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;
    private final ScheduleQueryService scheduleQueryService;
    private final GroupAuthService groupAuthService;

    /**
     * 팀 내 일정 조회
     */
    @GetMapping
    public ResponseEntity<Response<List<ScheduleResponse>>> getTeamSchedule(
            @RequestParam Long teamId
    ) {
        var currentUsername = SecurityUtil.getCurrentUsername();

        groupAuthService.validateTeam(currentUsername, teamId);

        return ResponseEntity.ok(
                Response.success(scheduleQueryService.getTeamSchedules(teamId)));
    }

    /**
     * 공지 일정 조회
     */
    @GetMapping("/common")
    public  ResponseEntity<Response<List<ScheduleResponse>>> getNoticeSchedules(
            @RequestParam String course
    ) {
        var currentUsername = SecurityUtil.getCurrentUsername();

        Long validatedBelongingCourseId = groupAuthService.validateCourse(currentUsername, course);

        return ResponseEntity.ok(
                Response.success(scheduleQueryService.getNoticeSchedules(validatedBelongingCourseId)));
    }

    /**
     * 일정 상세 조회
     */
    @GetMapping("/{id}")
    public ResponseEntity<Response<ScheduleResponse>> getSchedule(
            @RequestParam Long teamId,
            @PathVariable("id") Long scheduleId
    ) {
        var currentUsername = SecurityUtil.getCurrentUsername();

        groupAuthService.validateTeam(currentUsername, teamId);

        return ResponseEntity.ok(
                Response.success(scheduleQueryService.getSchedule(scheduleId)));
    }

    /**
     * 일정 등록
     */
    @PostMapping
    public ResponseEntity<Response<ScheduleIdResponse>> registSchedule(
            @RequestBody ScheduleCreateRequest scheduleCreateRequest
    ) {
        var register = SecurityUtil.getCurrentUsername();

        groupAuthService.validateTeam(register, scheduleCreateRequest.belongingId());

        var scheduleIdResponse = scheduleService.registSchedule(scheduleCreateRequest.belongingId(),
                scheduleCreateRequest);
        return ResponseEntity
                .created(URI.create("/api/schedules/" + scheduleIdResponse.scheduleId()))
                .body(Response.success(scheduleIdResponse));
    }

    /**
     * 일정 수정
     */
    @PatchMapping
    public ResponseEntity<Response<ScheduleIdResponse>> updateSchedule(
            @RequestBody ScheduleUpdateRequest scheduleUpdateRequest
    ) {
        var currentUsername = SecurityUtil.getCurrentUsername();

        groupAuthService.validateTeam(currentUsername, scheduleUpdateRequest.belongingId());

        return ResponseEntity.ok(
                Response.success(scheduleService.updateSchedule(scheduleUpdateRequest)));
    }

    /**
     * 일정 삭제
     */
    @DeleteMapping
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
