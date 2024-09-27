package org.team1.nbe1_2_team01.domain.board.service.extractor;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserExtractor {

    /**
     * 사용자 정보를 추출하는 메서드
     * (임시로 Object로 하자.)
     * @return
     */
    public static Object extract() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        log.info("principal = {}", principal);  //값이 어떻게 오는 지 확인용 로그
        return principal;
    }
}
