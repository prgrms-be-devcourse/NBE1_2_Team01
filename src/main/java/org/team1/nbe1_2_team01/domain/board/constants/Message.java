package org.team1.nbe1_2_team01.domain.board.constants;

import lombok.Getter;

@Getter
public enum Message {

    ADD_BOARD_COMPLETED("을 등록했습니다"),
    DELETE_BOARD_COMPLETED("이 삭제되었습니다."),

    ADD_COMMENT_COMPLETED("댓글을 등록했습니다."),
    DELETE_COMMENT_COMPLETED("댓글이 삭제되었습니다."),

    NOT_EXIST_BOARD("해당 게시글이 존재하지 않습니다.");

    private final String message;


    Message(String message) {
        this.message = message;
    }

    private static String getBoardType(boolean isNotice) {
        if(isNotice) {
            return "공지사항";
        }
        return "게시글";
    }

    public static String getAddMessage(boolean isNotice) {
       return getBoardType(isNotice) + ADD_BOARD_COMPLETED.getMessage();
    }

    public static String getDeleteMessage(boolean isNotice) {
        return getBoardType(isNotice) + DELETE_BOARD_COMPLETED.getMessage();
    }

}
