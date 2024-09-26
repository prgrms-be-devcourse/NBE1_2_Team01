package org.team1.nbe1_2_team01.domain.common.stub;

import java.time.LocalDateTime;
import org.team1.nbe1_2_team01.domain.attendance.service.port.DateTimeHolder;

public class FixedDateTimeHolder implements DateTimeHolder {

    private final LocalDateTime dateTime;

    public FixedDateTimeHolder(int hour, int minute) {
        this.dateTime = LocalDateTime.of(2024, 9, 25, hour, minute);
    }

    @Override
    public LocalDateTime getDate() {
        return dateTime;
    }
}
