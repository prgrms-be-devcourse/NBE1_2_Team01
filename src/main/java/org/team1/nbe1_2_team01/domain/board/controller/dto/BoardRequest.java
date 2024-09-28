package org.team1.nbe1_2_team01.domain.board.controller.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;
import org.team1.nbe1_2_team01.domain.board.entity.Board;
import org.team1.nbe1_2_team01.domain.board.entity.Category;
import org.team1.nbe1_2_team01.domain.group.entity.Belonging;
import org.team1.nbe1_2_team01.domain.user.entity.User;

@Getter @ToString
public class BoardRequest {

    private Long courseId;
    private Long teamId;
    private Long categoryId;

    @NotNull(message = "제목은 필수 입력값입니다.")
    @NotBlank(message = "내용을 입력해주세요.")
    private String title;

    @NotNull(message = "내용은 필수 입력값입니다.")
    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    private final boolean isNotice;

    BoardRequest(Long courseId, Long teamId, String title, String content, Long categoryId, boolean isNotice) {
        this.courseId = courseId;
        this.teamId = teamId;
        this.title = title;
        this.content = content;
        this.categoryId = categoryId;
        this.isNotice = isNotice;
    }

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
