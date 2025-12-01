package ru.yandex.practicum.sleeptracker;

import java.util.List;

public class MaxSleepDuration implements SleepAnalyzer {

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        if (sessions.isEmpty()) {
            return new SleepAnalysisResult("Максимальная длительность сна (мин)", 0);
        }

        long max = sessions.stream()
                .mapToLong(SleepingSession::getDurationMinutes)
                .max()
                .orElse(0L);

        return new SleepAnalysisResult("Максимальная длительность сна (мин)", (int) max);
    }
}
