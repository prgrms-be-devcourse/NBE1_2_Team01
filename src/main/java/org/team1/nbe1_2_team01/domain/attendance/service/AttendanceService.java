package org.team1.nbe1_2_team01.domain.attendance.service;

import java.time.LocalDate;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.team1.nbe1_2_team01.domain.attendance.controller.dto.AttendanceCreateRequest;
import org.team1.nbe1_2_team01.domain.attendance.controller.dto.AttendanceUpdateRequest;
import org.team1.nbe1_2_team01.domain.attendance.entity.Attendance;
import org.team1.nbe1_2_team01.domain.attendance.exception.AlreadyExistException;
import org.team1.nbe1_2_team01.domain.attendance.repository.AttendanceRepository;
import org.team1.nbe1_2_team01.domain.attendance.service.response.AttendanceIdResponse;
import org.team1.nbe1_2_team01.domain.user.entity.User;
import org.team1.nbe1_2_team01.domain.user.repository.UserRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;

    public AttendanceIdResponse registAttendance(
            String registerName,
            AttendanceCreateRequest attendanceCreateRequest
    ) {
        User register = getCurrentUser(registerName);

        validateAlreadyRegistToday(register);

        Attendance attendance = attendanceCreateRequest.toEntity(register);
        Attendance savedAttendance = attendanceRepository.save(attendance);

        return AttendanceIdResponse.from(savedAttendance.getId());
    }

    private void validateAlreadyRegistToday(User register) {
        attendanceRepository.findByUserIdAndStartAt(register.getId(), LocalDate.now())
                .ifPresent(attendance -> {
                    throw new AlreadyExistException("이미 오늘 등록된 요청이 있습니다");
                });
    }

    public AttendanceIdResponse updateAttendance(
            String currentUsername,
            AttendanceUpdateRequest attendanceUpdateRequest
    ) {
        Attendance attendance = attendanceRepository.findById(attendanceUpdateRequest.id())
                .orElseThrow(() -> new NoSuchElementException("출결 요청을 찾을 수 없습니다."));

        User currentUser = getCurrentUser(currentUsername);
        attendance.validateRegister(currentUser.getId());

        attendance.update(attendanceUpdateRequest);

        return AttendanceIdResponse.from(attendance.getId());
    }

    public void deleteAttendance(
            String currentUsername,
            Long attendanceId
    ) {
        User currentUser = getCurrentUser(currentUsername);

        Attendance attendance = attendanceRepository.findById(attendanceId)
                .orElseThrow(() -> new NoSuchElementException("출결 요청을 찾을 수 없습니다."));

        attendance.validateRegister(currentUser.getId());

        attendanceRepository.delete(attendance);
    }

    public AttendanceIdResponse approveAttendance(Long attendanceId) {
        Attendance attendance = attendanceRepository.findById(attendanceId)
                .orElseThrow(() -> new NoSuchElementException("출결 요청을 찾을 수 없습니다."));

        attendance.approve();

        return AttendanceIdResponse.from(attendance.getId());
    }

    public void rejectAttendance(Long attendanceId) {
        attendanceRepository.deleteById(attendanceId);
    }

    // 타 도메인 메서드
    private User getCurrentUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 유저입니다."));
    }
}
