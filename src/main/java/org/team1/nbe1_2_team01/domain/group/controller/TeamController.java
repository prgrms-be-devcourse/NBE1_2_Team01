package org.team1.nbe1_2_team01.domain.group.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.team1.nbe1_2_team01.domain.group.controller.request.*;
import org.team1.nbe1_2_team01.domain.group.service.response.TeamResponse;
import org.team1.nbe1_2_team01.domain.group.entity.Team;
import org.team1.nbe1_2_team01.domain.group.service.BelongingService;
import org.team1.nbe1_2_team01.domain.group.service.TeamService;
import org.team1.nbe1_2_team01.global.exception.AppException;
import org.team1.nbe1_2_team01.global.util.Response;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/teams")
@RequiredArgsConstructor
public class TeamController {
    // TODO: /teams/project 이하 엔드포인트들은 ADMIN 토큰 필요하도록 하기

    private final TeamService teamService;
    private final BelongingService belongingService;

    @PostMapping
    public ResponseEntity<?> createTeam(@RequestBody TeamCreateRequest teamCreateRequest) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Response.success(teamService.teamCreate(teamCreateRequest)));
        } catch (AppException e) {
            return ResponseEntity.status(e.getErrorCode().getStatus())
                    .body(Response.error(e.getErrorCode().getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Response.error("서버 내부 오류 발생"));
        }
    }

    @GetMapping("/waiting")
    public ResponseEntity<?> getCreationWaitingStudyTeams() {
        return ResponseEntity.ok().body(teamService.creationWaitingStudyTeamList());
    }

    @PatchMapping("/approval")
    public ResponseEntity<?> approveStudyTeam(@RequestBody TeamApprovalUpdateRequest teamApprovalUpdateRequest) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(teamService.studyTeamApprove(teamApprovalUpdateRequest));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PatchMapping("/project/name")
    public ResponseEntity<?> updateProjectTeamName(@RequestBody TeamNameUpdateRequest teamNameUpdateRequest) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(teamService.projectTeamNameUpdate(teamNameUpdateRequest));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PatchMapping("/study/name")
    public ResponseEntity<?> updateStudyTeamName(@RequestBody TeamNameUpdateRequest teamNameUpdateRequest) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(teamService.studyTeamNameUpdate(teamNameUpdateRequest));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/project/member")
    public ResponseEntity<?> addProjectTeamMember(@RequestBody TeamMemberAddRequest teamMemberAddRequest) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(teamService.projectTeamAddMember(teamMemberAddRequest));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/study/member")
    public ResponseEntity<?> addStudyTeamMember(@RequestBody TeamMemberAddRequest teamMemberAddRequest) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(teamService.studyTeamAddMember(teamMemberAddRequest));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/project/member")
    public ResponseEntity<?> deleteProjectTeamMember(@RequestBody TeamMemberDeleteRequest teamMemberDeleteRequest) {
        try {
            teamService.projectTeamDeleteMember(teamMemberDeleteRequest);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/study/member")
    public ResponseEntity<?> deleteStudyTeamMember(@RequestBody TeamMemberDeleteRequest teamMemberDeleteRequest) {
        try {
            teamService.studyTeamDeleteMember(teamMemberDeleteRequest);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/project")
    public ResponseEntity<?> deleteProjectTeam(@RequestBody TeamDeleteRequest teamDeleteRequest) {
        try {
            teamService.projectTeamDelete(teamDeleteRequest);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/study")
    public ResponseEntity<?> deleteStudyTeam(@RequestBody TeamDeleteRequest teamDeleteRequest) {
        try {
            teamService.studyTeamDelete(teamDeleteRequest);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping // pk 써서 사용해보기
    public ResponseEntity<?> getCourseUsers(@RequestParam String course) {
        return ResponseEntity.status(HttpStatus.OK).body(belongingService.courseUserList(course));
    }

}