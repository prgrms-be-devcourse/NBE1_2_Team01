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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.team1.nbe1_2_team01.domain.calendar.controller.dto.ScheduleCreateRequest;
import org.team1.nbe1_2_team01.domain.calendar.controller.dto.ScheduleDeleteRequest;
import org.team1.nbe1_2_team01.domain.calendar.controller.dto.ScheduleUpdateRequest;
import org.team1.nbe1_2_team01.domain.calendar.service.ScheduleQueryService;
import org.team1.nbe1_2_team01.domain.calendar.service.ScheduleService;
import org.team1.nbe1_2_team01.domain.calendar.service.response.ScheduleIdResponse;
import org.team1.nbe1_2_team01.domain.calendar.service.response.ScheduleResponse;
import org.team1.nbe1_2_team01.global.util.Response;

@RestController
@RequestMapping("/api/admin/schedules")
@RequiredArgsConstructor
public class ScheduleAdminController {

    private final ScheduleQueryService scheduleQueryService;
    private final ScheduleService scheduleService;

    /**
     * 모든 일정 조회
     */
    @GetMapping
    public Response<List<ScheduleResponse>> getAllSchedules() {
        return Response.success(scheduleQueryService.getAllSchedules());
    }

    /**
     * 공통(공지) 일정 등록
     */
    @PostMapping
    public ResponseEntity<Response<ScheduleIdResponse>> registSchedule(
            @RequestBody ScheduleCreateRequest scheduleCreateRequest
    ) {
        var scheduleIdResponse = scheduleService.registSchedule(scheduleCreateRequest.belongingId(),
                scheduleCreateRequest);
        return ResponseEntity
                .created(URI.create("/api/schedules/common"))
                .body(Response.success(scheduleIdResponse));
    }

    /**
     * 공통(공지) 일정 수정
     */
    @PatchMapping
    public Response<ScheduleIdResponse> updateSchedule(
            @RequestBody ScheduleUpdateRequest scheduleUpdateRequest
    ) {
        return Response.success(scheduleService.updateSchedule(scheduleUpdateRequest));
    }

    /**
     * 공통(공지) 일정 삭제
     */
    @DeleteMapping
    public ResponseEntity<Void> deleteSchedule(
            @RequestBody ScheduleDeleteRequest scheduleDeleteRequest
    ) {
        scheduleService.deleteSchedule(scheduleDeleteRequest.id());
        return ResponseEntity.noContent()
                .build();
    }
}
