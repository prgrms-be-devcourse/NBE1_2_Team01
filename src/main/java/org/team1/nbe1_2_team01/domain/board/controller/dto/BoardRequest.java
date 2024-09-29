package org.team1.nbe1_2_team01.domain.board.controller.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.team1.nbe1_2_team01.domain.board.entity.Board;
import org.team1.nbe1_2_team01.domain.board.entity.Category;
import org.team1.nbe1_2_team01.domain.group.entity.Belonging;
import org.team1.nbe1_2_team01.domain.user.entity.User;

public record BoardRequest(
        Long belongingId,
        Long categoryId,

        @NotNull(message = "제목은 필수 입력값입니다.")
        @NotBlank(message = "내용을 입력해주세요.")
        String title,

        @NotNull(message = "내용은 필수 입력값입니다.")
        @NotBlank(message = "내용을 입력해주세요.")
        String content,
        boolean isNotice
) {
    public Board toEntity(User user, Category category, Belonging belonging) {
        return Board.builder()
                .user(user)
                .belonging(belonging)    //소속이 감이 안온다..
                .title(title)
                .content(content)
                .category(category)
                .build();
    }
}
