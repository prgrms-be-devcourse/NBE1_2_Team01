package org.team1.nbe1_2_team01.domain.group.service;

import static org.team1.nbe1_2_team01.global.util.ErrorCode.COURSE_AUTH_DENIED;
import static org.team1.nbe1_2_team01.global.util.ErrorCode.TEAM_AUTH_DENIED;
import static org.team1.nbe1_2_team01.global.util.ErrorCode.USER_NOT_FOUND;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.team1.nbe1_2_team01.domain.group.entity.Belonging;
import org.team1.nbe1_2_team01.domain.group.repository.BelongingRepository;
import org.team1.nbe1_2_team01.domain.group.service.response.GroupAuthResponse;
import org.team1.nbe1_2_team01.domain.user.entity.User;
import org.team1.nbe1_2_team01.domain.user.repository.UserRepository;
import org.team1.nbe1_2_team01.global.exception.AppException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GroupAuthService {

    private final UserRepository userRepository;
    private final BelongingRepository belongingRepository;

    /**
     * 코스에 소속되어 있는지 검증
     * @param course 접속중인 코스 이름
     * @param username 현재 접속 중인 유저 이름
     * @return 코스 체크 후 관계 테이블(소속)의 id 반환
     */
    public Long validateCourse(String course, String username) {
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(USER_NOT_FOUND));

        // course에 등록되어 있는지 확인
        List<Belonging> belongingsAsCourse = belongingRepository.findByCourseAndUserIsNotNull(course);
        belongingsAsCourse.stream()
                .filter(belonging -> isBelong(currentUser, belonging.getUser()))
                .findFirst()
                .orElseThrow(() -> new AppException(COURSE_AUTH_DENIED));

        // course를 나타내는 Belonging 레코드 Id 반환
        Belonging belongingAsCourse = belongingRepository.findByCourseAndUserIsNullAndTeamIsNull(course);
        return belongingAsCourse.getId();
    }

    /**
     * 팀에 소속되어 있는지 검증
     * @param username 현재 접속 중인 유저 이름
     * @param belongingTeamId 현재 접속중인 소속 이름
     * @return 팀 체크 후 관계 테이블(소속)의 id 반환
     */
    public GroupAuthResponse validateTeam(String username, Long belongingTeamId) {
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(USER_NOT_FOUND));

        Belonging belongingByTeam = belongingRepository.findById(belongingTeamId)
                .filter(belonging -> isBelong(currentUser, belonging.getUser())) // 현재 접속 유저가 Team에 속해있는지 확인
                .filter(Belonging::isOwner) // belongingTeamId가 팀장의 Id인지 확인
                .orElseThrow(() -> new AppException(TEAM_AUTH_DENIED));

        return GroupAuthResponse.from(belongingByTeam);
    }

    private boolean isBelong(User currentUser, User belongingUser) {
        return belongingUser.getId().equals(currentUser.getId());
    }
}
