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
     * @param addAttendanceCommand - 출결 요청 등록 필요 데이터
     * @return attendance - 등록된 출결 요청
     */
    public Attendance registAttendance(AddAttendanceCommand addAttendanceCommand) {
        var user = userRepository.findByUsername(addAttendanceCommand.username())
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 유저입니다."));

        if (attendanceRepository.findByUserIdAndStartAt(user.getId(), LocalDate.now()).isPresent()) {
            throw new AlreadyExistException("이미 오늘 등록된 요청이 있습니다");
        }

        var attendance = addAttendanceCommand.toEntity(user);

        attendanceRepository.save(attendance);
        return attendance;
    }

    /**
     * 출결 요청 수정
     * @param updateAttendanceCommand - 출결 요청 수정 데이터
     * @return attendance - 수정된 출결 요청
     */
    public Attendance updateAttendance(UpdateAttendanceCommand updateAttendanceCommand) {
        var attendance = attendanceRepository.findById(updateAttendanceCommand.id())
                .orElseThrow(() -> new NoSuchElementException("출결 요청을 찾을 수 없습니다."));

        attendance.update(updateAttendanceCommand);
        return attendance;
    }

    /**
     * 출결 요청 삭제
     * @param attendanceId - 출결 요청 식별자
     */
    public void deleteAttendance(Long attendanceId) {
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
