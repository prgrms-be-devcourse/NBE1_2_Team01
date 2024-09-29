package org.team1.nbe1_2_team01.domain.board.controller.dto;

public record BoardDeleteRequest(
        Long boardId,
        boolean isNotice
) {

}
