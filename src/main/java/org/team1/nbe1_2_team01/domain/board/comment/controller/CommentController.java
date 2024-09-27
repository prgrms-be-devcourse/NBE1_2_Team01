package org.team1.nbe1_2_team01.domain.board.comment.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.team1.nbe1_2_team01.domain.board.comment.controller.dto.CommentRequest;
import org.team1.nbe1_2_team01.domain.board.comment.service.CommentService;
import org.team1.nbe1_2_team01.domain.board.comment.service.response.CommentResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/comments")
public class CommentController {

    private final CommentService commentService;

    /**
     * 댓글 목록 조회
     * @param page 페이지 번호
     * @param boardId 게시글 번호
     * @return
     */
    @GetMapping
    public ResponseEntity<List<CommentResponse>> getComments(
            @RequestParam(name = "p") int page,
            @RequestParam("boardId") Long boardId
    ) {
        return ResponseEntity.ok()
                .body(commentService.getReviewsByPage(boardId, page));
    }

    /**
     * 댓글 삭제
     * @param commentId 댓글 번호
     * @return
     */
    @DeleteMapping
    public ResponseEntity<String> deleteComment(@RequestBody Long commentId) {
        return ResponseEntity.ok()
                .body(commentService.deleteById(commentId));
    }

    /**
     * 댓글 등록
     * @param commentRequest
     * @return
     */
    @PostMapping
    public ResponseEntity<String> addComment(@RequestBody CommentRequest commentRequest) {
        return ResponseEntity.ok()
                .body(commentService.addComment(commentRequest));
    }
}
