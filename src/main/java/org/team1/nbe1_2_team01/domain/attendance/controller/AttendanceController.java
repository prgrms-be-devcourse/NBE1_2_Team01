package org.team1.nbe1_2_team01.domain.attendance.controller;

import jakarta.validation.Valid;
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
import org.springframework.web.bind.annotation.RestController;
import org.team1.nbe1_2_team01.domain.attendance.controller.dto.AttendanceCreateRequest;
import org.team1.nbe1_2_team01.domain.attendance.controller.dto.AttendanceUpdateRequest;
import org.team1.nbe1_2_team01.domain.attendance.entity.Attendance;
import org.team1.nbe1_2_team01.domain.attendance.service.AttendanceQueryService;
import org.team1.nbe1_2_team01.domain.attendance.service.AttendanceService;
import org.team1.nbe1_2_team01.global.util.SecurityUtil;

@RestController
@RequestMapping("/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;
    private final AttendanceQueryService attendanceQueryService;

    /**
     * 자신의 출결 요청 보기
     */
    @GetMapping
    public ResponseEntity<List<Attendance>> getMyAttendances() {
        var currentUsername = SecurityUtil.getCurrentUsername();

        List<Attendance> myAttendances = attendanceQueryService.getMyAttendances(currentUsername);
        return ResponseEntity.ok(myAttendances);
    }

    /**
     * 자신의 출결 요청 상세 보기
     */
    @GetMapping("{id}")
    public ResponseEntity<Attendance> getMyAttendance(
            @PathVariable("id") Long attendanceId
    ) {
        var currentUsername = SecurityUtil.getCurrentUsername();

        var myAttendance = attendanceQueryService.getByIdAndUserId(attendanceId, currentUsername);
        return ResponseEntity.ok(myAttendance);
    }

    /**
     * 출결 요청 등록
     */
    @PostMapping("/regist")
    public ResponseEntity<Attendance> registAttendance(
            @RequestBody @Valid AttendanceCreateRequest attendanceCreateRequest
    ) {
        var registerUsername = SecurityUtil.getCurrentUsername();

        var attendanceId = attendanceService.registAttendance(registerUsername, attendanceCreateRequest);
        return ResponseEntity
                .created(URI.create("/attendance/" + attendanceId))
                .build();
    }

    /**
     * 출결 요청 수정
     */
    @PatchMapping
    public ResponseEntity<Attendance> updateAttendance(
            @RequestBody @Valid AttendanceUpdateRequest attendanceUpdateRequest
    ) {
        var currentUsername = SecurityUtil.getCurrentUsername();

        attendanceService.updateAttendance(currentUsername, attendanceUpdateRequest);
        return ResponseEntity
                .ok().build();
    }

    /**
     * 출결 요청 삭제
     */
    @DeleteMapping
    public ResponseEntity<String> deleteAttendance(
            @RequestBody Long attendanceId
    ) {
        var currentUsername = SecurityUtil.getCurrentUsername();

        attendanceService.deleteAttendance(currentUsername, attendanceId);
        return ResponseEntity
                .noContent().build();
    }
}
