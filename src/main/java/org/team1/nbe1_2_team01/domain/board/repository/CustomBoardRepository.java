package org.team1.nbe1_2_team01.domain.board.repository;

import org.springframework.data.domain.Pageable;
import org.team1.nbe1_2_team01.domain.board.constants.CommonBoardType;
import org.team1.nbe1_2_team01.domain.board.service.response.BoardDetailResponse;
import org.team1.nbe1_2_team01.domain.board.service.response.BoardResponse;
import org.team1.nbe1_2_team01.domain.group.entity.Belonging;

import java.util.List;
import java.util.Optional;

public interface CustomBoardRepository {

    Optional<List<BoardResponse>> findAllCommonBoard(CommonBoardType type, long belongId, Pageable pageable);

    Optional<BoardDetailResponse> findBoardDetailExcludeComments(Long id);
}
