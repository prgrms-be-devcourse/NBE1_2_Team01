package org.team1.nbe1_2_team01.domain.board.repository;

import org.team1.nbe1_2_team01.domain.board.constants.CommonBoardType;
import org.team1.nbe1_2_team01.domain.board.service.response.BoardDetailResponse;
import org.team1.nbe1_2_team01.domain.board.service.response.BoardResponse;

import java.util.List;
import java.util.Optional;

public interface CustomBoardRepository {

    List<BoardResponse> findAllCommonBoard(CommonBoardType type, long belongId, Long boardId);

    List<BoardResponse> findAllTeamBoardDByType(Long belongingId, Long categoryId, Long boardId);

    Optional<BoardDetailResponse> findBoardDetailExcludeComments(Long id);
}
