package org.team1.nbe1_2_team01.domain.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.team1.nbe1_2_team01.domain.board.comment.service.CommentService;
import org.team1.nbe1_2_team01.domain.board.comment.service.response.CommentResponse;
import org.team1.nbe1_2_team01.domain.board.controller.dto.BoardRequest;
import org.team1.nbe1_2_team01.domain.board.entity.Board;
import org.team1.nbe1_2_team01.domain.board.exception.NotFoundBoardException;
import org.team1.nbe1_2_team01.domain.board.repository.BoardRepository;
import org.team1.nbe1_2_team01.domain.board.service.response.BoardDetailResponse;
import org.team1.nbe1_2_team01.domain.board.service.response.BoardResponse;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final CommentService commentService;

    @Override
    @Transactional(readOnly = true)
    public List<BoardResponse> getNoticeList(int page) {
        //security 쪽에서 검증을 해줄진 모르겠지만, 사용자의 정보를 받아와야 할 수도 있음

        //pageable 생성
        Pageable pageable = getPageable(page);
        //쿼리로 데이터 가져오기.
        return boardRepository.findAllNotices(pageable)
                .orElseGet(ArrayList::new);
    }

    @Override
    public String addNewNotice(BoardRequest noticeRequest, Authentication authentication) {
        //해당 사용자가 관리자의 권한이 있는 지 검사, 관리자가 아니면 예외를 던진다.
        authentication.getPrincipal();

        //사용자의 정보와 소속을 가져와야겠네

        //공지사항을 저장한다.
        Board notice = Board.builder()
                .user(null)
                .belonging(null)
                .title(noticeRequest.getTitle())
                .content(noticeRequest.getContent())
                .build();

        boardRepository.save(notice);
        return "공지사항을 등록했습니다.";
    }

    @Override
    @Transactional(readOnly = true)
    public BoardDetailResponse getBoardDetail(Long id, Authentication authentication) {
        //게시글 정보를 가져와.
        BoardDetailResponse boardDetailResponse = boardRepository.findBoardDetailExcludeComments(id)
                .orElseThrow(() -> new NotFoundBoardException("게시글이 존재하지 않습니다."));

        //게시글에 해당하는 리뷰를 가져와
        List<CommentResponse> comments = commentService.getReviewsByPage(id, 0);
        boardDetailResponse.addComments(comments);

        return boardDetailResponse;
    }

    private Pageable getPageable(int page) {
        int PAGE_SIZE = 10;
        return PageRequest.of(page, PAGE_SIZE);
    }
}
