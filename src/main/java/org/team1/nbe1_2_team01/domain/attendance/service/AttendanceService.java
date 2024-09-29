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

    /**
     * 출결 요청 등록
     * @param registerId - 현재 요청 유저 id
     * @param addAttendanceCommand - 출결 요청 등록 필요 데이터
     * @return attendanceId - 등록된 출결 요청 id
     */
    public Long registAttendance(
            Long registerId,
            AddAttendanceCommand addAttendanceCommand
    ) {
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

    /**
     * 출결 요청 수정
     * @param currentUserId - 현재 요청 유저 id
     * @param updateAttendanceCommand - 출결 요청 수정 데이터
     */
    public void updateAttendance(
            Long currentUserId,
            UpdateAttendanceCommand updateAttendanceCommand
    ) {
        var attendance = attendanceRepository.findById(updateAttendanceCommand.id())
                .orElseThrow(() -> new NoSuchElementException("출결 요청을 찾을 수 없습니다."));

        attendance.validateRegister(currentUserId);

        attendance.update(updateAttendanceCommand);
    }

    /**
     * 출결 요청 삭제
     * @param currentUserId - 현재 요청 유저 id
     * @param attendanceId - 출결 요청 식별자
     */
    public void deleteAttendance(
            Long currentUserId,
            Long attendanceId
    ) {
        var attendance = attendanceRepository.findById(attendanceId)
                .orElseThrow(() -> new NoSuchElementException("출결 요청을 찾을 수 없습니다."));

        attendance.validateRegister(currentUserId);

        attendanceRepository.delete(attendance);
    }

    /**
     * 출결 승인
     * @param attendanceId - 출결 요청 식별자
     */
    public void approveAttendance(Long attendanceId) {
        attendanceRepository.findById(attendanceId)
                .ifPresent(attendance -> attendance.approve());
    }

    /**
     * 출결 반려
     * @param attendanceId - 출결 요청 식별자
     */
    public void rejectAttendance(Long attendanceId) {
        attendanceRepository.deleteById(attendanceId);
    }
}
