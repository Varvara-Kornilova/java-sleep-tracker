package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BadSleepCountTest {

    private SleepingSession createSession(String startStr, String endStr, SleepQuality quality) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");
        LocalDateTime start = LocalDateTime.parse(startStr, formatter);
        LocalDateTime end = LocalDateTime.parse(endStr, formatter);
        return new SleepingSession(start, end, quality);
    }

    @Test
    void countsOnlySessionsWithBadQuality() {
        List<SleepingSession> sessions = List.of(
                createSession("01.10.25 23:00", "02.10.25 07:00", SleepQuality.GOOD),
                createSession("02.10.25 23:00", "03.10.25 06:00", SleepQuality.BAD),
                createSession("03.10.25 23:00", "04.10.25 07:00", SleepQuality.BAD)
        );
        SleepAnalysisResult result = new BadSleepCount().apply(sessions);
        assertEquals(2, result.getValue());
    }

    @Test
    void returnsZeroWhenNoBadSessions() {
        List<SleepingSession> sessions = List.of(
                createSession("01.10.25 23:00", "02.10.25 07:00", SleepQuality.GOOD)
        );
        SleepAnalysisResult result = new BadSleepCount().apply(sessions);
        assertEquals(0, result.getValue());
    }
}
