package org.team1.nbe1_2_team01.domain.attendance.service;

import static org.team1.nbe1_2_team01.global.util.ErrorCode.ATTENDANCE_NOT_FOUND;
import static org.team1.nbe1_2_team01.global.util.ErrorCode.USER_NOT_FOUND;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.team1.nbe1_2_team01.domain.attendance.entity.Attendance;
import org.team1.nbe1_2_team01.domain.attendance.repository.AttendanceJpaRepository;
import org.team1.nbe1_2_team01.domain.attendance.service.response.AttendanceResponse;
import org.team1.nbe1_2_team01.domain.user.entity.User;
import org.team1.nbe1_2_team01.domain.user.repository.UserRepository;
import org.team1.nbe1_2_team01.global.exception.AppException;

@Service
@RequiredArgsConstructor
public class AttendanceQueryService {

    private final AttendanceJpaRepository attendanceJpaRepository;
    private final UserRepository userRepository;

    public List<AttendanceResponse> getAll() {
        List<Attendance> attendances = attendanceJpaRepository.findAll();

        return attendances.stream()
                .map(attendance -> AttendanceResponse.from(
                        attendance.getRegistrant().getUserId(), "user", attendance))
                .toList();
    }

    public List<AttendanceResponse> getMyAttendances(String currentUsername) {
        User currentUser = getUserByUsername(currentUsername);

        List<Attendance> myAttendances = attendanceJpaRepository.findByRegistrant_UserId(currentUser.getId());

        return myAttendances.stream()
                .map(attendance -> AttendanceResponse.from(
                        attendance.getRegistrant().getUserId(), "user", attendance))
                .toList();
    }

    public AttendanceResponse getById(Long id) {
        Attendance attendance = attendanceJpaRepository.findById(id)
                .orElseThrow(() -> new AppException(ATTENDANCE_NOT_FOUND));

        return AttendanceResponse.from(
                attendance.getRegistrant().getUserId(), "user", attendance);
    }

    public AttendanceResponse getByIdAndUserId(Long attendanceId, String currentUsername) {
        User currentUser = getUserByUsername(currentUsername);

        Attendance attendance = attendanceJpaRepository.findByIdAndRegistrant_UserId(attendanceId, currentUser.getId())
                .orElseThrow(() -> new AppException(ATTENDANCE_NOT_FOUND));

        return AttendanceResponse.from(
                attendance.getRegistrant().getUserId(), "user", attendance);
    }

    // 타 서비스 메서드
    private User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(USER_NOT_FOUND));
    }
}
