package org.team1.nbe1_2_team01.domain.board.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.team1.nbe1_2_team01.domain.board.controller.dto.BoardDeleteRequest;
import org.team1.nbe1_2_team01.domain.board.controller.dto.BoardRequest;
import org.team1.nbe1_2_team01.domain.board.controller.dto.BoardUpdateRequest;
import org.team1.nbe1_2_team01.domain.board.service.BoardService;
import org.team1.nbe1_2_team01.domain.board.service.response.BoardDetailResponse;
import org.team1.nbe1_2_team01.domain.board.service.response.BoardResponse;
import org.team1.nbe1_2_team01.domain.board.service.response.Message;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/board")
@RequiredArgsConstructor
public class CommonBoardController {

    private final BoardService boardService;
    private static final String BASE_URL = "/api/v1/board";

    /**
     * 게시글 목록 조회 api
     * (CommonType 유형에 따라 공지사항, 스터디 모집글 분리,
     * 추후에 CommonType을 Category에 넣어도 될 것 같음.)
     * @param page 페이지 번호
     * @return
     */
    @GetMapping
    public ResponseEntity<List<BoardResponse>> getCommonBoardList(@RequestParam(name = "p", defaultValue = "0") int page,
                                                                  @RequestParam(name = "type") String type) {
        return ResponseEntity.ok().body(boardService.getCommonBoardList(type, page));
    }

    /**
     * 공지사항 & 스터디 모집글 작성
     * (나중에 게시글 작성으로 확장해볼 수 있을 것 같음)
     * @param boardRequest 등록할 공지사항 정보
     * @return
     */
    @PostMapping
    public ResponseEntity<Message> addCommonBoard(@RequestBody @Valid BoardRequest boardRequest) {
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

    @PatchMapping
    public ResponseEntity<Message> updateBoard(@RequestBody @Valid BoardUpdateRequest updateRequest) {
        return ResponseEntity.created(URI.create("/"+updateRequest.getBoardId()))
                .body(boardService.updateBoard(updateRequest));
    }

    @PostMapping("/delete")
    public ResponseEntity<Message> deleteBoard(@RequestBody BoardDeleteRequest deleteRequest) {
        return ResponseEntity.ok().body(boardService.deleteBoardById(deleteRequest));
    }
}
