package org.team1.nbe1_2_team01.domain.board.comment.repository;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.team1.nbe1_2_team01.domain.board.comment.service.response.CommentResponse;

import java.util.List;
import java.util.Optional;

import static org.team1.nbe1_2_team01.domain.board.entity.QComment.comment;
import static org.team1.nbe1_2_team01.domain.user.entity.QUser.user;

@RequiredArgsConstructor
public class CustomCommentRepositoryImpl implements CustomCommentRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<List<CommentResponse>> getCommentsByBoardId(Long boardId, Pageable pageable) {
        List<Tuple> tuples = queryFactory.select(
                        comment.id,
                        user.username,
                        comment.content,
                        comment.createdAt
                )
                .from(comment)
                .innerJoin(user).on(comment.user.eq(user))
                .where(comment.board.id.eq(boardId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(comment.createdAt.desc())
                .fetch();

        List<CommentResponse> comments = tuples.stream().map(tuple -> CommentResponse.of(
                tuple.get(comment.id),
                tuple.get(user.username),
                tuple.get(comment.content),
                tuple.get(comment.createdAt)
        )).toList();

        return Optional.of(comments);
    }
}
