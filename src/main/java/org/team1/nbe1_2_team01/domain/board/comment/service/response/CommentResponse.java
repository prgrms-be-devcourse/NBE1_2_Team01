package org.team1.nbe1_2_team01.domain.board.comment.service.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter @ToString
public class CommentResponse {

    private final Long id;
    private final String writer;
    private final String content;
    private final String createdAt;
    private boolean isAdmin;
    private boolean isMine;

    @Builder
    private CommentResponse(Long id, String writer, String content, String createdAt) {
        this.id = id;
        this.writer = writer;
        this.content = content;
        this.createdAt = createdAt;
    }
}
