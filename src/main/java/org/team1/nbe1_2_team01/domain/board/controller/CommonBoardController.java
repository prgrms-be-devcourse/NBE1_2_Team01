package org.team1.nbe1_2_team01.domain.board.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.team1.nbe1_2_team01.domain.board.controller.dto.BoardRequest;
import org.team1.nbe1_2_team01.domain.board.service.BoardService;
import org.team1.nbe1_2_team01.domain.board.service.CategoryService;
import org.team1.nbe1_2_team01.domain.board.service.response.BoardDetailResponse;
import org.team1.nbe1_2_team01.domain.board.service.response.BoardResponse;
import org.team1.nbe1_2_team01.domain.board.service.response.CategoryResponse;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CommonBoardController {

    private final BoardService boardService;

    /**
     * 공지사항 게시글 목록 조회 api
     * (게시글 목록 조회로 확장 해볼 수 있을 것 같음.)
     * @param page 페이지 번호
     * @return
     */
    @GetMapping("/notice")
    public ResponseEntity<List<BoardResponse>> getBoardList(@RequestParam(name = "p", defaultValue = "0") int page) {
        return ResponseEntity.ok().body(boardService.getNoticeList(page));
    }

    /**
     * 공지사항 작성
     * (나중에 게시글 작성으로 확장해볼 수 있을 것 같음)
     * @param noticeRequest 등록할 공지사항 정보
     * @return
     */
    @PostMapping("/notice")
    public ResponseEntity<?> addBoard(@RequestBody BoardRequest noticeRequest) {
        return ResponseEntity.ok().body(boardService.addNewNotice(noticeRequest));
    }

    /**
     * 공지사항 상세 조회
     * @param id
     * @return
     */
    @GetMapping("/board/{id}")
    public ResponseEntity<BoardDetailResponse> getBoardDetailById(@PathVariable Long id) {
        return ResponseEntity.ok().body(boardService.getBoardDetailById(id));
    }
}
