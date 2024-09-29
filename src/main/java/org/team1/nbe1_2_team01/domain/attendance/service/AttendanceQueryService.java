package org.team1.nbe1_2_team01.domain.attendance.service;

import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.team1.nbe1_2_team01.domain.attendance.entity.Attendance;
import org.team1.nbe1_2_team01.domain.attendance.repository.AttendanceRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AttendanceQueryService {

    private final AttendanceRepository attendanceRepository;

    public List<Attendance> getAll() {
        return attendanceRepository.findAll();
    }

    public List<Attendance> getMyAttendances(Long currentUserId) {
        return attendanceRepository.findAttendancesByUserId(currentUserId);
    }

    public Attendance getById(Long id) {
        return attendanceRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("출결 요청을 찾을 수 없습니다."));
    }

    public Attendance getByIdAndUserId(Long attendanceId, Long currentUserId) {
        return attendanceRepository.findAttendanceIdByUserId(attendanceId, currentUserId)
                .orElseThrow(() -> new NoSuchElementException("출결 요청을 찾을 수 없습니다."));
    }
}
