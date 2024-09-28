package org.team1.nbe1_2_team01.domain.attendance.exception;

public class AccessDeniedException extends RuntimeException {

    public AccessDeniedException(String message) {
        super(message);
    }
}
