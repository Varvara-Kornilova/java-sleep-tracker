package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SleeplessNightsCountTest {

    private SleepingSession createSession(String startStr, String endStr, SleepQuality quality) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");
        LocalDateTime start = LocalDateTime.parse(startStr, formatter);
        LocalDateTime end = LocalDateTime.parse(endStr, formatter);
        return new SleepingSession(start, end, quality);
    }

    @Test
    void returnsZeroForEmptySessionList() {
        SleepAnalysisResult result = new SleeplessNightsCount().apply(List.of());
        assertEquals(0, result.getValue());
    }

    @Test
    void returnsZeroWhenAllNightsAreCovered() {
        List<SleepingSession> sessions = List.of(
                createSession("01.10.25 23:00", "02.10.25 07:00", SleepQuality.GOOD),
                createSession("02.10.25 23:30", "03.10.25 06:30", SleepQuality.NORMAL)
        );
        SleepAnalysisResult result = new SleeplessNightsCount().apply(sessions);
        assertEquals(0, result.getValue());
    }

    @Test
    void countsNightAsSleeplessWhenNotCovered() {
        List<SleepingSession> sessions = List.of(
                createSession("01.10.25 23:00", "02.10.25 07:00", SleepQuality.GOOD),
                createSession("03.10.25 23:00", "04.10.25 07:00", SleepQuality.GOOD)
        );
        SleepAnalysisResult result = new SleeplessNightsCount().apply(sessions);
        assertEquals(1, result.getValue());
    }

    @Test
    void countsDaytimeSessionsAsSleeplessNights() {
        List<SleepingSession> sessions = List.of(
                createSession("02.10.25 13:00", "02.10.25 15:00", SleepQuality.NORMAL),
                createSession("03.10.25 14:00", "03.10.25 16:00", SleepQuality.GOOD)
        );
        SleepAnalysisResult result = new SleeplessNightsCount().apply(sessions);
        assertEquals(2, result.getValue());
    }

    @Test
    void handlesCrossMonthBoundaryCorrectly() {
        List<SleepingSession> sessions = List.of(
                createSession("31.10.25 23:00", "01.11.25 07:00", SleepQuality.GOOD)
        );
        SleepAnalysisResult result = new SleeplessNightsCount().apply(sessions);
        assertEquals(0, result.getValue());
    }

    @Test
    void handlesSingleIntraDayNightSession() {
        List<SleepingSession> sessions = List.of(
                createSession("05.10.25 00:10", "05.10.25 06:20", SleepQuality.GOOD)
        );
        SleepAnalysisResult result = new SleeplessNightsCount().apply(sessions);
        assertEquals(0, result.getValue());
    }

    @Test
    void countsIntraDaySessionAsSleeplessIfOutsideNightWindow() {
        List<SleepingSession> sessions = List.of(
                createSession("05.10.25 07:00", "05.10.25 10:00", SleepQuality.NORMAL)
        );
        SleepAnalysisResult result = new SleeplessNightsCount().apply(sessions);
        assertEquals(1, result.getValue());
    }

    @Test
    void handlesMultipleSessionsEndingOnSameDay() {
        List<SleepingSession> sessions = List.of(
                createSession("02.10.25 23:00", "03.10.25 07:00", SleepQuality.GOOD),
                createSession("03.10.25 14:00", "03.10.25 15:00", SleepQuality.NORMAL)

        );
        SleepAnalysisResult result = new SleeplessNightsCount().apply(sessions);
        assertEquals(0, result.getValue());
    }
}
