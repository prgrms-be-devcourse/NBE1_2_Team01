package org.team1.nbe1_2_team01.domain.board.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.team1.nbe1_2_team01.domain.group.entity.Team;
import org.team1.nbe1_2_team01.domain.user.entity.Course;
import org.team1.nbe1_2_team01.domain.user.entity.User;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "course_board")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CourseBoard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt = LocalDateTime.now();

    private Long readCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    private CourseBoard(
            Course course,
            User user,
            String title,
            String content) {
        this.course = course;
        this.user = user;
        this.title = title;
        this.content = content;
        user.addCourseBoard(this);;
        course.addCourseBoard(this);
    }
}
