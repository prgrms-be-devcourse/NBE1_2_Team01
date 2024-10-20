package org.team1.nbe1_2_team01.domain.board.service.valid;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.team1.nbe1_2_team01.domain.board.entity.TeamBoard;
import org.team1.nbe1_2_team01.domain.user.entity.Role;
import org.team1.nbe1_2_team01.domain.user.entity.User;
import org.team1.nbe1_2_team01.global.exception.AppException;
import org.team1.nbe1_2_team01.global.util.ErrorCode;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TeamBoardValidator {

    public static void validateWriter(TeamBoard board, User currentUser) {
        User writer = board.getUser();
        boolean isWriter = writer.getUsername().equals(currentUser.getUsername());
        boolean isUser = currentUser.getRole().equals(Role.USER);

        if(!isWriter && isUser) {
            throw new AppException(ErrorCode.CANNOT_MANIPULATE_OTHERS_BOARD);
        }
    }
}
