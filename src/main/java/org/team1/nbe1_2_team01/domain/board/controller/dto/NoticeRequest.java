package org.team1.nbe1_2_team01.domain.board.controller.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.team1.nbe1_2_team01.domain.board.entity.Board;
import org.team1.nbe1_2_team01.domain.user.entity.User;

@Getter @ToString
public class NoticeRequest {

    private String title;
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
