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
        User register = getCurrentUser(registerName);

        // 오늘 등록된 요청이 있는지 확인
        attendanceRepository.findByUserIdAndStartAt(register.getId(), LocalDate.now())
                .ifPresent(attendance -> {
                    throw new AlreadyExistException("이미 오늘 등록된 요청이 있습니다");
                });

        var attendance = addAttendanceCommand.toEntity(register);
        var savedAttendance = attendanceRepository.save(attendance);

        return savedAttendance.getId();
    }

    public void updateAttendance(
            String currentUsername,
            UpdateAttendanceCommand updateAttendanceCommand
    ) {
        User currentUser = getCurrentUser(currentUsername);

        var attendance = attendanceRepository.findById(updateAttendanceCommand.id())
                .orElseThrow(() -> new NoSuchElementException("출결 요청을 찾을 수 없습니다."));

        attendance.validateRegister(currentUser.getId());

        attendance.update(updateAttendanceCommand);
    }

    public void deleteAttendance(
            String currentUsername,
            Long attendanceId
    ) {
        User currentUser = getCurrentUser(currentUsername);

        var attendance = attendanceRepository.findById(attendanceId)
                .orElseThrow(() -> new NoSuchElementException("출결 요청을 찾을 수 없습니다."));

        attendance.validateRegister(currentUser.getId());

        attendanceRepository.delete(attendance);
    }

    public void approveAttendance(Long attendanceId) {
        attendanceRepository.findById(attendanceId)
                .ifPresent(attendance -> attendance.approve());
    }

    public void rejectAttendance(Long attendanceId) {
        attendanceRepository.deleteById(attendanceId);
    }

    private User getCurrentUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 유저입니다."));
    }
}
