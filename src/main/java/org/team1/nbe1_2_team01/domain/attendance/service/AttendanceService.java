package org.team1.nbe1_2_team01.domain.attendance.service;

import jakarta.persistence.EntityNotFoundException;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.team1.nbe1_2_team01.domain.attendance.entity.Attendance;
import org.team1.nbe1_2_team01.domain.attendance.repository.AttendanceRepository;
import org.team1.nbe1_2_team01.domain.attendance.service.dto.AttendanceCreateCommand;
import org.team1.nbe1_2_team01.domain.attendance.service.dto.AttendanceUpdateCommand;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;

    /**
     * 출결 요청 등록
     * @param attendanceCreateCommand - 출결 요청 등록 필요 데이터
     * @return attendance - 등록된 출결 요청
     */
    @Transactional
    public Attendance registAttendance(AttendanceCreateCommand attendanceCreateCommand) {
        // 이미 user에 맞는 출결 요청 내역이 있다면 예외 발생

        var attendance = attendanceCreateCommand.toEntity();

        attendanceRepository.save(attendance);
        return attendance;
    }

    /**
     * 출결 요청 수정
     * @param attendanceUpdateCommand - 출결 요청 수정 데이터
     * @return attendance - 수정된 출결 요청
     */
    @Transactional
    public Attendance updateAttendance(AttendanceUpdateCommand attendanceUpdateCommand) {
        var attendance = attendanceRepository.findById(attendanceUpdateCommand.id())
                .orElseThrow(() -> new NoSuchElementException("출결 요청을 찾을 수 없습니다."));

        attendance.update(attendanceUpdateCommand);
        return attendance;
    }

    /**
     * 출결 요청 삭제
     * @param attendanceId - 출결 요청 식별자
     */
    @Transactional
    public void deleteAttendance(Long attendanceId) {
        try {
            attendanceRepository.deleteById(attendanceId);
        } catch (EntityNotFoundException e) {
            throw new NoSuchElementException("출결 요청을 찾을 수 없습니다.");
        }
    }
}
