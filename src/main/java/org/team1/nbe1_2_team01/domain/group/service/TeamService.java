package org.team1.nbe1_2_team01.domain.group.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.team1.nbe1_2_team01.domain.calendar.entity.Calendar;
import org.team1.nbe1_2_team01.domain.calendar.repository.CalendarRepository;
import org.team1.nbe1_2_team01.domain.group.controller.dto.TeamCreateRequest;
import org.team1.nbe1_2_team01.domain.group.entity.Belonging;
import org.team1.nbe1_2_team01.domain.group.entity.Team;
import org.team1.nbe1_2_team01.domain.group.repository.BelongingRepository;
import org.team1.nbe1_2_team01.domain.group.repository.TeamRepository;
import org.team1.nbe1_2_team01.domain.user.entity.User;
import org.team1.nbe1_2_team01.domain.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamService {

    public final TeamRepository teamRepository;
    public final UserRepository userRepository;
    public final BelongingRepository belongingRepository;
    public final CalendarRepository calendarRepository;

    @Transactional
    public Team projectTeamCreate(TeamCreateRequest teamCreateRequest) {
        Team newTeam = teamCreateRequest.toProjectTeamEntity();
        teamRepository.save(newTeam);

        if (!belongingRepository.existsByCourse(teamCreateRequest.getCourse())) {
            Belonging courseBelonging = Belonging.createBelongingOf(false, teamCreateRequest.getCourse(), null);
            belongingRepository.save(courseBelonging);

            Calendar courseCalendar = Calendar.createCalendarOf(courseBelonging);
            calendarRepository.save(courseCalendar);
        }

        for (Long l : teamCreateRequest.getUserIds()) {
            User user = userRepository.findById(l).orElse(null);
            if (user == null) throw new RuntimeException("u");

            boolean isOwner = teamCreateRequest.getLeaderId().equals(l);
            Belonging belonging = Belonging.createBelongingOf(isOwner, teamCreateRequest.getCourse(), user);

            newTeam.assignBelonging(belonging);
            belongingRepository.save(belonging);

            if (isOwner) {
                Calendar teamCalendar = Calendar.createCalendarOf(belonging);
                calendarRepository.save(teamCalendar);
            }
        }

        teamRepository.save(newTeam);
        return newTeam;
    }

    @Transactional
    public Team studyTeamCreate(TeamCreateRequest teamCreateRequest) {
        if (!belongingRepository.existsByCourse(teamCreateRequest.getCourse())) throw new RuntimeException("c");

        Team newTeam = teamCreateRequest.toStudyTeamEntity();
        teamRepository.save(newTeam);

        for (Long l : teamCreateRequest.getUserIds()) {
            User user = userRepository.findById(l).orElse(null);
            if (user == null) throw new RuntimeException("u");

            boolean isOwner = teamCreateRequest.getLeaderId().equals(l);
            Belonging belonging = Belonging.createBelongingOf(isOwner, teamCreateRequest.getCourse(), user);

            newTeam.assignBelonging(belonging);
            belongingRepository.save(belonging);

            if (isOwner) {
                Calendar teamCalendar = Calendar.createCalendarOf(belonging);
                calendarRepository.save(teamCalendar);
            }
        }
        teamRepository.save(newTeam);
        return newTeam;
    }

}
