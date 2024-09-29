package org.team1.nbe1_2_team01.domain.board.service;

import org.team1.nbe1_2_team01.domain.board.controller.dto.*;
import org.team1.nbe1_2_team01.domain.board.service.response.BoardDetailResponse;
import org.team1.nbe1_2_team01.domain.board.service.response.BoardResponse;
import org.team1.nbe1_2_team01.domain.board.service.response.Message;

import java.util.List;

public interface BoardService {

    List<BoardResponse> getCommonBoardList(BoardListRequest boardListRequest);

    Message addBoard(BoardRequest noticeRequest);

    BoardDetailResponse getBoardDetailById(Long id);

    Message deleteBoardById(BoardDeleteRequest id);

    Message updateBoard(BoardUpdateRequest updateRequest);

    List<BoardResponse> getTeamBoardListByType(TeamBoardListRequest teamBoardListRequest);

}
