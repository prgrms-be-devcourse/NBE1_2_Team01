package org.team1.nbe1_2_team01.domain.attendance.service;

import static org.team1.nbe1_2_team01.global.util.ErrorCode.ATTENDANCE_NOT_FOUND;
import static org.team1.nbe1_2_team01.global.util.ErrorCode.USER_NOT_FOUND;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.team1.nbe1_2_team01.domain.attendance.entity.Attendance;
import org.team1.nbe1_2_team01.domain.attendance.repository.AttendanceRepository;
import org.team1.nbe1_2_team01.domain.attendance.service.response.AttendanceResponse;
import org.team1.nbe1_2_team01.domain.user.entity.User;
import org.team1.nbe1_2_team01.domain.user.repository.UserRepository;
import org.team1.nbe1_2_team01.global.exception.AppException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AttendanceQueryService {

    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;

    public List<AttendanceResponse> getAll() {
        List<Attendance> attendances = attendanceRepository.findAll();

        return attendances.stream()
                .map(attendance -> AttendanceResponse.from(attendance.getUser(), attendance))
                .toList();
    }

    public List<AttendanceResponse> getMyAttendances(String currentUsername) {
        User currentUser = getUserByUsername(currentUsername);

        List<Attendance> myAttendances = attendanceRepository.findAttendancesByUserId(currentUser.getId());

        return myAttendances.stream()
                .map(attendance -> AttendanceResponse.from(attendance.getUser(), attendance))
                .toList();
    }

    public AttendanceResponse getById(Long id) {
        Attendance attendance = attendanceRepository.findById(id)
                .orElseThrow(() -> new AppException(ATTENDANCE_NOT_FOUND));

        return AttendanceResponse.from(attendance.getUser(), attendance);
    }

    public AttendanceResponse getByIdAndUserId(Long attendanceId, String currentUsername) {
        User currentUser = getUserByUsername(currentUsername);

        Attendance attendance = attendanceRepository.findByIdAndUserId(attendanceId, currentUser.getId())
                .orElseThrow(() -> new AppException(ATTENDANCE_NOT_FOUND));

        return AttendanceResponse.from(attendance.getUser(), attendance);
    }

    // 타 서비스 메서드
    private User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(USER_NOT_FOUND));
    }
}
