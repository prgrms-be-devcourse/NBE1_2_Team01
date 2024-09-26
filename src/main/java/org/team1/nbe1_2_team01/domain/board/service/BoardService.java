package org.team1.nbe1_2_team01.domain.board.service;

import org.team1.nbe1_2_team01.domain.board.controller.dto.BoardDeleteRequest;
import org.team1.nbe1_2_team01.domain.board.controller.dto.BoardRequest;
import org.team1.nbe1_2_team01.domain.board.service.response.BoardDetailResponse;
import org.team1.nbe1_2_team01.domain.board.service.response.BoardResponse;

import java.util.List;

public interface BoardService {

    List<BoardResponse> getCommonBoardList(String type, int page);

    String addCommonBoard(BoardRequest noticeRequest);

    BoardDetailResponse getBoardDetailById(Long id);

    String deleteBoardById(BoardDeleteRequest id);

}
