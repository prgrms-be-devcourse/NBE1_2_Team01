package org.team1.nbe1_2_team01.domain.board.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BoardUpdateRequest(
        @NotNull(message = "게시글 수정 실패했습니다.")
        Long boardId,

        @NotNull(message = "제목은 필수 입력값입니다.")
        @NotBlank(message = "내용을 입력해주세요.")
        String title,

        @NotNull(message = "내용은 필수 입력값입니다.")
        @NotBlank(message = "내용을 입력해주세요.")
        String content,

        @NotNull(message = "게시글 수정 실패했습니다.")
        Boolean isNotice
) {

}