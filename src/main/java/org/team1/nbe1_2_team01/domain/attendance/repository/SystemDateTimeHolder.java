package org.team1.nbe1_2_team01.domain.attendance.repository;

import java.time.LocalDateTime;
import org.springframework.stereotype.Component;
import org.team1.nbe1_2_team01.domain.attendance.service.port.DateTimeHolder;

@Component
public class SystemDateTimeHolder implements DateTimeHolder {

    @Override
    public LocalDateTime getDate() {
        return LocalDateTime.now();
    }
}
