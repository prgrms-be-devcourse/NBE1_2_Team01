package org.team1.nbe1_2_team01.domain.board.controller.dto;

public record TeamBoardListRequest(
        Long belongingId,
        Long categoryId,
        Long boardId
) {
}
