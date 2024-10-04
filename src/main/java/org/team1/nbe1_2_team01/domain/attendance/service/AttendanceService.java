package org.team1.nbe1_2_team01.domain.attendance.service;

import static org.team1.nbe1_2_team01.global.util.ErrorCode.ATTENDANCE_NOT_FOUND;
import static org.team1.nbe1_2_team01.global.util.ErrorCode.REQUEST_ALREADY_EXISTS;
import static org.team1.nbe1_2_team01.global.util.ErrorCode.USER_NOT_FOUND;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.team1.nbe1_2_team01.domain.attendance.controller.dto.AttendanceCreateRequest;
import org.team1.nbe1_2_team01.domain.attendance.controller.dto.AttendanceUpdateRequest;
import org.team1.nbe1_2_team01.domain.attendance.entity.Attendance;
import org.team1.nbe1_2_team01.domain.attendance.repository.AttendanceRepository;
import org.team1.nbe1_2_team01.domain.attendance.service.port.DateTimeHolder;
import org.team1.nbe1_2_team01.domain.attendance.service.response.AttendanceIdResponse;
import org.team1.nbe1_2_team01.domain.user.entity.User;
import org.team1.nbe1_2_team01.domain.user.repository.UserRepository;
import org.team1.nbe1_2_team01.global.exception.AppException;

@Service
@Transactional
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final DateTimeHolder dateTimeHolder;
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
        attendanceRepository.findByUserIdAndStartAt(register.getId(), dateTimeHolder.getDate())
                .ifPresent(attendance -> {
                    throw new AppException(REQUEST_ALREADY_EXISTS);
                });
    }

    public AttendanceIdResponse updateAttendance(
            String currentUsername,
            AttendanceUpdateRequest attendanceUpdateRequest
    ) {
        Attendance attendance = attendanceRepository.findById(attendanceUpdateRequest.id())
                .orElseThrow(() -> new AppException(ATTENDANCE_NOT_FOUND));

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
                .orElseThrow(() -> new AppException(ATTENDANCE_NOT_FOUND));

        attendance.validateRegister(currentUser.getId());

        attendanceRepository.delete(attendance);
    }

    public AttendanceIdResponse approveAttendance(Long attendanceId) {
        Attendance attendance = attendanceRepository.findById(attendanceId)
                .orElseThrow(() -> new AppException(ATTENDANCE_NOT_FOUND));

        attendance.approve();

        return AttendanceIdResponse.from(attendance.getId());
    }

    public void rejectAttendance(Long attendanceId) {
        attendanceRepository.deleteById(attendanceId);
    }

    // 타 도메인 메서드
    private User getCurrentUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(USER_NOT_FOUND));
    }
}
