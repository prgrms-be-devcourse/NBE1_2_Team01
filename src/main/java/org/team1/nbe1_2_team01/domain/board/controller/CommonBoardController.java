package org.team1.nbe1_2_team01.domain.board.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.team1.nbe1_2_team01.domain.board.controller.dto.*;
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
     * 공통 게시글 목록 조회 api
     * (CommonType 유형에 따라 공지사항, 스터디 모집글 분리,
     * 추후에 CommonType을 Category에 넣어도 될 것 같음.)
     * @param boardListRequest
     * @return
     */
    @GetMapping
    public ResponseEntity<List<BoardResponse>> getCommonBoardList(
            @ModelAttribute BoardListRequest boardListRequest
    ) {
        return ResponseEntity.ok()
                .body(boardService.getCommonBoardList(boardListRequest));
    }

    /**
     * 팀 내에서 카테고리에 맞는 게시글 리스트 조회
     * 만약 categoryId가 null이면, 전체 카테고리 데이터 가져옴
     * @param request
     * @return
     */
    @GetMapping("/team")
    public ResponseEntity<List<BoardResponse>> getTeamBoardList(
            @ModelAttribute TeamBoardListRequest request
    ) {
        return ResponseEntity.ok()
                .body(boardService.getTeamBoardListByType(request));
    }

    /**
     * 공지사항 & 스터디 모집글 작성
     * (나중에 게시글 작성으로 확장해볼 수 있을 것 같음)
     * @param boardRequest 등록할 공지사항 정보
     * @return
     */
    @PostMapping
    public ResponseEntity<Message> addCommonBoard(
            @RequestBody @Valid BoardRequest boardRequest
    ) {
        return ResponseEntity.ok()
                .body(boardService.addBoard(boardRequest));
    }

    /**
     * 게시글 상세 조회
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<BoardDetailResponse> getBoardDetailById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok()
                .body(boardService.getBoardDetailById(id));
    }

    @PatchMapping
    public ResponseEntity<Message> updateBoard(
            @RequestBody @Valid BoardUpdateRequest updateRequest
    ) {
        URI uri = URI.create(BASE_URL + "/" + updateRequest.boardId());

        return ResponseEntity.created(uri)
                .body(boardService.updateBoard(updateRequest));
    }

    @DeleteMapping
    public ResponseEntity<Message> deleteBoard(
            @RequestBody BoardDeleteRequest deleteRequest
    ) {
        return ResponseEntity.ok()
                .body(boardService.deleteBoardById(deleteRequest));
    }
}
