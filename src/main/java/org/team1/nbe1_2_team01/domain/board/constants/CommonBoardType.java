package org.team1.nbe1_2_team01.domain.board.constants;

import org.team1.nbe1_2_team01.domain.board.exception.NotFoundTypeException;

public enum CommonBoardType {

    NOTICE("notice"),
    STUDY("study");

    private final String type;

    CommonBoardType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static CommonBoardType getType(String type) {
        for (CommonBoardType enumType : values()) {
            if(enumType.getType().equals(type)) {
                return enumType;
            }
        }
        throw new NotFoundTypeException("게시글이 존재하지 않습니다.");
    }
}
