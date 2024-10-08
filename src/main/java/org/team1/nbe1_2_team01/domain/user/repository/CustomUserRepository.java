package org.team1.nbe1_2_team01.domain.user.repository;

import org.team1.nbe1_2_team01.domain.user.entity.User;

import java.util.List;
import java.util.Optional;

public interface CustomUserRepository {

    List<User> findAllUsersByIdList(List<Long> userIds);

    List<User> findUsersAndAdminsByCourseId(Long courseId);
}
