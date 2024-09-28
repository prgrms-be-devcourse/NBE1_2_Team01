package org.team1.nbe1_2_team01.domain.attendance.controller;

import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
import org.team1.nbe1_2_team01.global.util.SecurityUtil;

@RestController
@RequestMapping("/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    /**
     * 출결 요청 등록
     * @param attendanceCreateRequest - 출결 요청 생성에 필요한 데이터
     * @return
     */
    @PostMapping("/register")
    public ResponseEntity<Attendance> registAttendance(
            @RequestBody @Valid AttendanceCreateRequest attendanceCreateRequest
    ) {
        String username = SecurityUtil.getCurrentUsername();
        var attendance = attendanceService.registAttendance(username, attendanceCreateRequest.toCommand());
        return ResponseEntity
                .created(URI.create("/attendance/" + attendance.getId()))
                .body(attendance);
    }

    /**
     * 출결 요청 수정
     * @param attendanceUpdateRequest
     * @return
     */
    @PutMapping("/update")
    public ResponseEntity<Attendance> updateAttendance(
            @RequestBody @Valid AttendanceUpdateRequest attendanceUpdateRequest
    ) {
        String username = SecurityUtil.getCurrentUsername();

        var attendance = attendanceService.updateAttendance(username, attendanceUpdateRequest.toCommand());
        return ResponseEntity
                .created(URI.create("/attendance/" + attendance.getId()))
                .body(attendance);
    }

    /**
     * 출결 요청 삭제
     * @param attendanceId
     * @return
     */
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteAttendance(
            @RequestBody Long attendanceId
    ) {
        String username = SecurityUtil.getCurrentUsername();

        attendanceService.deleteAttendance(attendanceId);
        return ResponseEntity
                .ok("성공적으로 삭제되었습니다.");
    }

    /**
     * 출결 요청 승인
     * @param attendanceId
     * @return
     */
    @PostMapping("/admin/approve")
    public ResponseEntity<Attendance> approveAttendance(
            @RequestBody Long attendanceId
    ) {
        String username = SecurityUtil.getCurrentUsername();

        var approvedAttendance = attendanceService.approveAttendance(attendanceId);
        return ResponseEntity
                .ok(approvedAttendance);
    }

    /**
     * 출결 요청 반려
     * @param attendanceId
     * @return
     */
    @PostMapping("/admin/reject")
    public ResponseEntity<String> rejectAttendance(
            @RequestBody Long attendanceId
    ) {
        String username = SecurityUtil.getCurrentUsername();

        attendanceService.rejectAttendance(attendanceId);
        return ResponseEntity
                .ok("성공적으로 반려되었습니다.");
    }
}
