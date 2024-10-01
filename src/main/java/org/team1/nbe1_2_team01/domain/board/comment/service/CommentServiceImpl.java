package org.team1.nbe1_2_team01.domain.board.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.team1.nbe1_2_team01.domain.board.comment.controller.dto.CommentRequest;
import org.team1.nbe1_2_team01.domain.board.comment.repository.CommentRepository;
import org.team1.nbe1_2_team01.domain.board.comment.service.response.CommentResponse;
import org.team1.nbe1_2_team01.domain.board.entity.Board;
import org.team1.nbe1_2_team01.domain.board.entity.Comment;
import org.team1.nbe1_2_team01.domain.board.repository.BoardRepository;
import org.team1.nbe1_2_team01.domain.user.entity.User;
import org.team1.nbe1_2_team01.domain.user.repository.UserRepository;
import org.team1.nbe1_2_team01.global.util.Message;
import org.team1.nbe1_2_team01.global.exception.AppException;
import org.team1.nbe1_2_team01.global.util.ErrorCode;
import org.team1.nbe1_2_team01.global.util.SecurityUtil;

import java.util.ArrayList;
import java.util.List;

import static org.team1.nbe1_2_team01.domain.board.constants.MessageContent.*;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CommentResponse> getReviewsByPage(Long boardId, Long lastCommentId) {
        return commentRepository.getCommentsByBoardId(boardId, lastCommentId)
                .orElseGet(ArrayList::new);
    }

    @Override
    @Transactional
    public Message deleteById(Long id) {
        //삭제를 요청한 사용자가 자신의 댓글을 지우는 건지 확인하는 작업 필요.

        commentRepository.deleteById(id);
        String deleteCommentMessage = DELETE_COMMENT_COMPLETED.getMessage();
        return new Message(deleteCommentMessage);
    }

    @Override
    @Transactional
    public Message addComment(CommentRequest commentRequest) {
        String currentUsername = SecurityUtil.getCurrentUsername();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Board findBoard = boardRepository.findById(commentRequest.boardId())
                .orElseThrow(() -> new AppException(ErrorCode.BOARD_NOT_FOUND));

        Comment comment = commentRequest.toEntity(currentUser, findBoard);
        commentRepository.save(comment);

        String addCommentMessage = ADD_BOARD_COMPLETED.getMessage();
        return new Message(addCommentMessage);
    }
}
