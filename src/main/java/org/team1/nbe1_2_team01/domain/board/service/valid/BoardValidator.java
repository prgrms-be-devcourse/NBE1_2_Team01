package org.team1.nbe1_2_team01.domain.board.service.valid;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.team1.nbe1_2_team01.domain.board.entity.Board;
import org.team1.nbe1_2_team01.domain.user.entity.User;
import org.team1.nbe1_2_team01.global.exception.AppException;
import org.team1.nbe1_2_team01.global.util.ErrorCode;
import org.team1.nbe1_2_team01.global.util.SecurityUtil;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BoardValidator {

    public static void validateWriter(Board board) {
        User user = board.getUser();
        String currentUsername = SecurityUtil.getCurrentUsername();

        if(!user.getUsername().equals(currentUsername)) {
            throw new AppException(ErrorCode.CANNOT_MANIPULATE_OTHERS_BOARD);
        }
    }

    public static void validateAdminForNotice(User user, boolean isNotice) {
        if(isNotice) {
            SecurityUtil.validateAdmin(user);
        }
    }
}
