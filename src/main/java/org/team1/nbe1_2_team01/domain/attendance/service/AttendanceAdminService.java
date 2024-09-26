package org.team1.nbe1_2_team01.domain.attendance.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.team1.nbe1_2_team01.domain.attendance.entity.Attendance;
import org.team1.nbe1_2_team01.domain.attendance.repository.AttendanceRepository;

@Service
@RequiredArgsConstructor
public class AttendanceAdminService {

    private final AttendanceRepository attendanceRepository;

    /**
     * 출결 승인
     * @param attendance - 출결 요청 내역
     * @return 승인 완료된 출결 내역
     */
    @Transactional
    public Attendance approveAttendance(Attendance attendance) {
        attendance.approve();

        attendanceRepository.save(attendance);
        return attendance;
    }

    /**
     * 출결 반려
     * @param attendance - 출결 요청 내역
     */
    @Transactional
    public void rejectAttendance(Attendance attendance) {
        attendanceRepository.delete(attendance);
    }
}
