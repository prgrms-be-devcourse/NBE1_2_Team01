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
import org.team1.nbe1_2_team01.global.util.Response;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private static final String BASE_URL = "/api/board";

    /**
     * 공통 게시글 목록 조회 api
     * (CommonType 유형에 따라 공지사항, 스터디 모집글 분리,
     * 추후에 CommonType을 Category에 넣어도 될 것 같음.)
     * @param boardListRequest
     * @return
     */
    @GetMapping
    public ResponseEntity<Response<List<BoardResponse>>> getCommonBoardList(
            @ModelAttribute BoardListRequest boardListRequest
    ) {
        List<BoardResponse> commonBoardList = boardService.getCommonBoardList(boardListRequest);
        return ResponseEntity.ok()
                .body(Response.success(commonBoardList));
    }

    /**
     * 팀 내에서 카테고리에 맞는 게시글 리스트 조회
     * 만약 categoryId가 null이면, 전체 카테고리 데이터 가져옴
     * @param request
     * @return
     */
    @GetMapping("/team")
    public ResponseEntity<Response<List<BoardResponse>>> getTeamBoardList(
            @ModelAttribute TeamBoardListRequest request
    ) {
        List<BoardResponse> boardList = boardService.getTeamBoardListByType(request);
        return ResponseEntity.ok()
                .body(Response.success(boardList));
    }

    /**
     * 공지사항 & 스터디 모집글 작성
     * (나중에 게시글 작성으로 확장해볼 수 있을 것 같음)
     * @param boardRequest 등록할 공지사항 정보
     * @return
     */
    @PostMapping
    public ResponseEntity<Response<Message>> addCommonBoard(
            @RequestBody @Valid BoardRequest boardRequest
    ) {
        Message message = boardService.addBoard(boardRequest);
        return ResponseEntity.ok()
                .body(Response.success(message));
    }

    /**
     * 게시글 상세 조회
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<Response<BoardDetailResponse>> getBoardDetailById(
            @PathVariable Long id
    ) {
        BoardDetailResponse data = boardService.getBoardDetailById(id);
        return ResponseEntity.ok()
                .body(Response.success(data));
    }

    @PatchMapping
    public ResponseEntity<Response<Message>> updateBoard(
            @RequestBody @Valid BoardUpdateRequest updateRequest
    ) {
        URI uri = URI.create(BASE_URL + "/" + updateRequest.boardId());
        Message message = boardService.updateBoard(updateRequest);

        return ResponseEntity.created(uri)
                .body(Response.success(message));
    }

    @DeleteMapping
    public ResponseEntity<Response<Message>> deleteBoard(
            @RequestBody @Valid BoardDeleteRequest deleteRequest
    ) {
        Message message = boardService.deleteBoardById(deleteRequest);
        return ResponseEntity.ok()
                .body(Response.success(message));
    }
}
