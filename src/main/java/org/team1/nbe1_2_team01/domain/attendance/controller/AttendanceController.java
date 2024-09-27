package org.team1.nbe1_2_team01.domain.attendance.controller;

import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.team1.nbe1_2_team01.domain.attendance.controller.dto.AttendanceCreateRequest;
import org.team1.nbe1_2_team01.domain.attendance.controller.dto.AttendanceUpdateRequest;
import org.team1.nbe1_2_team01.domain.attendance.entity.Attendance;
import org.team1.nbe1_2_team01.domain.attendance.service.AttendanceService;

@RestController
@RequestMapping("/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    /**
     * 출결 요청 등록
     * @param username - 인증 과정을 거친 username
     * @param attendanceCreateRequest - 출결 요청 생성에 필요한 데이터
     * @return
     */
    @PostMapping("/register")
    public ResponseEntity<Attendance> registAttendance(
            @AuthenticationPrincipal String username,
            @RequestBody @Valid AttendanceCreateRequest attendanceCreateRequest
    ) {
        var attendance = attendanceService.registAttendance(attendanceCreateRequest.toCommand(username));
        return ResponseEntity
                .created(URI.create("/attendance/" + attendance.getId()))
                .body(attendance);
    }

    /**
     * 출결 요청 수정
     * @param username
     * @param attendanceUpdateRequest
     * @return
     */
    @PutMapping("/update")
    public ResponseEntity<Attendance> updateAttendance(
            @AuthenticationPrincipal String username,
            @RequestBody @Valid AttendanceUpdateRequest attendanceUpdateRequest
    ) {
        var attendance = attendanceService.updateAttendance(attendanceUpdateRequest.toCommand(username));
        return ResponseEntity
                .created(URI.create("/attendance/" + attendance.getId()))
                .body(attendance);
    }

    /**
     * 출결 요청 삭제
     * @param username
     * @param id
     * @return
     */
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteAttendance(
            @AuthenticationPrincipal String username,
            @RequestBody Long id
    ) {
        attendanceService.deleteAttendance(id);
        return ResponseEntity
                .ok("성공적으로 삭제되었습니다.");
    }

    /**
     * 출결 요청 승인
     * @param username
     * @param id
     * @return
     */
    @PostMapping("/approve")
    public ResponseEntity<Attendance> approveAttendance(
            @AuthenticationPrincipal String username,
            @RequestBody Long id
    ) {
        var approvedAttendance = attendanceService.approveAttendance(id);
        return ResponseEntity
                .ok(approvedAttendance);
    }

    /**
     * 출결 요청 반려
     * @param username
     * @param id
     * @return
     */
    @PostMapping("/reject")
    public ResponseEntity<String> rejectAttendance(
            @AuthenticationPrincipal String username,
            @RequestBody Long id
    ) {
        attendanceService.rejectAttendance(id);
        return ResponseEntity
                .ok("성공적으로 반려되었습니다.");
    }
}
