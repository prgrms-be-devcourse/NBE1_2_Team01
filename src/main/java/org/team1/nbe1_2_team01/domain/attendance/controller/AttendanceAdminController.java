package org.team1.nbe1_2_team01.domain.attendance.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.team1.nbe1_2_team01.domain.attendance.entity.Attendance;
import org.team1.nbe1_2_team01.domain.attendance.service.AttendanceQueryService;
import org.team1.nbe1_2_team01.domain.attendance.service.AttendanceService;

@RestController
@RequestMapping("/attendance/admin")
@RequiredArgsConstructor
public class AttendanceAdminController {

    private final AttendanceService attendanceService;
    private final AttendanceQueryService attendanceQueryService;

    /**
     * 관리자 - 모든 출결 요청 보기
     * @return 모든 사용자의 출결 요청 정보
     */
    @GetMapping
    public ResponseEntity<List<Attendance>> findAll() {
        return ResponseEntity
                .ok(attendanceQueryService.getAll());
    }

    /**
     * 관리자 - 출결 요청 상세 내역 보기
     * @param attendanceId 조회할 출결 요청 id
     * @return 요청한 출결 상세 정보
     */
    @GetMapping("{id}")
    public ResponseEntity<Attendance> getAttendanceById(
            @PathVariable("id") long attendanceId
    ) {
        return ResponseEntity
                .ok(attendanceQueryService.getById(attendanceId));
    }

    /**
     * 관리자 - 출결 요청 승인
     * @param attendanceId 승인할 출결 요청 id
     * @return
     */
    @PostMapping("/admin/{id}/approve")
    public ResponseEntity<Attendance> approveAttendance(
            @PathVariable("id") Long attendanceId
    ) {
        attendanceService.approveAttendance(attendanceId);
        return ResponseEntity
                .ok().build();
    }

    /**
     * 관리자 - 출결 요청 반려
     * @param attendanceId 반려할 출결 요청 id
     * @return
     */
    @PostMapping("/admin/{id}/reject")
    public ResponseEntity<String> rejectAttendance(
            @PathVariable("id") Long attendanceId
    ) {
        attendanceService.rejectAttendance(attendanceId);
        return ResponseEntity
                .noContent().build();
    }
}
