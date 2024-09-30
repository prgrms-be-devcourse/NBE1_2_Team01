package org.team1.nbe1_2_team01.domain.board.repository;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.team1.nbe1_2_team01.domain.board.constants.CommonBoardType;
import org.team1.nbe1_2_team01.domain.board.service.response.BoardDetailResponse;
import org.team1.nbe1_2_team01.domain.board.service.response.BoardResponse;
import org.team1.nbe1_2_team01.domain.user.entity.Role;
import org.team1.nbe1_2_team01.domain.user.entity.User;
import org.team1.nbe1_2_team01.global.util.SecurityUtil;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.team1.nbe1_2_team01.domain.board.entity.QBoard.board;
import static org.team1.nbe1_2_team01.domain.board.entity.QComment.comment;
import static org.team1.nbe1_2_team01.domain.user.entity.QUser.user;
import static org.team1.nbe1_2_team01.domain.board.entity.QCategory.category;

@RequiredArgsConstructor
public class CustomBoardRepositoryImpl implements CustomBoardRepository {

    private final JPAQueryFactory queryFactory;
    private static final int PAGE_SIZE = 10;

    @Override
    public Optional<List<BoardResponse>> findAllCommonBoard(
            CommonBoardType type,
            long belongingId,
            Long boardId
    ) {
        JPAQuery<Tuple> commonQuery = buildBoardQueryByType(type);
        setPagingStart(commonQuery, belongingId, boardId);

        List<Tuple> results = commonQuery
                .limit(PAGE_SIZE)
                .groupBy(board.id, board.title, user.username, board.createdAt)
                .orderBy(board.createdAt.desc())
                .fetch();

        List<BoardResponse> boards = getBoardResponses(results);
        return Optional.of(boards);
    }

    private JPAQuery<Tuple> buildBoardQueryByType(CommonBoardType type) {
        JPAQuery<Tuple> commonQuery = queryFactory
                .select(
                    board.id,
                    board.title,
                    user.name,
                    board.createdAt,
                    comment.count())
                .from(board)
                .innerJoin(user).on(board.user.eq(user))
                .leftJoin(comment).on(comment.board.eq(board));

        if(type.equals(CommonBoardType.NOTICE)) {
            return commonQuery.where(user.role.eq(Role.ADMIN));
        }

        return commonQuery.where(user.role.eq(Role.USER));
    }

    @Override
    public Optional<List<BoardResponse>> findAllTeamBoardDByType(
            Long belongingId,
            Long categoryId,
            Long boardId
    ) {
        JPAQuery<Tuple> teamBoardQuery = queryFactory
                .select(
                        board.id,
                        board.title,
                        user.name,
                        category.name,
                        board.createdAt,
                        comment.count())
                .from(board)
                .innerJoin(user).on(board.user.eq(user))
                .innerJoin(category).on(board.category.eq(category))
                .leftJoin(comment).on(comment.board.eq(board));

        setConditionByCategoryId(teamBoardQuery, categoryId);
        setPagingStart(teamBoardQuery, belongingId, boardId);

        List<Tuple> results = teamBoardQuery
                .limit(PAGE_SIZE)
                .groupBy(board.id, board.title, user.name, category.name, board.createdAt)
                .orderBy(board.createdAt.desc())
                .fetch();

        List<BoardResponse> boards = getBoardResponses(results);

        return Optional.of(boards);
    }

    @Override
    public Optional<BoardDetailResponse> findBoardDetailExcludeComments(Long id) {
        Tuple tuple = queryFactory.select(
                        board.id,
                        board.title,
                        board.content,
                        user.name,
                        board.createdAt,
                        board.user.id
                )
                .from(board)
                .innerJoin(user).on(board.user.eq(user))
                .where(board.id.eq(id))
                .fetchOne();

        if (tuple == null) {
            return Optional.empty(); // 결과가 없을 경우 빈 Optional 반환
        }

        String currentUsername = SecurityUtil.getCurrentUsername();
        User currentUser = queryFactory.selectFrom(user).where(user.username.eq(currentUsername)).fetchOne();


        BoardDetailResponse boardDetailResponse = BoardDetailResponse.of(
                tuple.get(board.id),
                tuple.get(board.title),
                tuple.get(board.content),
                tuple.get(user.name),
                tuple.get(board.createdAt),
                Objects.equals(currentUser.getRole(), Role.ADMIN),
                Objects.equals(tuple.get(board.user.id), currentUser.getId())
        );

        return Optional.ofNullable(boardDetailResponse);
    }

    private void setPagingStart(JPAQuery<Tuple> commonQuery, long belongingId, Long boardId) {
        commonQuery.where(board.belonging.id.eq(belongingId));

        if (boardId != null) {
            commonQuery.where(board.id.lt(boardId));
        }
    }

    private void setConditionByCategoryId(JPAQuery<Tuple> teamBoardQuery, Long categoryId) {
        if(categoryId != null) {
            teamBoardQuery.where(board.category.id.eq(categoryId));
        }
    }

    private List<BoardResponse> getBoardResponses(List<Tuple> query) {
        return query.stream()
                .map(tuple -> BoardResponse.of(
                        tuple.get(board.id),
                        tuple.get(board.title),
                        tuple.get(user.name),
                        tuple.get(category.name),
                        tuple.get(board.createdAt),
                        tuple.get(comment.count())
                ))
                .toList();
    }
}
