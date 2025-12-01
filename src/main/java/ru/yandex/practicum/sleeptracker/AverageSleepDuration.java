package ru.yandex.practicum.sleeptracker;

import java.util.List;

public class AverageSleepDuration implements SleepAnalyzer {

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        if (sessions.isEmpty()) {
            return new SleepAnalysisResult("Средняя длительность сна (мин)", 0);
        }

        double averageMinutes = sessions.stream()
                .mapToLong(SleepingSession::getDurationMinutes)
                .average()
                .orElse(0.0);

        int rounded = (int) Math.round(averageMinutes);
        return new SleepAnalysisResult("Средняя длительность сна (мин)", rounded);
    }
}
