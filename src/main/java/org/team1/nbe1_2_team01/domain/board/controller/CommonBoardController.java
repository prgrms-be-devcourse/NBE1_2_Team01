package org.team1.nbe1_2_team01.domain.board.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.team1.nbe1_2_team01.domain.board.controller.dto.BoardRequest;
import org.team1.nbe1_2_team01.domain.board.service.BoardService;
import org.team1.nbe1_2_team01.domain.board.service.response.BoardDetailResponse;
import org.team1.nbe1_2_team01.domain.board.service.response.BoardResponse;

import java.util.List;

@RestController
@RequestMapping("/api/v1/board")
@RequiredArgsConstructor
public class CommonBoardController {

    private final BoardService boardService;

    /**
     * 공지사항 게시글 목록 조회 api
     * (게시글 목록 조회로 확장 해볼 수 있을 것 같음.)
     * @param page 페이지 번호
     * @return
     */
    @GetMapping
    public ResponseEntity<List<BoardResponse>> getCommonBoardList(@RequestParam(name = "p", defaultValue = "0") int page,
                                                                  @RequestParam(name = "type") String type) {
        return ResponseEntity.ok().body(boardService.getCommonBoardList(type, page));
    }

    /**
     * 공지사항 작성
     * (나중에 게시글 작성으로 확장해볼 수 있을 것 같음)
     * @param boardRequest 등록할 공지사항 정보
     * @return
     */
    @PostMapping
    public ResponseEntity<?> addCommonBoard(@RequestBody BoardRequest boardRequest) {
        return ResponseEntity.ok().body(boardService.addCommonBoard(boardRequest));
    }

    /**
     * 게시글 상세 조회
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<BoardDetailResponse> getBoardDetailById(@PathVariable Long id) {
        return ResponseEntity.ok().body(boardService.getBoardDetailById(id));
    }
}
