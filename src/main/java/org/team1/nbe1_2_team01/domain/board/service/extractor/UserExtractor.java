package org.team1.nbe1_2_team01.domain.board.service.extractor;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserExtractor {

    /**
     * 사용자 정보를 추출하는 메서드
     * @return
     */
    public static Object extract() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getPrincipal();
    }
}
