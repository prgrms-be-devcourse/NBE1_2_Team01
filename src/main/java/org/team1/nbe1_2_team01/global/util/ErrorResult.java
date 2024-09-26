package org.team1.nbe1_2_team01.global.util;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ErrorResult {

    private final int status;
    private final Object errors;
    private final LocalDateTime occurrenceTime;

    public ErrorResult(int status, Object errors) {
        this.status = status;
        this.errors = errors;
        this.occurrenceTime = LocalDateTime.now();
    }
}
