package org.team1.nbe1_2_team01.domain.board.comment.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CommentRequest {

    @NotNull(message = "내용을 입력해주세요.")
    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

}
