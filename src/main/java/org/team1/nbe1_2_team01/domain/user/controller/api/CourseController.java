package org.team1.nbe1_2_team01.domain.user.controller.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.team1.nbe1_2_team01.domain.user.controller.request.CourseCreateRequest;
import org.team1.nbe1_2_team01.domain.user.controller.request.CourseUpdateRequest;
import org.team1.nbe1_2_team01.domain.user.service.CourseService;
import org.team1.nbe1_2_team01.domain.user.service.response.CourseDetailsResponse;
import org.team1.nbe1_2_team01.domain.user.service.response.CourseIdResponse;
import org.team1.nbe1_2_team01.domain.user.service.response.UserBrifResponse;
import org.team1.nbe1_2_team01.global.util.Response;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/course")
public class CourseController {
    private final CourseService courseService;

    /**
     * 코스 등록
     */
    @PostMapping("/admin")
    public ResponseEntity<Response<CourseIdResponse>> createCourse(@RequestBody CourseCreateRequest courseCreateRequest){
        CourseIdResponse courseIdResponse = courseService.createCourse(courseCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(Response.success(courseIdResponse));
    }

    /**
     *  전체 코스 조회
     */
    @GetMapping("/admin/all")
    public ResponseEntity<Response<List<CourseDetailsResponse>>> getAllCourses(){
        List<CourseDetailsResponse> courseDetailsResponses = courseService.getAllCourses();
        return ResponseEntity.ok().body(Response.success(courseDetailsResponses));
    }

    /**
     * 특정 코스 이름 수정
     */
    @PatchMapping("/admin")
    public ResponseEntity<Response<CourseIdResponse>> updateCourse(@RequestBody CourseUpdateRequest courseUpdateRequest){
        CourseIdResponse courseIdResponse = courseService.updateCourse(courseUpdateRequest);
        return ResponseEntity.ok().body(Response.success(courseIdResponse));
    }

    /**
     * 특정 코스에 속한 유저 조회
     * 관리자 전용
     */
    @GetMapping("/admin/{courseId}/users")
    public ResponseEntity<Response<List<UserBrifResponse>>> getCourseUsersForAdmins(@PathVariable Long courseId){
        return ResponseEntity.ok()
                .body(Response.success(courseService.getCourseUsersForAdmins(courseId)));
    }


    /**
     * 자신의 코스에 속한 유저 조회
     * 사용자 전용
     */
    @GetMapping("/users")
    public ResponseEntity<Response<List<UserBrifResponse>>> getCourseUsers(){
        return ResponseEntity.ok().body(Response.success(courseService.getCourseUsers()));
    }

}