package ru.yandex.practicum.sleeptracker;

import java.util.List;

public class TotalSessionsCount implements SleepAnalyzer {

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        int count = (int) sessions.stream().count();
        return new SleepAnalysisResult("Всего сессий сна", count);
    }
}
