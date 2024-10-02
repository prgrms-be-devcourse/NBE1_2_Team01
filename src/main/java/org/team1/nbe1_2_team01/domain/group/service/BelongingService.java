package org.team1.nbe1_2_team01.domain.group.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.team1.nbe1_2_team01.domain.calendar.entity.Calendar;
import org.team1.nbe1_2_team01.domain.calendar.repository.CalendarRepository;
import org.team1.nbe1_2_team01.domain.group.controller.request.CourseCreateRequest;
import org.team1.nbe1_2_team01.domain.group.entity.Belonging;
import org.team1.nbe1_2_team01.domain.group.repository.BelongingRepository;
import org.team1.nbe1_2_team01.domain.group.service.response.BelongingIdResponse;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BelongingService {

    private final BelongingRepository belongingRepository;
    private final CalendarRepository calendarRepository;

    public List<Long> courseUserList(String course) {
        return belongingRepository.findDistinctUserIdsByCourse(course);
    }

    public BelongingIdResponse courseBelongingCreate(CourseCreateRequest courseCreateRequest) {
        Belonging courseBelonging = Belonging.createBelongingOf(false, courseCreateRequest.getCourseName(), null);
        Calendar courseCalendar = Calendar.createCalendarOf(courseBelonging);

        courseBelonging = belongingRepository.save(courseBelonging);
        calendarRepository.save(courseCalendar);

        return BelongingIdResponse.of(courseBelonging);
    }
}
