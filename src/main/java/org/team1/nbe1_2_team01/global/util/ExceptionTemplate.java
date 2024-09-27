package org.team1.nbe1_2_team01.global.util;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ExceptionTemplate {

    private final int status;
    private final Object errors;
    private final LocalDateTime occurrenceTime;

    public ExceptionTemplate(int status, Object errors) {
        this.status = status;
        this.errors = errors;
        this.occurrenceTime = LocalDateTime.now();
    }
}
