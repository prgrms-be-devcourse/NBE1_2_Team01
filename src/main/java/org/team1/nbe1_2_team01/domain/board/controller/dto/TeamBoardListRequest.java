package org.team1.nbe1_2_team01.domain.board.controller.dto;

public record TeamBoardListRequest(
        Long teamId,
        Long categoryId,
        Long boardId
) {
}
