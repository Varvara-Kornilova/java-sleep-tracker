package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class SleepTrackerAppTest {

    private SleepingSession createSession(String start, String end, SleepQuality quality) {
        DateTimeFormatter f = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");
        return new SleepingSession(
                LocalDateTime.parse(start, f),
                LocalDateTime.parse(end, f),
                quality
        );
    }

    @Test
    void analyzeAll_returnsExactlySevenResultsInExpectedOrder() {
        SleepTrackerApp app = new SleepTrackerApp();
        List<SleepingSession> sessions = List.of(
                createSession("01.10.25 23:00", "02.10.25 07:00", SleepQuality.GOOD),
                createSession("02.10.25 23:30", "03.10.25 06:00", SleepQuality.BAD)
        );

        List<SleepAnalysisResult> results = app.analyzeAll(sessions);

        assertEquals(7, results.size(), "Должно быть ровно 7 анализаторов");

        assertEquals("Всего сессий сна", results.get(0).getDescription());
        assertEquals("Минимальная длительность сна (мин)", results.get(1).getDescription());
        assertEquals("Максимальная длительность сна (мин)", results.get(2).getDescription());
        assertEquals("Средняя длительность сна (мин)", results.get(3).getDescription());
        assertEquals("Количество плохих ночей (BAD)", results.get(4).getDescription());
        assertEquals("Количество бессонных ночей", results.get(5).getDescription());
        assertEquals("Хронотип", results.get(6).getDescription());
    }

    @Test
    void analyzeAll_handlesEmptySessionListGracefully() {
        SleepTrackerApp app = new SleepTrackerApp();
        List<SleepAnalysisResult> results = app.analyzeAll(List.of());

        assertEquals(7, results.size());
        assertEquals(0, results.get(0).getValue()); // TotalSessionsCount
        assertEquals(0, results.get(1).getValue()); // Min
        assertEquals(0, results.get(2).getValue()); // Max
        assertEquals(0, results.get(3).getValue()); // Avg
        assertEquals(0, results.get(4).getValue()); // Bad count
        assertEquals(0, results.get(5).getValue()); // Sleepless nights
        assertEquals("Недостаточно данных", results.get(6).getValue()); // Chronotype
    }
}