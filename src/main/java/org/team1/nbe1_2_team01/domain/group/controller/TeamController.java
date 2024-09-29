package org.team1.nbe1_2_team01.domain.group.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.team1.nbe1_2_team01.domain.group.controller.dto.TeamCreateRequest;
import org.team1.nbe1_2_team01.domain.group.controller.dto.TeamMemberAddRequest;
import org.team1.nbe1_2_team01.domain.group.controller.dto.TeamNameUpdateRequest;
import org.team1.nbe1_2_team01.domain.group.controller.dto.TeamResponse;
import org.team1.nbe1_2_team01.domain.group.entity.Team;
import org.team1.nbe1_2_team01.domain.group.service.BelongingService;
import org.team1.nbe1_2_team01.domain.group.service.TeamService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/teams")
@RequiredArgsConstructor
public class TeamController {
    // TODO: /teams/project 이하 엔드포인트들은 ADMIN 토큰 필요하도록 하기

    private final TeamService teamService;
    private final BelongingService belongingService;

    @PostMapping
    public ResponseEntity<?> createTeam(@RequestBody TeamCreateRequest teamCreateRequest) {
        try {
            if (teamCreateRequest.getTeamType().equals("PROJECT")) {
                Team team = teamService.projectTeamCreate(teamCreateRequest);
                return ResponseEntity.ok().body(TeamResponse.of(team));
            } else if (teamCreateRequest.getTeamType().equals("STUDY")) {
                Team team = teamService.studyTeamCreate(teamCreateRequest);
                return ResponseEntity.ok().body(TeamResponse.of(team));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("팀 타입이 필요합니다.");
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/waiting")
    public ResponseEntity<?> getCreationWaitingStudyTeams() {
        List<Team> teams = teamService.creationWaitingStudyTeamList();

        List<TeamResponse> response = new ArrayList<>();

        for (Team t : teams) {
            response.add(TeamResponse.of(t));
        }

        return ResponseEntity.ok().body(response);
    }

    @PatchMapping("/approval/{teamId}")
    public ResponseEntity<?> approveStudyTeam(@PathVariable Long teamId) {
        try {
            return ResponseEntity.ok().body(teamService.studyTeamApprove(teamId));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PatchMapping("/project/{teamId}/name")
    public ResponseEntity<?> updateProjectTeamName(@PathVariable Long teamId, @RequestBody TeamNameUpdateRequest teamNameUpdateRequest) {
        try {
            return ResponseEntity.ok().body(teamService.projectTeamNameUpdate(teamId, teamNameUpdateRequest));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PatchMapping("/study/{teamId}/name")
    public ResponseEntity<?> updateStudyTeamName(@PathVariable Long teamId, @RequestBody TeamNameUpdateRequest teamNameUpdateRequest) {
        try {
            return ResponseEntity.ok().body(teamService.studyTeamNameUpdate(teamId, teamNameUpdateRequest));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/project/{teamId}/member")
    public ResponseEntity<?> addProjectTeamMember(@PathVariable Long teamId, @RequestBody TeamMemberAddRequest teamMemberAddRequest) {
        try {
            return ResponseEntity.ok().body(teamService.projectTeamAddMember(teamId, teamMemberAddRequest));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/study/{teamId}/member")
    public ResponseEntity<?> addStudyTeamMember(@PathVariable Long teamId, @RequestBody TeamMemberAddRequest teamMemberAddRequest) {
        try {
            return ResponseEntity.ok().body(teamService.studyTeamAddMember(teamId, teamMemberAddRequest));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/project/{teamId}/member")
    public ResponseEntity<?> deleteProjectTeamMember(@PathVariable Long teamId, @RequestBody TeamMemberAddRequest teamMemberAddRequest) {
        try {
            return ResponseEntity.ok().body(teamService.projectTeamDeleteMember(teamId, teamMemberAddRequest));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/study/{teamId}/member")
    public ResponseEntity<?> deleteStudyTeamMember(@PathVariable Long teamId, @RequestBody TeamMemberAddRequest teamMemberAddRequest) {
        try {
            return ResponseEntity.ok().body(teamService.studyTeamDeleteMember(teamId, teamMemberAddRequest));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getCourseUsers(@RequestParam String course) {
        return ResponseEntity.ok().body(belongingService.courseUserList(course));
    }

}