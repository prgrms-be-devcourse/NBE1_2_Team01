package org.team1.nbe1_2_team01.domain.board.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.team1.nbe1_2_team01.domain.board.controller.dto.NoticeRequest;
import org.team1.nbe1_2_team01.domain.board.service.BoardService;
import org.team1.nbe1_2_team01.domain.board.service.response.BoardDetailResponse;
import org.team1.nbe1_2_team01.domain.board.service.response.BoardResponse;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CommonBoardController {

    private final BoardService boardService;

    /**
     * 공지사항 게시글 목록 조회 api
     * @param page 페이지 번호
     * @return
     */
    @GetMapping("/notice")
    public ResponseEntity<List<BoardResponse>> getNoticeList(@RequestParam(name = "p", defaultValue = "0") int page) {
        return ResponseEntity.ok().body(boardService.getNoticeList(page));
    }

    /**
     * 공지사항 작성
     * @param noticeRequest 등록할 공지사항 정보
     * @return
     */
    @PostMapping("/notice")
    public ResponseEntity<?> addNewNotice(@RequestBody NoticeRequest noticeRequest) {
        return ResponseEntity.ok().body(boardService.addNewNotice(noticeRequest, getAuthentication()));
    }

    /**
     * 공지사항 상세 조회
     * @param id
     * @return
     */
    @GetMapping("/board/{id}")
    public ResponseEntity<BoardDetailResponse> getBoardDetail(@PathVariable Long id) {
        return ResponseEntity.ok().body(boardService.getBoardDetail(id, getAuthentication()));
    }

    //분리 필요
    private static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
