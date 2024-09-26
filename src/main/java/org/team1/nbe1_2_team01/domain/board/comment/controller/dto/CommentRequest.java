package org.team1.nbe1_2_team01.domain.board.comment.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.team1.nbe1_2_team01.domain.board.entity.Board;
import org.team1.nbe1_2_team01.domain.board.entity.Comment;
import org.team1.nbe1_2_team01.domain.user.entity.User;

@Getter @ToString
public class CommentRequest {

    @NotNull
    private final Long boardId;

    @NotNull(message = "내용을 입력해주세요.")
    @NotBlank(message = "내용을 입력해주세요.")
    private final String content;

    public CommentRequest(Long boardId, String content) {
        this.boardId = boardId;
        this.content = content;
    }

    public Comment toEntity(User user, Board board) {
        return Comment.builder()
                .user(user)
                .board(board)
                .content(content)
                .build();
    }
}
