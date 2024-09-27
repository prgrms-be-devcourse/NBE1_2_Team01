package org.team1.nbe1_2_team01.domain.board.comment.repository;

import org.springframework.data.domain.Pageable;
import org.team1.nbe1_2_team01.domain.board.comment.service.response.CommentResponse;

import java.util.List;
import java.util.Optional;

public interface CustomCommentRepository {

    Optional<List<CommentResponse>> getCommentsByBoardId(Long boardId, Pageable pageable);
}
