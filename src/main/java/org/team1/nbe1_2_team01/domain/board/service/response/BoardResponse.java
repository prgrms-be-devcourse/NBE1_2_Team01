package org.team1.nbe1_2_team01.domain.board.service.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.team1.nbe1_2_team01.domain.board.service.converter.DateTimeToStringConverter;

import java.time.LocalDateTime;

@Getter @ToString
public class BoardResponse {

    private Long id;
    private String title;
    private String writer;
    private String categoryName;
    private String createdAt;
    private Long commentCount;

    @Builder
    private BoardResponse(Long id,
                          String title,
                          String writer,
                          String categoryName,
                          LocalDateTime createdAt,
                          Long commentCount) {
        this.id = id;
        this.title = title;
        this.writer = writer;
        this.categoryName = categoryName;
        this.createdAt = DateTimeToStringConverter.convert(createdAt);
        this.commentCount = commentCount;
    }

    public static BoardResponse of(Long id,
                                   String title,
                                   String writer,
                                   String categoryName,
                                   LocalDateTime createdAt,
                                   Long commentCount) {
        return BoardResponse.builder()
                .id(id)
                .title(title)
                .writer(writer)
                .categoryName(categoryName)
                .createdAt(createdAt)
                .commentCount(commentCount)
                .build();
    }
}
