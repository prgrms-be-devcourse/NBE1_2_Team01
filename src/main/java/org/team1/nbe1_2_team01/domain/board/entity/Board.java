package org.team1.nbe1_2_team01.domain.board.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.team1.nbe1_2_team01.domain.board.controller.dto.BoardUpdateRequest;
import org.team1.nbe1_2_team01.domain.group.entity.Belonging;
import org.team1.nbe1_2_team01.domain.user.entity.User;
import org.team1.nbe1_2_team01.global.exception.AppException;
import org.team1.nbe1_2_team01.global.util.ErrorCode;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "board")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board {

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "belonging_id")
    private Belonging belonging;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "board")
    private List<Comment> comments = new ArrayList<>();


    @Builder
    private Board(
            Belonging belonging,
            Category category,
            User user,
            String title,
            String content) {
        this.belonging = belonging;
        this.category = category;
        this.user = user;
        this.title = title;
        this.content = content;
        user.addBoard(this);

        if(category != null) {
            category.addBoards(this);
        }

        belonging.addBoards(this);
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
    }


    public void updateBoard(BoardUpdateRequest updateRequest) {
        String newTitle = updateRequest.title();
        String newContent = updateRequest.content();

        if(this.title.equals(newTitle) && this.content.equals(newContent)) {
            throw new AppException(ErrorCode.BOARD_NOT_UPDATED);
        }

        this.title = newTitle;
        this.content = newContent;
        this.updatedAt = LocalDateTime.now();
    }
}
