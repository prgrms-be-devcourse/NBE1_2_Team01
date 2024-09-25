package org.team1.nbe1_2_team01.domain.board.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.team1.nbe1_2_team01.domain.board.service.response.BoardDetailResponse;
import org.team1.nbe1_2_team01.domain.board.service.response.BoardResponse;
import org.team1.nbe1_2_team01.domain.user.entity.Role;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.team1.nbe1_2_team01.domain.board.entity.QBoard.board;
import static org.team1.nbe1_2_team01.domain.board.entity.QCategory.category;
import static org.team1.nbe1_2_team01.domain.board.entity.QComment.comment;
import static org.team1.nbe1_2_team01.domain.user.entity.QUser.user;


@RequiredArgsConstructor
public class CustomBoardRepositoryImpl implements CustomBoardRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<List<BoardResponse>> findAllNotices(Pageable pageable) {
        //inner join와 left join의 차이? 일단 두고 보자. + 나중을 위해 쿼리 튜닝 필요
        //Querydsl로 데이터를 Tuple로 가져옴
        List<Tuple> results = queryFactory
                .select(board.id, board.title, user.username, board.createdAt, comment.count().nullif(0L))
                .from(board)
                .innerJoin(user).on(board.user.eq(user))
                .leftJoin(comment).on(comment.board.eq(board))
                .where(user.role.eq(Role.ADMIN))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .groupBy(board.id, board.title, user.username, board.createdAt)
                .orderBy(board.createdAt.desc())
                .fetch();

        List<BoardResponse> notices = results.stream()
                .map(tuple -> BoardResponse.of(
                        tuple.get(board.id),
                        tuple.get(board.title),
                        tuple.get(user.username),
                        null, // 카테고리 이름은 지금 null로 설정
                        tuple.get(board.createdAt),
                        tuple.get(comment.count())
                ))
                .collect(Collectors.toList());

        return Optional.of(notices);
    }

    @Override
    public Optional<BoardDetailResponse> findBoardDetailExcludeComments(Long id) {
        BoardDetailResponse boardDetailResponse = queryFactory.select(Projections.constructor(BoardDetailResponse.class,
                        board.id,
                        board.title,
                        board.content,
                        user.username,
                        board.createdAt))
                .from(board)
                .innerJoin(user).on(board.user.eq(user))
                .where(board.id.eq(id))
                .fetchOne();
        return Optional.ofNullable(boardDetailResponse);
    }
}
