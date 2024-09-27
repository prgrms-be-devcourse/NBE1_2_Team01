package org.team1.nbe1_2_team01.domain.common.stub;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.team1.nbe1_2_team01.domain.attendance.service.port.DateTimeHolder;

public class FixedDateTimeHolder implements DateTimeHolder {

    private final LocalDateTime dateTime;

    public FixedDateTimeHolder(int hour, int minute) {
        this.dateTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(hour, minute));
    }

    @Override
    public LocalDateTime getDate() {
        return dateTime;
    }
}
