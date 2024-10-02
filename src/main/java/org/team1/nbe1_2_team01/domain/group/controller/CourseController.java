package org.team1.nbe1_2_team01.domain.group.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.team1.nbe1_2_team01.domain.group.controller.request.CourseCreateRequest;
import org.team1.nbe1_2_team01.domain.group.service.BelongingService;
import org.team1.nbe1_2_team01.global.util.Response;

@RestController
@RequestMapping("/api/course")
@RequiredArgsConstructor
public class CourseController {

    private final BelongingService belongingService;

    @PostMapping
    public ResponseEntity<?> createCourse(@RequestBody CourseCreateRequest courseCreateRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Response.success(belongingService.courseBelongingCreate(courseCreateRequest)));
    }

}
