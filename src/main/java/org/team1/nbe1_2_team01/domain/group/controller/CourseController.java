package org.team1.nbe1_2_team01.domain.group.controller;

import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.team1.nbe1_2_team01.domain.group.controller.request.CourseCreateRequest;
import org.team1.nbe1_2_team01.domain.group.service.BelongingService;
import org.team1.nbe1_2_team01.domain.group.service.TeamService;
import org.team1.nbe1_2_team01.global.util.Response;

@RestController
@RequestMapping("/api/course")
@RequiredArgsConstructor
public class CourseController {

    private final BelongingService belongingService;
    private final TeamService teamService;

    @PostMapping
    public ResponseEntity<?> createCourse(@RequestBody CourseCreateRequest courseCreateRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Response.success(belongingService.courseBelongingCreate(courseCreateRequest)));
    }

    // 특정코스에 어떤팀들있는지 목록
    @GetMapping("/teams")
    public ResponseEntity<?> courseTeamList(@RequestParam String course) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Response.success(teamService.courseTeamList(course)));
    }

}
