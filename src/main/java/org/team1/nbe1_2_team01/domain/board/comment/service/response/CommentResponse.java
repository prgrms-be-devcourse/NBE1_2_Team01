package org.team1.nbe1_2_team01.domain.board.comment.service.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.team1.nbe1_2_team01.domain.board.service.converter.DateTimeToStringConverter;

import java.time.LocalDateTime;

@Getter @ToString
public class CommentResponse {

    private final Long id;
    private final String writer;
    private final String content;
    private final String createdAt;
    private boolean isAdmin;
    private boolean isMine;

    @Builder
    private CommentResponse(Long id, String writer, String content, LocalDateTime createdAt) {
        this.id = id;
        this.writer = writer;
        this.content = content;
        this.createdAt = DateTimeToStringConverter.convert(createdAt);
    }

    public static CommentResponse of(Long id, String writer, String content, LocalDateTime createdAt) {
        return CommentResponse.builder()
                .id(id)
                .writer(writer)
                .content(content)
                .createdAt(createdAt)
                .build();
    }
}
