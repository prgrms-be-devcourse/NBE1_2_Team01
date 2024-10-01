package org.team1.nbe1_2_team01.domain.board.comment.repository;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.team1.nbe1_2_team01.domain.board.comment.service.response.CommentResponse;

import java.util.List;
import java.util.Optional;

import static org.team1.nbe1_2_team01.domain.board.entity.QComment.comment;
import static org.team1.nbe1_2_team01.domain.user.entity.QUser.user;

@RequiredArgsConstructor
public class CustomCommentRepositoryImpl implements CustomCommentRepository {

    private final JPAQueryFactory queryFactory;
    private static final int PAGE_SIZE = 10;

    @Override
    public Optional<List<CommentResponse>> getCommentsByBoardId(Long boardId, Long lastCommentId) {
        JPAQuery<Tuple> initialQuery = queryFactory.select(
                        comment.id,
                        user.username,
                        comment.content,
                        comment.createdAt
                )
                .from(comment)
                .innerJoin(user).on(comment.user.eq(user))
                .where(comment.board.id.eq(boardId));

        setPagingStart(initialQuery, lastCommentId);

        List<Tuple> tuples = initialQuery
                .limit(PAGE_SIZE)
                .orderBy(comment.createdAt.desc(), comment.id.desc())
                .fetch();

        List<CommentResponse> comments = convertToResponses(tuples);

        return Optional.of(comments);
    }

    private static List<CommentResponse> convertToResponses(List<Tuple> tuples) {
        return tuples.stream().map(tuple -> CommentResponse.of(
                tuple.get(comment.id),
                tuple.get(user.username),
                tuple.get(comment.content),
                tuple.get(comment.createdAt)
        )).toList();
    }

    private void setPagingStart(JPAQuery<Tuple> query, Long commentId) {
        if(commentId != null) {
            query.where(comment.id.lt(commentId));
        }
    }
}
