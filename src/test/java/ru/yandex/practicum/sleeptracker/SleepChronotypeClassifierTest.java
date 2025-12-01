package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SleepChronotypeClassifierTest {

    private SleepingSession createSession(String startStr, String endStr, SleepQuality quality) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");
        LocalDateTime start = LocalDateTime.parse(startStr, formatter);
        LocalDateTime end = LocalDateTime.parse(endStr, formatter);
        return new SleepingSession(start, end, quality);
    }

    @Test
    void classifiesAsOwlWhenMajorityAreOwlNights() {
        List<SleepingSession> sessions = List.of(
                createSession("01.10.25 23:30", "02.10.25 09:30", SleepQuality.GOOD),
                createSession("02.10.25 00:15", "02.10.25 10:00", SleepQuality.GOOD),
                createSession("03.10.25 22:00", "04.10.25 06:00", SleepQuality.NORMAL)
        );
        SleepAnalysisResult result = new SleepChronotypeClassifier().apply(sessions);
        assertEquals("Сова", result.getValue());
    }

    @Test
    void classifiesAsDoveWhenThereIsACoincidence() {
        List<SleepingSession> sessions = List.of(
                createSession("01.10.25 23:30", "02.10.25 09:30", SleepQuality.GOOD),
                createSession("02.10.25 21:00", "03.10.25 06:00", SleepQuality.GOOD)
        );
        SleepAnalysisResult result = new SleepChronotypeClassifier().apply(sessions);
        assertEquals("Голубь", result.getValue());
    }

    @Test
    void ignoresShortDaytimeSessions() {
        List<SleepingSession> sessions = List.of(
                createSession("01.10.25 14:00", "01.10.25 15:00", SleepQuality.NORMAL),
                createSession("02.10.25 21:30", "03.10.25 06:30", SleepQuality.GOOD)
        );
        SleepAnalysisResult result = new SleepChronotypeClassifier().apply(sessions);
        assertEquals("Жаворонок", result.getValue());
    }

    @Test
    void returnsInsufficientDataWhenNoLongSessions() {
        List<SleepingSession> sessions = List.of(
                createSession("01.10.25 14:00", "01.10.25 15:00", SleepQuality.NORMAL),
                createSession("02.10.25 13:00", "02.10.25 14:30", SleepQuality.NORMAL)
        );
        SleepAnalysisResult result = new SleepChronotypeClassifier().apply(sessions);
        assertEquals("Недостаточно данных", result.getValue());
    }
}