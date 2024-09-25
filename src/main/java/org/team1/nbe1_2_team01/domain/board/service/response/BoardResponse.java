package org.team1.nbe1_2_team01.domain.board.service.response;

import com.querydsl.core.Tuple;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.team1.nbe1_2_team01.domain.board.entity.Board;
import org.team1.nbe1_2_team01.domain.user.entity.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter @ToString
public class BoardResponse {

    private Long id;
    private String title;
    private String writer;
    private String categoryName;
    private String createdAt;
    private long commentCount;

    public BoardResponse(Long id, String title, String writer, LocalDateTime createdAt, long commentCount) {
        this.id = id;
        this.title = title;
        this.writer = writer;
        this.createdAt = formatTimeToString(createdAt);
        this.commentCount = commentCount;
    }

    public BoardResponse(Long id, String title, String writer, String categoryName, LocalDateTime createdAt, long commentCount) {
        this(id, title, writer, createdAt, commentCount);
        this.categoryName = categoryName;
    }

    private static String formatTimeToString(LocalDateTime createdAt) {
        return createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }
}
