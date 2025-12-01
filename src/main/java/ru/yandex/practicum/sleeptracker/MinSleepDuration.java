package ru.yandex.practicum.sleeptracker;

import java.util.List;

public class MinSleepDuration implements SleepAnalyzer {

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        if (sessions.isEmpty()) {
            return new SleepAnalysisResult("Минимальная длительность сна (мин)", 0);
        }

        long min = sessions.stream()
                .mapToLong(SleepingSession::getDurationMinutes)
                .min()
                .orElse(0L);

        return new SleepAnalysisResult("Минимальная длительность сна (мин)", (int) min);
    }
}
