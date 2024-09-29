package org.team1.nbe1_2_team01.domain.attendance.controller;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;
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
import org.team1.nbe1_2_team01.domain.user.repository.UserRepository;
import org.team1.nbe1_2_team01.global.util.SecurityUtil;

@RestController
@RequestMapping("/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;
    private final AttendanceQueryService attendanceQueryService;
    private final UserRepository userRepository;

    /**
     * 자신의 출결 요청 보기
     * @return 자신의 출결 요청 리스트
     */
    @GetMapping
    public ResponseEntity<List<Attendance>> getMyAttendances() {
        var currentUserId = getCurrentUserId();

        List<Attendance> myAttendances = attendanceQueryService.getMyAttendances(currentUserId);
        return ResponseEntity.ok(myAttendances);
    }

    /**
     * 자신의 출결 요청 상세 보기
     *
     */
    @GetMapping("{id}")
    public ResponseEntity<Attendance> getMyAttendance(
            @PathVariable("id") Long attendanceId
    ) {
        var currentUserId = getCurrentUserId();

        var myAttendance = attendanceQueryService.getByIdAndUserId(attendanceId, currentUserId);
        return ResponseEntity.ok(myAttendance);
    }

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
