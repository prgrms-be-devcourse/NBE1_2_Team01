package org.team1.nbe1_2_team01.domain.board.controller.dto;


import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.team1.nbe1_2_team01.domain.board.entity.Board;
import org.team1.nbe1_2_team01.domain.user.entity.User;

@Getter @ToString
public class NoticeRequest {

    @NotNull(message = "제목은 필수 입력값입니다.")
    private String title;

    @NotNull(message = "내용은 필수 입력값입니다.")
    private String content;

    @Builder
    private NoticeRequest(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public Board toEntity(User user) {
        return Board.builder()
                .user(user)
                .belonging(null)    //소속이 감이 안온다..
                .title(title)
                .content(content)
                .build();
    }
}
