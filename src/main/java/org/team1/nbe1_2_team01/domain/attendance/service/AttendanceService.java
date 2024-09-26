package org.team1.nbe1_2_team01.domain.attendance.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.team1.nbe1_2_team01.domain.attendance.entity.Attendance;
import org.team1.nbe1_2_team01.domain.attendance.repository.AttendanceRepository;
import org.team1.nbe1_2_team01.domain.attendance.service.dto.AttendanceCreateCommand;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;

    /**
     * 출결 요청 등록
     * @param attendanceCreateCommand
     * @return attendance - 출결 요청 등록값
     */
    @Transactional
    public Attendance registAttendance(AttendanceCreateCommand attendanceCreateCommand) {
        Attendance attendance = attendanceCreateCommand.toEntity();

        attendanceRepository.save(attendance);
        return attendance;
    }
}
