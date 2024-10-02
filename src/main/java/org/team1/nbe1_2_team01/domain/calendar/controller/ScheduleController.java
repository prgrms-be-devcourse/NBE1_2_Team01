package org.team1.nbe1_2_team01.domain.calendar.controller;

import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.team1.nbe1_2_team01.domain.calendar.controller.dto.ScheduleCreateRequest;
import org.team1.nbe1_2_team01.domain.calendar.controller.dto.ScheduleDeleteRequest;
import org.team1.nbe1_2_team01.domain.calendar.controller.dto.ScheduleUpdateRequest;
import org.team1.nbe1_2_team01.domain.calendar.service.ScheduleService;
import org.team1.nbe1_2_team01.domain.calendar.service.response.ScheduleIdResponse;
import org.team1.nbe1_2_team01.global.util.Response;
import org.team1.nbe1_2_team01.global.util.SecurityUtil;

@RestController
@RequestMapping("/schedule")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    /**
     * 일정 등록
     */
    @PostMapping("/regist")
    public ResponseEntity<Response<ScheduleIdResponse>> registSchedule(
            @RequestBody ScheduleCreateRequest scheduleCreateRequest
    ) {
        var register = SecurityUtil.getCurrentUsername();

        var scheduleIdResponse = scheduleService.registSchedule(register, scheduleCreateRequest.belongingId(), scheduleCreateRequest);
        return ResponseEntity
                .created(URI.create("/schedule/" + scheduleIdResponse.scheduleId()))
                .body(Response.success(scheduleIdResponse));
    }

    /**
     * 일정 수정
     */
    @PatchMapping
    public Response<ScheduleIdResponse> updateSchedule(
            @RequestBody ScheduleUpdateRequest scheduleUpdateRequest
    ) {
        var currentUsername = SecurityUtil.getCurrentUsername();

        return Response.success(scheduleService.updateSchedule(currentUsername, scheduleUpdateRequest.belongingId(), scheduleUpdateRequest));
    }

    /**
     * 일정 삭제
     */
    @DeleteMapping
    public ResponseEntity<Void> deleteSchedule(
            @RequestBody ScheduleDeleteRequest scheduleDeleteRequest
    ) {
        var currentUsername = SecurityUtil.getCurrentUsername();

        scheduleService.deleteSchedule(currentUsername, scheduleDeleteRequest.belongingId(), scheduleDeleteRequest.id());
        return ResponseEntity.noContent()
                .build();
    }
}
