package org.team1.nbe1_2_team01.domain.attendance.service;

import java.time.LocalDate;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.team1.nbe1_2_team01.domain.attendance.exception.AlreadyExistException;
import org.team1.nbe1_2_team01.domain.attendance.repository.AttendanceRepository;
import org.team1.nbe1_2_team01.domain.attendance.service.command.AddAttendanceCommand;
import org.team1.nbe1_2_team01.domain.attendance.service.command.UpdateAttendanceCommand;
import org.team1.nbe1_2_team01.domain.user.entity.User;
import org.team1.nbe1_2_team01.domain.user.repository.UserRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;

    public Long registAttendance(
            String registerName,
            AddAttendanceCommand addAttendanceCommand
    ) {
        Long registerId = getCurrentUserId(registerName);

        // 오늘 등록된 요청이 있는지 확인
        attendanceRepository.findByUserIdAndStartAt(registerId, LocalDate.now())
                .ifPresent(attendance -> {
                    throw new AlreadyExistException("이미 오늘 등록된 요청이 있습니다");
                });

        var register = new User(registerId);
        var attendance = addAttendanceCommand.toEntity(register);
        var savedAttendance = attendanceRepository.save(attendance);

        return savedAttendance.getId();
    }

    public void updateAttendance(
            String currentUsername,
            UpdateAttendanceCommand updateAttendanceCommand
    ) {
        Long currentUserId = getCurrentUserId(currentUsername);

        var attendance = attendanceRepository.findById(updateAttendanceCommand.id())
                .orElseThrow(() -> new NoSuchElementException("출결 요청을 찾을 수 없습니다."));

        attendance.validateRegister(currentUserId);

        attendance.update(updateAttendanceCommand);
    }

    public void deleteAttendance(
            String currentUsername,
            Long attendanceId
    ) {
        Long currentUserId = getCurrentUserId(currentUsername);

        var attendance = attendanceRepository.findById(attendanceId)
                .orElseThrow(() -> new NoSuchElementException("출결 요청을 찾을 수 없습니다."));

        attendance.validateRegister(currentUserId);

        attendanceRepository.delete(attendance);
    }

    public void approveAttendance(Long attendanceId) {
        attendanceRepository.findById(attendanceId)
                .ifPresent(attendance -> attendance.approve());
    }

    public void rejectAttendance(Long attendanceId) {
        attendanceRepository.deleteById(attendanceId);
    }

    private Long getCurrentUserId(String username) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 유저입니다."));
        return user.getId();
    }
}
