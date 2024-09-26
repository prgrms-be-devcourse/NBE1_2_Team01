package org.team1.nbe1_2_team01.domain.board.controller.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.team1.nbe1_2_team01.domain.board.entity.Board;
import org.team1.nbe1_2_team01.domain.board.entity.Category;
import org.team1.nbe1_2_team01.domain.user.entity.User;

@Getter @ToString
public class BoardRequest {

    private Long categoryId;

    @NotNull(message = "제목은 필수 입력값입니다.")
    @NotBlank(message = "내용을 입력해주세요.")
    private String title;

    @NotNull(message = "내용은 필수 입력값입니다.")
    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    private BoardRequest(String title, String content, Long categoryId) {
        this.title = title;
        this.content = content;
        this.categoryId = categoryId;
    }

    public Board toEntity(User user, Category category) {
        return Board.builder()
                .user(user)
                .belonging(null)    //소속이 감이 안온다..
                .category(category)
                .title(title)
                .content(content)
                .build();
    }
}
