package org.team1.nbe1_2_team01.domain.group.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
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
import org.team1.nbe1_2_team01.domain.user.entity.QUser;
import org.team1.nbe1_2_team01.domain.user.entity.User;
import org.team1.nbe1_2_team01.domain.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TeamService {

    public final TeamRepository teamRepository;
    public final UserRepository userRepository;
    public final BelongingRepository belongingRepository;
    public final CalendarRepository calendarRepository;
    private final JPAQueryFactory jpaQueryFactory;

    @Transactional
    public Team projectTeamCreate(TeamCreateRequest teamCreateRequest) {
        // 가입되지 않은 유저가 있다면 예외
        List<User> users = checkUsers(teamCreateRequest.getUserIds());

        List<Belonging> belongings = new ArrayList<>();

        // 처음으로 생성되는 코스라면 코스 Belonging 생성, 코스 Calendar 생성
        Calendar courseCalendar = null;
        if (!belongingRepository.existsByCourse(teamCreateRequest.getCourse())) {
            Belonging courseBelonging = Belonging.createBelongingOf(false, teamCreateRequest.getCourse(), null);
            belongings.add(courseBelonging);
            courseCalendar = Calendar.createCalendarOf(courseBelonging);
        }

        // 생성될 팀 객체
        Team newTeam = teamCreateRequest.toProjectTeamEntity();

        Calendar teamCalendar = null;
        for (User u : users) {
            // 코스, 유저를 가지는 Belonging 생성
            boolean isOwner = teamCreateRequest.getLeaderId().equals(u.getId());
            Belonging belonging = Belonging.createBelongingOf(isOwner, teamCreateRequest.getCourse(), u);
            // 팀 정보까지 입력
            newTeam.assignBelonging(belonging);
            belongings.add(belonging);

            if (isOwner) {
                teamCalendar = Calendar.createCalendarOf(belonging);
            }
        }
        assert teamCalendar != null;

        teamRepository.save(newTeam);

        System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        belongingRepository.saveAll(belongings);
        System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

        if (courseCalendar != null) {
            calendarRepository.save(courseCalendar);
        }
        calendarRepository.save(teamCalendar);
        return newTeam;

        //TODO: 스터디 팀 생성 메서드랑 공통되는 부분 메서드로 빼기
    }

    @Transactional
    public Team studyTeamCreate(TeamCreateRequest teamCreateRequest) {
        if (!belongingRepository.existsByCourse(teamCreateRequest.getCourse())) throw new RuntimeException("존재하지 않는 코스명입니다.");
        // 가입되지 않은 유저가 있다면 예외
        List<User> users = checkUsers(teamCreateRequest.getUserIds());

        List<Belonging> belongings = new ArrayList<>();

        Team newTeam = teamCreateRequest.toStudyTeamEntity();

        Calendar teamCalendar = null;
        for (User u : users) {
            // 코스, 유저를 가지는 Belonging 생성
            boolean isOwner = teamCreateRequest.getLeaderId().equals(u.getId());
            Belonging belonging = Belonging.createBelongingOf(isOwner, teamCreateRequest.getCourse(), u);
            // 팀 정보까지 입력
            newTeam.assignBelonging(belonging);
            belongings.add(belonging);

            if (isOwner) {
                teamCalendar = Calendar.createCalendarOf(belonging);
            }
        }
        assert teamCalendar != null;

        teamRepository.save(newTeam);
        System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        belongingRepository.saveAll(belongings);
        System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        calendarRepository.save(teamCalendar);
        return newTeam;
    }

    private List<User> checkUsers(List<Long> userIds) {
        QUser user = QUser.user;
        List<User> users = jpaQueryFactory
                .selectFrom(user)
                .where(user.id.in(userIds))
                .fetch();
        List<Long> foundUserIds = users.stream().map(User::getId).toList();
        for (Long userId : userIds) {
            if (!foundUserIds.contains(userId)) {
                throw new RuntimeException("아이디가 " + userId + "인 유저는 없습니다.");
            }
        }

        return users;
    }

    public List<Team> creationWaitingStudyTeamList() {
        return teamRepository.findByCreationWaiting(true);
    }

    public Team studyTeamApprove(Long teamId) {
        Optional<Team> team = teamRepository.findById(teamId);

        if (team.isEmpty()) {
            throw new RuntimeException("존재하지 않는 팀입니다.");
        }

        if (!team.get().isCreationWaiting()) {
            throw new RuntimeException("승인 대기중인 팀이 아닙니다.");
        }

        team.get().setCreationWaiting(false);

        return teamRepository.save(team.get());
    }

}
