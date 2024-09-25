package org.team1.nbe1_2_team01.domain.board.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.team1.nbe1_2_team01.domain.board.comment.repository.CommentRepository;
import org.team1.nbe1_2_team01.domain.board.comment.service.response.CommentResponse;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CommentResponse> getReviewsByPage(Long boardId, int page) {
        return commentRepository.getCommentsByBoardId(boardId, getPageable(page))
                .orElseGet(ArrayList::new);
    }

    @Override
    @Transactional
    public String deleteById(Long id) {
        //삭제를 요청한 사용자가 자신의 댓글을 지우는 건지 확인하는 작업 필요.

        commentRepository.deleteById(id);
        return "댓글이 삭제되었습니다.";
    }

    private Pageable getPageable(int page) {
        int PAGE_SIZE = 10;
        return PageRequest.of(page, PAGE_SIZE);
    }
}
