package org.team1.nbe1_2_team01.domain.calendar.controller;

import static org.team1.nbe1_2_team01.global.util.ErrorCode.ACCESS_TYPE_NOT_ALLOWED;

import java.net.URI;
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
import org.team1.nbe1_2_team01.global.exception.AppException;
import org.team1.nbe1_2_team01.global.util.Response;

@RestController
@RequestMapping("/api/admin/schedules")
@RequiredArgsConstructor
public class ScheduleAdminController {

    private final CourseScheduleQueryService courseScheduleQueryService;
    private final CourseScheduleService courseScheduleService;
    private final TeamScheduleQueryService teamScheduleQueryService;
    private final TeamScheduleService teamScheduleService;

    /**
     * 모든 일정 조회
     */
    /*@GetMapping
    public ResponseEntity<Response<List<ScheduleResponse>>> getAllSchedules() {
        return ResponseEntity.ok(
                Response.success(scheduleQueryService.getAllSchedules()));
    }*/

    /**
     * 일정 상세 조회
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
     * 공통(공지) 일정 등록
     */
    @PostMapping
    public ResponseEntity<Void> registSchedule(
            @RequestBody ScheduleCreateRequest scheduleCreateRequest
    ) {
        var scheduleIdResponse = courseScheduleService.registSchedule(scheduleCreateRequest.belongingId(),
                scheduleCreateRequest);
        return ResponseEntity
                .created(URI.create("/api/schedules/common/" + scheduleIdResponse))
                .build();
    }

    /**
     * 공통(공지) 일정 수정
     */
    @PatchMapping
    public ResponseEntity<Response<ScheduleIdResponse>> updateSchedule(
            @RequestBody ScheduleUpdateRequest scheduleUpdateRequest
    ) {
        return ResponseEntity.ok(
                Response.success(courseScheduleService.updateSchedule(scheduleUpdateRequest)));
    }

    /**
     * 공통(공지) 일정 삭제
     */
    @DeleteMapping
    public ResponseEntity<Void> deleteSchedule(
            @RequestBody ScheduleDeleteRequest scheduleDeleteRequest
    ) {
        courseScheduleService.deleteSchedule(scheduleDeleteRequest.id());
        return ResponseEntity.noContent()
                .build();
    }
}
