package org.team1.nbe1_2_team01.domain.group.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.team1.nbe1_2_team01.domain.calendar.repository.CalendarRepository;
import org.team1.nbe1_2_team01.domain.group.controller.request.CourseCreateRequest;
import org.team1.nbe1_2_team01.domain.group.entity.Belonging;
import org.team1.nbe1_2_team01.domain.group.repository.BelongingRepository;
import org.team1.nbe1_2_team01.domain.group.service.response.BelongingIdResponse;
import org.team1.nbe1_2_team01.domain.group.service.response.UserResponse;
import org.team1.nbe1_2_team01.domain.user.entity.User;
import org.team1.nbe1_2_team01.global.exception.AppException;
import org.team1.nbe1_2_team01.global.util.ErrorCode;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BelongingService {

    private final BelongingRepository belongingRepository;
    private final CalendarRepository calendarRepository;

    public List<UserResponse> teamMemberList(Long teamId) {
        List<User> users = belongingRepository.findUsersByTeamId(teamId);
        return users.stream().map(UserResponse::of).toList();
    }

    public BelongingIdResponse courseBelongingCreate(CourseCreateRequest courseCreateRequest) {
        if (belongingRepository.existsByCourse(courseCreateRequest.getCourseName())) throw new AppException(ErrorCode.COURSE_ALREADY_EXISTS);

        Belonging courseBelonging = Belonging.createBelongingOf(false, courseCreateRequest.getCourseName(), null);
        Calendar courseCalendar = Calendar.createCalendarOf(courseBelonging);

        courseBelonging = belongingRepository.save(courseBelonging);
        calendarRepository.save(courseCalendar);

        return BelongingIdResponse.of(courseBelonging);
    }
}
