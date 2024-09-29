package org.team1.nbe1_2_team01.domain.attendance.controller;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.team1.nbe1_2_team01.domain.attendance.controller.dto.AttendanceCreateRequest;
import org.team1.nbe1_2_team01.domain.attendance.controller.dto.AttendanceUpdateRequest;
import org.team1.nbe1_2_team01.domain.attendance.entity.Attendance;
import org.team1.nbe1_2_team01.domain.attendance.service.AttendanceService;
import org.team1.nbe1_2_team01.domain.user.repository.UserRepository;
import org.team1.nbe1_2_team01.global.util.SecurityUtil;

@RestController
@RequestMapping("/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;
    private final UserRepository userRepository;

    /**
     * 출결 요청 등록
     * @param attendanceCreateRequest 출결 요청 생성에 필요한 데이터
     * @return 등록된 attendance id url 반환
     */
    @PostMapping("/register")
    public ResponseEntity<Attendance> registAttendance(
            @RequestBody @Valid AttendanceCreateRequest attendanceCreateRequest
    ) {
        var registerId = getCurrentUserId();
        var addAttendanceCommand = attendanceCreateRequest.toCommand();

        var attendanceId = attendanceService.registAttendance(registerId, addAttendanceCommand);
        return ResponseEntity
                .created(URI.create("/attendance/" + attendanceId))
                .build();
    }

    /**
     * 출결 요청 수정
     * @param attendanceUpdateRequest 출결 요청 수정에 필요한 데이터
     * @return
     */
    @PatchMapping("/{id}")
    public ResponseEntity<Attendance> updateAttendance(
            @PathVariable("id") Long attendanceId,
            @RequestBody @Valid AttendanceUpdateRequest attendanceUpdateRequest
    ) {
        var currentUserId = getCurrentUserId();
        var updateAttendanceCommand = attendanceUpdateRequest.toCommand(attendanceId);

        attendanceService.updateAttendance(currentUserId, updateAttendanceCommand);
        return ResponseEntity
                .ok().build();
    }

    /**
     * 출결 요청 삭제
     * @param attendanceId 삭제할 출결 요청 id
     * @return no content
     */
    @DeleteMapping("/{id}}")
    public ResponseEntity<String> deleteAttendance(
            @PathVariable("id") Long attendanceId
    ) {
        var currentUserId = getCurrentUserId();

        attendanceService.deleteAttendance(currentUserId, attendanceId);
        return ResponseEntity
                .noContent().build();
    }

    /**
     * 출결 요청 승인
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
     * 출결 요청 반려
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

    /**
     * userId를 바로 반환받을 수 있다면 해당 메서드는 지워질 예정
     * @return 현재 api에 접근한 user id
     */
    private Long getCurrentUserId() {
        var username = SecurityUtil.getCurrentUsername();
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 유저입니다."));
        return user.getId();
    }
}
