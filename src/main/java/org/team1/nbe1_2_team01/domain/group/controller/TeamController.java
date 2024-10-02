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

    private final TeamService teamService;
    private final BelongingService belongingService;

    @PostMapping
    public ResponseEntity<?> createTeam(@RequestBody TeamCreateRequest teamCreateRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Response.success(teamService.teamCreate(teamCreateRequest)));
    }

    @GetMapping("/waiting")
    public ResponseEntity<?> getCreationWaitingStudyTeams() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Response.success(teamService.creationWaitingStudyTeamList()));
    }

    @PatchMapping("/approval")
    public ResponseEntity<?> approveStudyTeam(@RequestBody TeamApprovalUpdateRequest teamApprovalUpdateRequest) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Response.success(teamService.studyTeamApprove(teamApprovalUpdateRequest)));
    }

    @PatchMapping("/name")
    public ResponseEntity<?> updateTeamName(@RequestBody TeamNameUpdateRequest teamNameUpdateRequest) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Response.success(teamService.teamNameUpdate(teamNameUpdateRequest)));
    }

    @PostMapping("/member")
    public ResponseEntity<?> addTeamMember(@RequestBody TeamMemberAddRequest teamMemberAddRequest) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Response.success(teamService.teamAddMember(teamMemberAddRequest)));
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