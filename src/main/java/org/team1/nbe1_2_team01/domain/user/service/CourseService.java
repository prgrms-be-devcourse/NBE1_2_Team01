package org.team1.nbe1_2_team01.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.team1.nbe1_2_team01.domain.user.controller.request.CourseCreateRequest;
import org.team1.nbe1_2_team01.domain.user.controller.request.CourseUpdateRequest;
import org.team1.nbe1_2_team01.domain.user.entity.Course;
import org.team1.nbe1_2_team01.domain.user.entity.User;
import org.team1.nbe1_2_team01.domain.user.repository.CourseRepository;
import org.team1.nbe1_2_team01.domain.user.repository.UserRepository;
import org.team1.nbe1_2_team01.domain.user.service.response.CourseDetailsResponse;
import org.team1.nbe1_2_team01.domain.user.service.response.CourseIdResponse;
import org.team1.nbe1_2_team01.domain.user.service.response.UserBrifResponse;
import org.team1.nbe1_2_team01.global.exception.AppException;
import org.team1.nbe1_2_team01.global.util.SecurityUtil;

import java.util.List;

import static org.team1.nbe1_2_team01.global.util.ErrorCode.COURSE_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CourseService {
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    @Transactional
    public CourseIdResponse createCourse(CourseCreateRequest courseCreateRequest) {
        Course course = Course.builder()
                .name(courseCreateRequest.name())
                .build();
        Long id = courseRepository.save(course).getId();
        return new CourseIdResponse(id);
    }

    public List<CourseDetailsResponse> getAllCourses() {
        return courseRepository.findAll()
                .stream()
                .map(course -> new CourseDetailsResponse(course.getId(), course.getName()))
                .toList();
    }

    @Transactional
    public CourseIdResponse updateCourse(CourseUpdateRequest courseUpdateRequest) {
        Course course = courseRepository.findById(courseUpdateRequest.id())
                .orElseThrow(() -> new AppException(COURSE_NOT_FOUND));
        course.updateName(courseUpdateRequest.name());
        return new CourseIdResponse(course.getId());
    }


    public List<UserBrifResponse> getCourseUsersForAdmins(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new AppException(COURSE_NOT_FOUND));
        return mapToUserBriefResponse(course);
    }

    public List<UserBrifResponse> getCourseUsers() {
        User currentUser = userRepository.findByUsername(SecurityUtil.getCurrentUsername())
                .orElseThrow(() -> new UsernameNotFoundException("해당 사용자가 없습니다"));
        return mapToUserBriefResponse(currentUser.getCourse());
    }

    private List<UserBrifResponse> mapToUserBriefResponse(Course course){
        return userRepository.findByCourse(course)
                .stream()
                .map(user -> new UserBrifResponse(
                        user.getId(),
                        user.getUsername(),
                        user.getName()
                )).toList();
    }
}
