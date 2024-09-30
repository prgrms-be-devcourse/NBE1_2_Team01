package org.team1.nbe1_2_team01.domain.group.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.team1.nbe1_2_team01.domain.group.controller.request.TeamCreateRequest;
import org.team1.nbe1_2_team01.domain.group.controller.request.TeamMemberAddRequest;
import org.team1.nbe1_2_team01.domain.group.controller.request.TeamNameUpdateRequest;
import org.team1.nbe1_2_team01.domain.group.service.response.TeamResponse;
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
        // TODO: 프로젝트 팀인지 스터디 팀인지 분기하는 로직을 서비스로 옮기기
        try {
            if (teamCreateRequest.getTeamType().equals("PROJECT")) {
                return ResponseEntity.status(HttpStatus.CREATED).body(teamService.projectTeamCreate(teamCreateRequest));
            } else if (teamCreateRequest.getTeamType().equals("STUDY")) {
                return ResponseEntity.status(HttpStatus.CREATED).body(teamService.studyTeamCreate(teamCreateRequest));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("팀 타입이 필요합니다.");
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/waiting")
    public ResponseEntity<?> getCreationWaitingStudyTeams() {
        return ResponseEntity.ok().body(teamService.creationWaitingStudyTeamList());
    }

    @PatchMapping("/approval/{teamId}")
    public ResponseEntity<?> approveStudyTeam(@PathVariable Long teamId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(teamService.studyTeamApprove(teamId));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PatchMapping("/project/{teamId}/name")
    public ResponseEntity<?> updateProjectTeamName(@PathVariable Long teamId, @RequestBody TeamNameUpdateRequest teamNameUpdateRequest) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(teamService.projectTeamNameUpdate(teamId, teamNameUpdateRequest));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PatchMapping("/study/{teamId}/name")
    public ResponseEntity<?> updateStudyTeamName(@PathVariable Long teamId, @RequestBody TeamNameUpdateRequest teamNameUpdateRequest) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(teamService.studyTeamNameUpdate(teamId, teamNameUpdateRequest));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/project/{teamId}/member")
    public ResponseEntity<?> addProjectTeamMember(@PathVariable Long teamId, @RequestBody TeamMemberAddRequest teamMemberAddRequest) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(teamService.projectTeamAddMember(teamId, teamMemberAddRequest));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/study/{teamId}/member")
    public ResponseEntity<?> addStudyTeamMember(@PathVariable Long teamId, @RequestBody TeamMemberAddRequest teamMemberAddRequest) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(teamService.studyTeamAddMember(teamId, teamMemberAddRequest));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/project/{teamId}/member")
    public ResponseEntity<?> deleteProjectTeamMember(@PathVariable Long teamId, @RequestBody TeamMemberAddRequest teamMemberAddRequest) {
        try {
            teamService.projectTeamDeleteMember(teamId, teamMemberAddRequest);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/study/{teamId}/member")
    public ResponseEntity<?> deleteStudyTeamMember(@PathVariable Long teamId, @RequestBody TeamMemberAddRequest teamMemberAddRequest) {
        try {
            teamService.studyTeamDeleteMember(teamId, teamMemberAddRequest);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/project/{teamId}")
    public ResponseEntity<?> deleteProjectTeam(@PathVariable Long teamId) {
        try {
            teamService.projectTeamDelete(teamId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/study/{teamId}")
    public ResponseEntity<?> deleteStudyTeam(@PathVariable Long teamId) {
        try {
            teamService.studyTeamDelete(teamId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getCourseUsers(@RequestParam String course) {
        return ResponseEntity.status(HttpStatus.OK).body(belongingService.courseUserList(course));
    }

}