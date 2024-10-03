package org.team1.nbe1_2_team01.domain.group.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.team1.nbe1_2_team01.domain.group.controller.request.*;
import org.team1.nbe1_2_team01.domain.group.service.BelongingService;
import org.team1.nbe1_2_team01.domain.group.service.TeamService;
import org.team1.nbe1_2_team01.global.util.Response;

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

    @PostMapping("/approval")
    public ResponseEntity<?> approveStudyTeamCreation(@RequestBody TeamApprovalUpdateRequest teamApprovalUpdateRequest) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Response.success(teamService.studyTeamCreationApprove(teamApprovalUpdateRequest)));
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

    @DeleteMapping("/member")
    public ResponseEntity<?> deleteTeamMember(@RequestBody TeamMemberDeleteRequest teamMemberDeleteRequest) {
        teamService.teamDeleteMember(teamMemberDeleteRequest);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping
    public ResponseEntity<?> deleteTeam(@RequestBody TeamDeleteRequest teamDeleteRequest) {
        teamService.teamDelete(teamDeleteRequest);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/approval")
    public ResponseEntity<?> approveStudyTeamDeletion(@RequestBody TeamApprovalUpdateRequest teamApprovalUpdateRequest) {
        teamService.studyTeamDeletionApprove(teamApprovalUpdateRequest);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }

    @GetMapping("/members/{teamId}")
    public ResponseEntity<?> getTeamMembers(@PathVariable Long teamId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Response.success(belongingService.teamMemberList(teamId)));
    }

}