package org.team1.nbe1_2_team01.domain.board.service.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.team1.nbe1_2_team01.domain.board.comment.service.response.CommentResponse;
import org.team1.nbe1_2_team01.domain.board.service.converter.DateTimeToStringConverter;

import java.time.LocalDateTime;
import java.util.List;

@Getter @ToString
public class BoardDetailResponse {

    private Long id;
    private String title;
    private String content;
    private String writer;
    private String createdAt;
    private boolean isAdmin;
    private boolean isMine;
    private List<CommentResponse> comments;

    @Builder
    private BoardDetailResponse(Long id, String title, String content, String writer, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.createdAt = DateTimeToStringConverter.convert(createdAt);
    }

    public void addComments(List<CommentResponse> comments) {
        this.comments = comments;
    }

}
