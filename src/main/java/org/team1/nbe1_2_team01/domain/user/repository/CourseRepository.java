package org.team1.nbe1_2_team01.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.team1.nbe1_2_team01.domain.user.entity.Course;

public interface CourseRepository extends JpaRepository<Course,Long> {
}