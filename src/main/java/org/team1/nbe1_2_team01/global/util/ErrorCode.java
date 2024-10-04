package org.team1.nbe1_2_team01.global.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;


@RequiredArgsConstructor
public enum ErrorCode {
    //attendance
    ATTENDANCE_NOT_FOUND(HttpStatus.NOT_FOUND, "출결 요청을 찾을 수 없습니다."),
    REQUEST_ALREADY_APPROVED(HttpStatus.CONFLICT, "이미 승인되었습니다."),
    REQUEST_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 오늘 등록된 요청이 있습니다"),
    ATTENDANCE_TIME_OUT_OF_RANGE(HttpStatus.BAD_REQUEST, "출결 이슈는 9시에서 17시 59분 사이여야 합니다."),
    ATTENDANCE_TIME_END_BEFORE_START(HttpStatus.BAD_REQUEST, "출결 시작 시간이 끝 시간보다 나중일 수 없습니다."),
    ATTENDANCE_ACCESS_DENIED(HttpStatus.FORBIDDEN, "출결 이슈에 접근할 수 없습니다."),

    //board
    BOARD_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 게시글이 존재하지 않습니다."),
    CANNOT_MANIPULATE_OTHERS_BOARD(HttpStatus.FORBIDDEN, "자신이 작성한 게시글이 아닙니다."),
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 카테고리입니다."),
    CATEGORY_NOT_DELETED(HttpStatus.NOT_FOUND, "카테고리가 삭제되지 않았습니다."),
    BOARD_NOT_UPDATED(HttpStatus.NOT_MODIFIED, "게시글이 수정되지 않았습니다."),

    //comment
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 댓글이 존재하지 않습니다."),
    CANNOT_MANIPULATE_OTHERS_COMMENT(HttpStatus.FORBIDDEN, "자신이 작성한 댓글이 아닙니다."),



    //chat
    CHANEL_NOT_FOUND(HttpStatus.NOT_FOUND, "채널을 찾을 수 없습니다"),
    NO_PARTICIPANTS(HttpStatus.NOT_FOUND, "참여자가 없음"),
    INVITER_NOT_FOUND(HttpStatus.NOT_FOUND, "초대자를 찾을 수 없음."),
    NOT_CHANEL_CREATOR(HttpStatus.FORBIDDEN, "채널 생성자만이 초대를 할 수 있습니다."),
    PARTICIPANTS_NOT_FOUND(HttpStatus.NOT_FOUND, "참여자 userId: %d가 channelId: %d에 존재하지 않습니다."),


    //calender

    //group
    MISSING_TEAM_TYPE(HttpStatus.BAD_REQUEST, "팀 타입이 필요합니다."),
    COURSE_NOT_FOUND(HttpStatus.NOT_FOUND, "코스가 존재하지 않습니다."),
    COURSE_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 코스명입니다."),
    TEAM_NOT_FOUND(HttpStatus.NOT_FOUND, "팀이 존재하지 않습니다."),
    TEAM_NOT_WAITING(HttpStatus.BAD_REQUEST, "승인 대기 중인 팀이 아닙니다."),
    BELONGING_NOT_FOUND(HttpStatus.NOT_FOUND, "소속이 존재하지 않습니다"),
    NOT_TEAM_LEADER(HttpStatus.UNAUTHORIZED, "팀장만 이용 가능합니다."),
    TEAM_NOT_UPDATED(HttpStatus.NOT_MODIFIED, "팀이 수정되지 않았습니다"),
    TEAM_EXISTING_MEMBER(HttpStatus.CONFLICT, "이미 해당 팀에 존재하는 회원입니다."),
    LEADER_BELONGING_NOT_FOUND(HttpStatus.NOT_FOUND, "팀장 소속 정보를 찾아오는 중 오류가 발생했습니다."),
    CANNOT_DELETE_LEADER(HttpStatus.BAD_REQUEST, "팀장을 삭제할 수는 없습니다. 삭제하시려면 팀을 삭제하세요."),

    //user
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 유저입니다."),
    NOT_ADMIN_USER(HttpStatus.UNAUTHORIZED, "관리자만 이용가능합니다."),
    USERNAME_ALREADY_EXISTS(HttpStatus.CONFLICT, "%s는 이미 존재하는 아이디입니다."),
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "%s는 이미 존재하는 이메일 입니다.")
    ;

    @Getter
    private final HttpStatus status;
    private final String message;
    private String formattedMessage;    //동적 생성된 메시지를 저장하는 변수

    public ErrorCode withArgs(Object... args) {
        this.formattedMessage = String.format(this.message, args);
        return this;
    }

    public String getMessage() {
        if(formattedMessage == null) return message;
        return formattedMessage;
    }
}
