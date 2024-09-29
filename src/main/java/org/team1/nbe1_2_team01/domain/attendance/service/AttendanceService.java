package org.team1.nbe1_2_team01.domain.attendance.service;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.team1.nbe1_2_team01.domain.attendance.entity.Attendance;
import org.team1.nbe1_2_team01.domain.attendance.exception.AlreadyExistException;
import org.team1.nbe1_2_team01.domain.attendance.repository.AttendanceRepository;
import org.team1.nbe1_2_team01.domain.attendance.service.command.AddAttendanceCommand;
import org.team1.nbe1_2_team01.domain.attendance.service.command.UpdateAttendanceCommand;
import org.team1.nbe1_2_team01.domain.user.repository.UserRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;

    /**
     * 출결 요청 등록
     * @param username - 현재 요청 유저 이름
     * @param addAttendanceCommand - 출결 요청 등록 필요 데이터
     * @return attendance - 등록된 출결 요청
     */
    public Attendance registAttendance(
            String username,
            AddAttendanceCommand addAttendanceCommand
    ) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 유저입니다."));

        // 오늘 등록된 요청이 있는지 확인
        if (attendanceRepository.findByUserIdAndStartAt(user.getId(), LocalDate.now()).isPresent()) {
            throw new AlreadyExistException("이미 오늘 등록된 요청이 있습니다");
        }

        // 등록
        var attendance = addAttendanceCommand.toEntity(user);
        attendanceRepository.save(attendance);
        return attendance;
    }

    /**
     * 출결 요청 수정
     * @param username - 현재 요청 유저 이름
     * @param updateAttendanceCommand - 출결 요청 수정 데이터
     * @return attendance - 수정된 출결 요청
     */
    public Attendance updateAttendance(
            String username,
            UpdateAttendanceCommand updateAttendanceCommand
    ) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 유저입니다."));
        var attendance = attendanceRepository.findById(updateAttendanceCommand.id())
                .orElseThrow(() -> new NoSuchElementException("출결 요청을 찾을 수 없습니다."));

        // 자신의 출결 요청인지 확인 -> 인증 과정을 거쳤는데 예외 처리를 할 필요가 있는지?
        attendance.validateRegister(user.getId());

        // 수정
        attendance.update(updateAttendanceCommand);
        return attendance;
    }

    /**
     * 출결 요청 삭제
     * @param username - 현재 요청 유저 이름
     * @param attendanceId - 출결 요청 식별자
     */
    public void deleteAttendance(
            String username, Long attendanceId
    ) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 유저입니다."));
        var attendance = attendanceRepository.findById(attendanceId)
                .orElseThrow(() -> new NoSuchElementException("출결 요청을 찾을 수 없습니다."));

        // 자신의 출결 요청인지 확인
        attendance.validateRegister(user.getId());

        // 삭제
        try {
            attendanceRepository.deleteById(attendanceId);
        } catch (EntityNotFoundException e) {
            throw new NoSuchElementException("출결 요청을 찾을 수 없습니다.");
        }
    }

    /**
     * 출결 승인
     * @param attendanceId - 출결 요청 식별자
     * @return 승인 완료된 출결 내역
     */
    public Attendance approveAttendance(Long attendanceId) {
        var attendance = attendanceRepository.findById(attendanceId)
                .orElseThrow(() -> new NoSuchElementException("출결 요청을 찾을 수 없습니다."));

        attendance.approve();

        attendanceRepository.save(attendance);
        return attendance;
    }

    /**
     * 출결 반려
     * @param attendanceId - 출결 요청 식별자
     */
    public void rejectAttendance(Long attendanceId) {
        try {
            attendanceRepository.deleteById(attendanceId);
        } catch (EntityNotFoundException e) {
            throw new NoSuchElementException("출결 요청을 찾을 수 없습니다.");
        }
    }
}
