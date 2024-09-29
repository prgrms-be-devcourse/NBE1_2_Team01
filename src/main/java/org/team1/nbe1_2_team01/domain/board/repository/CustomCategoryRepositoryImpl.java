package org.team1.nbe1_2_team01.domain.board.repository;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.team1.nbe1_2_team01.domain.board.service.response.CategoryResponse;

import java.util.List;
import java.util.Optional;

import static org.team1.nbe1_2_team01.domain.board.entity.QCategory.category;
import static org.team1.nbe1_2_team01.domain.board.entity.QBoard.board;

@RequiredArgsConstructor
public class CustomCategoryRepositoryImpl implements CustomCategoryRepository {

    private final JPAQueryFactory queryFactory;
    @Override
    public Optional<List<CategoryResponse>> getAllCategoryByTeamId(Long teamId) {
        List<Tuple> fetchedList = queryFactory.select(
                        category.id,
                        category.name,
                        board.count().as("boardCount")
                )
                .from(category)
                .leftJoin(board).on(board.category.eq(category))
                .where(category.belonging.id.eq(teamId))
                .groupBy(category.id, category.name)
                .orderBy(category.id.asc())
                .fetch();

        List<CategoryResponse> responseList = fetchedList.stream().map(tuple -> CategoryResponse.of(
                tuple.get(category.id),
                tuple.get(category.name),
                Optional.ofNullable(tuple.get(board.count())).orElse(0L)
        )).toList();

        return Optional.of(responseList);
    }
}
