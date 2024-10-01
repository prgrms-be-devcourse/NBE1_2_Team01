package org.team1.nbe1_2_team01.domain.calendar.service;

import static org.team1.nbe1_2_team01.global.util.ErrorCode.COURSE_NOT_FOUND;
import static org.team1.nbe1_2_team01.global.util.ErrorCode.USER_NOT_FOUND;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.team1.nbe1_2_team01.domain.calendar.entity.Schedule;
import org.team1.nbe1_2_team01.domain.calendar.repository.ScheduleRepository;
import org.team1.nbe1_2_team01.domain.calendar.service.response.ScheduleResponse;
import org.team1.nbe1_2_team01.domain.group.repository.BelongingRepository;
import org.team1.nbe1_2_team01.domain.user.entity.User;
import org.team1.nbe1_2_team01.domain.user.repository.UserRepository;
import org.team1.nbe1_2_team01.global.exception.AppException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ScheduleQueryService {

    private final ScheduleRepository scheduleRepository;
    private final BelongingRepository belongingRepository;
    private final UserRepository userRepository;

    // 공지 일정 조회
    public List<ScheduleResponse> getNoticeSchedules(
            String currentUsername,
            String course
    ) {
        Long belongingId = validateCourse(course, currentUsername);

        List<Schedule> schedules = scheduleRepository.findByBelongingId(belongingId);

        return schedules.stream()
                .map(schedule -> ScheduleResponse.from(schedule.getCalendar(), schedule))
                .toList();
    }

    private Long validateCourse(String course, String username) {
        User user = getUserByUsername(username);

        List<Long> belongingIds = getBelongingIdsByCourse(course);

        return belongingIds.stream()
                .filter(id -> id.equals(user.getId()))
                .findFirst()
                .orElseThrow(() -> new AppException(COURSE_NOT_FOUND));
    }

    // 타 서비스 메서드
    private User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(USER_NOT_FOUND));
    }

    private List<Long> getBelongingIdsByCourse(String course) {
        return belongingRepository.findDistinctUserIdsByCourse(course);
    }
}
