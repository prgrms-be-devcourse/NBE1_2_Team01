package org.team1.nbe1_2_team01.domain.group.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.team1.nbe1_2_team01.domain.group.controller.dto.TeamCreateRequest;
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
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("팀 타입이 필요합니다.");
            }
        } catch (RuntimeException e) {
            if (e.getMessage().equals("u")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("유저가 존재하지 않습니다.");
            } else if (e.getMessage().equals("c")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("코스가 존재하지 않습니다.");
            }
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 내부 오류");
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

    @GetMapping
    public ResponseEntity<?> getCourseUsers(@RequestParam String course) {
        return ResponseEntity.ok().body(belongingService.courseUserList(course));
    }

}