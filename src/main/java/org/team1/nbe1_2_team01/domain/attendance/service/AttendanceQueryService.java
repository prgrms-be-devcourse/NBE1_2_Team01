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
    private final AttendanceReader attendanceReader;
    private final UserRepository userRepository;

    public List<AttendanceResponse> getAll() {
        List<Attendance> attendances = attendanceReader.getList();

        return attendances.stream()
                .map(attendance -> AttendanceResponse.from(
                        attendance.getRegistrant().getUserId(), "user", attendance))
                .toList();
    }

    public List<AttendanceResponse> getMyAttendances(String currentUsername) {
        User currentUser = getUser(currentUsername);

        List<Attendance> attendances = attendanceReader.getList(currentUser.getId());

        return attendances.stream()
                .map(attendance -> AttendanceResponse.from(
                        attendance.getRegistrant().getUserId(), currentUser.getUsername(), attendance))
                .toList();
    }

    public AttendanceResponse getById(Long id) {
        Attendance attendance = attendanceReader.get(id);

        User registrant = getUser(attendance.getRegistrant().getUserId());

        return AttendanceResponse.from(
                attendance.getRegistrant().getUserId(), registrant.getUsername(), attendance);
    }

    public AttendanceResponse getByIdAndUserId(Long attendanceId, String currentUsername) {
        User currentUser = getUser(currentUsername);

        Attendance attendance = attendanceJpaRepository.findByIdAndRegistrant_UserId(attendanceId, currentUser.getId())
                .orElseThrow(() -> new AppException(ATTENDANCE_NOT_FOUND));

        return AttendanceResponse.from(
                attendance.getRegistrant().getUserId(), "user", attendance);
    }

    // 타 서비스 메서드
    private User getUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(USER_NOT_FOUND));
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new AppException(USER_NOT_FOUND));
    }
}
