package org.team1.nbe1_2_team01.domain.calendar.controller;

import static org.team1.nbe1_2_team01.global.util.ErrorCode.ACCESS_TYPE_NOT_ALLOWED;

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
import org.team1.nbe1_2_team01.domain.calendar.application.CourseScheduleQueryService;
import org.team1.nbe1_2_team01.domain.calendar.application.CourseScheduleService;
import org.team1.nbe1_2_team01.domain.calendar.application.TeamScheduleQueryService;
import org.team1.nbe1_2_team01.domain.calendar.application.TeamScheduleService;
import org.team1.nbe1_2_team01.domain.calendar.application.response.ScheduleIdResponse;
import org.team1.nbe1_2_team01.domain.calendar.application.response.ScheduleResponse;
import org.team1.nbe1_2_team01.domain.calendar.controller.dto.ScheduleCreateRequest;
import org.team1.nbe1_2_team01.domain.calendar.controller.dto.ScheduleDeleteRequest;
import org.team1.nbe1_2_team01.domain.calendar.controller.dto.ScheduleUpdateRequest;
import org.team1.nbe1_2_team01.domain.group.service.GroupAuthService;
import org.team1.nbe1_2_team01.global.exception.AppException;
import org.team1.nbe1_2_team01.global.util.Response;
import org.team1.nbe1_2_team01.global.util.SecurityUtil;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final CourseScheduleQueryService courseScheduleQueryService;
    private final CourseScheduleService courseScheduleService;
    private final TeamScheduleQueryService teamScheduleQueryService;
    private final TeamScheduleService teamScheduleService;
    private final GroupAuthService groupAuthService;

    /**
     * 일정 상세 조회
     * (조회 로직은 타입 별로 어떻게 받아오는지 추후 분기 처리 고민 필요)
     */
    @GetMapping("/{id}")
    public ResponseEntity<Response<ScheduleResponse>> getSchedule(
            @RequestParam String accessType,
            @PathVariable("id") Long scheduleId
    ) {
        if (accessType.equals("TEAM")) {
            return ResponseEntity.ok(
                    Response.success(teamScheduleQueryService.getTeamSchedule(scheduleId)));
        }
        else if (accessType.equals("COMMON")) {
            return ResponseEntity.ok(
                    Response.success(courseScheduleQueryService.getCourseSchedule(scheduleId)));
        }
        else {
            throw new AppException(ACCESS_TYPE_NOT_ALLOWED);
        }
    }

    /**
     * 팀 내 일정 조회
     */
    @GetMapping("/teams")
    public ResponseEntity<Response<List<ScheduleResponse>>> getTeamSchedule(
            @RequestParam Long teamId
    ) {
        var currentUsername = SecurityUtil.getCurrentUsername();

        groupAuthService.validateTeam(currentUsername, teamId);

        return ResponseEntity.ok(
                Response.success(teamScheduleQueryService.getTeamSchedules(teamId)));
    }

    /**
     * 공지 일정 조회
     * (groupAuthService 수정 필요)
     */
    @GetMapping("/commons")
    public  ResponseEntity<Response<List<ScheduleResponse>>> getNoticeSchedules(
            @RequestParam Long courseId
    ) {
        var currentUsername = SecurityUtil.getCurrentUsername();

        //Long courseId = groupAuthService.validateCourse(currentUsername, courseId);

        return ResponseEntity.ok(
                Response.success(courseScheduleQueryService.getCourseSchedules(courseId)));
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
                Response.success(teamScheduleQueryService.getTeamSchedule(scheduleId)));
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

        var scheduleIdResponse = teamScheduleService.registSchedule(scheduleCreateRequest.belongingId(),
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
                Response.success(teamScheduleService.updateSchedule(scheduleUpdateRequest)));
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

        teamScheduleService.deleteSchedule(scheduleDeleteRequest.id());
        return ResponseEntity.noContent()
                .build();
    }
}
