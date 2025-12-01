package ru.yandex.practicum.sleeptracker;

import java.util.List;

public class BadSleepCount implements SleepAnalyzer {

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        long count = sessions.stream()
                .filter(session -> SleepQuality.BAD == session.getQuality())
                .count();
        return new SleepAnalysisResult("Количество плохих ночей (BAD)", (int) count);
    }
}
