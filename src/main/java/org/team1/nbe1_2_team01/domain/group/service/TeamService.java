package org.team1.nbe1_2_team01.domain.group.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.team1.nbe1_2_team01.domain.calendar.entity.Calendar;
import org.team1.nbe1_2_team01.domain.calendar.repository.CalendarRepository;
import org.team1.nbe1_2_team01.domain.group.controller.request.*;
import org.team1.nbe1_2_team01.domain.group.entity.Belonging;
import org.team1.nbe1_2_team01.domain.group.entity.Team;
import org.team1.nbe1_2_team01.domain.group.repository.BelongingRepository;
import org.team1.nbe1_2_team01.domain.group.repository.TeamRepository;
import org.team1.nbe1_2_team01.domain.group.service.response.BelongingIdResponse;
import org.team1.nbe1_2_team01.domain.group.service.response.TeamIdResponse;
import org.team1.nbe1_2_team01.domain.group.service.response.TeamResponse;
import org.team1.nbe1_2_team01.domain.user.entity.User;
import org.team1.nbe1_2_team01.domain.user.repository.UserRepository;
import org.team1.nbe1_2_team01.global.exception.AppException;
import org.team1.nbe1_2_team01.global.util.ErrorCode;
import org.team1.nbe1_2_team01.global.util.SecurityUtil;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TeamService {

    public final TeamRepository teamRepository;
    public final UserRepository userRepository;
    public final BelongingRepository belongingRepository;
    public final CalendarRepository calendarRepository;

    public TeamIdResponse teamCreate(TeamCreateRequest teamCreateRequest) {
        if (!teamCreateRequest.getTeamType().equals("PROJECT") && !teamCreateRequest.getTeamType().equals("STUDY")) throw new AppException(ErrorCode.MISSING_TEAM_TYPE);

        // 가입되지 않은 유저가 있다면 예외
        List<User> users = checkUsers(teamCreateRequest.getUserIds());
        List<Belonging> belongings = new ArrayList<>();

        boolean existsByCourse = belongingRepository.existsByCourse(teamCreateRequest.getCourse());
        if (!existsByCourse) throw new AppException(ErrorCode.COURSE_NOT_FOUND);
        // 저장될 팀 객체
        Team newTeam = teamCreateRequest.toProjectTeamEntity();

        Calendar teamCalendar = null;
        for (User u : users) {
            // 코스, 유저를 가지는 Belonging 생성
            boolean isOwner = teamCreateRequest.getLeaderId().equals(u.getId());
            Belonging belonging = Belonging.createBelongingOf(isOwner, teamCreateRequest.getCourse(), u);
            // 팀 정보까지 입력
            newTeam.assignBelonging(belonging);
            belongings.add(belonging);

            if (isOwner) teamCalendar = Calendar.createCalendarOf(belonging);
        }
        assert teamCalendar != null;

        teamRepository.save(newTeam);
        belongingRepository.saveAll(belongings);
        calendarRepository.save(teamCalendar);
        return TeamIdResponse.of(newTeam);
    }

    @Transactional(readOnly = true)
    public List<User> checkUsers(List<Long> userIds) {
        List<User> users = userRepository.findAllUsersByIdList(userIds);

        List<Long> foundUserIds = users.stream().map(User::getId).toList();
        for (Long userId : userIds) if (!foundUserIds.contains(userId)) throw new AppException(ErrorCode.USER_NOT_FOUND);

        return users;
    }

    @Transactional(readOnly = true)
    public List<TeamResponse> creationWaitingStudyTeamList() {
        return teamRepository.findByCreationWaiting(true).stream().map(TeamResponse::of).toList();
    }

    public TeamIdResponse studyTeamApprove(TeamApprovalUpdateRequest teamApprovalUpdateRequest) {
        Team team = teamRepository.findById(teamApprovalUpdateRequest.getTeamId()).orElseThrow(() -> new AppException(ErrorCode.TEAM_NOT_FOUND));
        if (!team.isCreationWaiting()) throw new AppException(ErrorCode.TEAM_NOT_WAITING);

        team.setCreationWaiting(false);

        return TeamIdResponse.of(teamRepository.save(team));
    }

    public TeamIdResponse teamNameUpdate(TeamNameUpdateRequest teamNameUpdateRequest) {
        Team team = teamRepository.findByIdWithLeaderBelonging(teamNameUpdateRequest.getTeamId()).orElseThrow(() -> new AppException(ErrorCode.TEAM_NOT_FOUND));
        User user = userRepository.findByUsername(SecurityUtil.getCurrentUsername()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        if (team.getTeamType().name().equals("PROJECT") && !user.getRole().name().equals("ADMIN")) throw new AppException(ErrorCode.NOT_ADMIN_USER);
        if (team.getTeamType().name().equals("STUDY")) {
            Long leaderId = team.getBelongings().get(0).getUser().getId();
            if (!user.getId().equals(leaderId)) throw new AppException(ErrorCode.NOT_TEAM_LEADER);
        }

        team.setName(teamNameUpdateRequest.getName());
        return new TeamIdResponse(teamNameUpdateRequest.getTeamId());
    }

    public List<BelongingIdResponse> teamAddMember(TeamMemberAddRequest teamMemberAddRequest) {
        User curUser = userRepository.findByUsername(SecurityUtil.getCurrentUsername()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        List<User> users = checkUsers(teamMemberAddRequest.getUserIds());

        List<Belonging> allBelongings = belongingRepository.findAllByTeamIdWithTeam(teamMemberAddRequest.getTeamId());
        if (allBelongings.isEmpty()) throw new AppException(ErrorCode.TEAM_NOT_FOUND);

        Long leaderId = allBelongings.stream()
                .filter(Belonging::isOwner)
                .findFirst()
                .orElseThrow(() -> new AppException(ErrorCode.LEADER_BELONGING_NOT_FOUND))
                .getUser().getId();

        Team team = allBelongings.get(0).getTeam();
        if (team.getTeamType().name().equals("PROJECT") && !curUser.getRole().name().equals("ADMIN")) throw new AppException(ErrorCode.NOT_ADMIN_USER);
        if (team.getTeamType().name().equals("STUDY") && !curUser.getId().equals(leaderId)) throw new AppException(ErrorCode.NOT_TEAM_LEADER);

        List<Long> existingUserIds = allBelongings.stream().map(Belonging::getUser).map(User::getId).toList();
        List<Belonging> newBelongings = new ArrayList<>();
        for (User u : users) {
            if (existingUserIds.contains(u.getId())) throw new AppException(ErrorCode.TEAM_EXISTING_MEMBER);

            Belonging belonging = Belonging.createBelongingOf(false, allBelongings.get(0).getCourse(), u);
            belonging.assignTeam(team);
            newBelongings.add(belonging);
        }

        return belongingRepository.saveAll(newBelongings).stream().map(BelongingIdResponse::of).toList();
    }

    public void projectTeamDeleteMember(TeamMemberDeleteRequest teamMemberDeleteRequest) {
        List<User> users = checkUsers(teamMemberDeleteRequest.getUserIds());
        List<Long> userIds = users.stream().map(User::getId).toList();

        List<Belonging> allBelongings = belongingRepository.findAllByTeamIdWithTeam(teamMemberDeleteRequest.getTeamId());
        if (allBelongings.isEmpty()) {
            throw new RuntimeException("존재하지 않는 팀입니다.");
        }
        if (!allBelongings.get(0).getTeam().getTeamType().name().equals("PROJECT")) throw new RuntimeException("프로젝트 팀에 대한 요청이 아닙니다.");

        Long leaderId = null;
        for (Belonging b : allBelongings) {
            if (b.isOwner()) {
                leaderId = b.getUser().getId();
            }
        }
        if (userIds.contains(leaderId)) {
            throw new RuntimeException("팀장은 삭제할 수 없습니다.");
        }

        int deleted = belongingRepository.deleteBelongings(teamMemberDeleteRequest.getTeamId(), userIds);
    }

    public void studyTeamDeleteMember(TeamMemberDeleteRequest teamMemberDeleteRequest) {
        // TODO: Authorization 헤더 보고, 팀장이 요청한 게 아니면 예외

        List<User> users = checkUsers(teamMemberDeleteRequest.getUserIds());
        List<Long> userIds = users.stream().map(User::getId).toList();

        List<Belonging> allBelongings = belongingRepository.findAllByTeamIdWithTeam(teamMemberDeleteRequest.getTeamId());
        if (allBelongings.isEmpty()) {
            throw new RuntimeException("존재하지 않는 팀입니다.");
        }
        if (!allBelongings.get(0).getTeam().getTeamType().name().equals("STUDY")) throw new RuntimeException("스터디 팀에 대한 요청이 아닙니다.");

        Long leaderId = null;
        for (Belonging b : allBelongings) {
            if (b.isOwner()) {
                leaderId = b.getUser().getId();
            }
        }
        if (userIds.contains(leaderId)) {
            throw new RuntimeException("팀장은 삭제할 수 없습니다.");
        }

        int deleted = belongingRepository.deleteBelongings(teamMemberDeleteRequest.getTeamId(), userIds);
    }

    public void projectTeamDelete(TeamDeleteRequest teamDeleteRequest) {
        // TODO: 예외처리

        Belonging ownerBelonging = belongingRepository.findByTeamIdAndIsOwner(teamDeleteRequest.getTeamId(), true);
        calendarRepository.deleteByBelonging(ownerBelonging);
        teamRepository.deleteById(teamDeleteRequest.getTeamId());
    }

    public void studyTeamDelete(TeamDeleteRequest teamDeleteRequest) {
        // TODO: 예외처리

        Belonging ownerBelonging = belongingRepository.findByTeamIdAndIsOwner(teamDeleteRequest.getTeamId(), true);
        calendarRepository.deleteByBelonging(ownerBelonging);
        teamRepository.deleteById(teamDeleteRequest.getTeamId());
    }

}
