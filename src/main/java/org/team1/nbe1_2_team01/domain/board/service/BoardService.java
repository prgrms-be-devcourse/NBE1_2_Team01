package org.team1.nbe1_2_team01.domain.board.service;

import org.springframework.security.core.Authentication;
import org.team1.nbe1_2_team01.domain.board.controller.dto.BoardRequest;
import org.team1.nbe1_2_team01.domain.board.service.response.BoardDetailResponse;
import org.team1.nbe1_2_team01.domain.board.service.response.BoardResponse;

import java.util.List;

public interface BoardService {

    List<BoardResponse> getNoticeList(int page);

    String addNewNotice(BoardRequest noticeRequest, Authentication authentication);

    BoardDetailResponse getBoardDetailById(Long id);
}
