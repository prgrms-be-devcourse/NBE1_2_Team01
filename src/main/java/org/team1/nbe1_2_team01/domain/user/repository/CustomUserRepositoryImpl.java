package org.team1.nbe1_2_team01.domain.user.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.team1.nbe1_2_team01.domain.user.entity.QUser;
import org.team1.nbe1_2_team01.domain.user.entity.User;

import java.util.List;

@RequiredArgsConstructor
public class CustomUserRepositoryImpl implements CustomUserRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<User> findAllUsersByIdList(List<Long> userIds) {
        QUser user = QUser.user;
        List<User> users = queryFactory.selectFrom(user)
                .where(user.id.in(userIds))
                .fetch();

        return users;
    }
}
