package ru.yandex.practicum.sleeptracker;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class SleepTrackerApp {

    public List<SleepAnalysisResult> analyzeAll(List<SleepingSession> sessions) {
        List<SleepAnalyzer> analyzers = List.of(
                new TotalSessionsCount(),
                new MinSleepDuration(),
                new MaxSleepDuration(),
                new AverageSleepDuration(),
                new BadSleepCount(),
                new SleeplessNightsCount(),
                new SleepChronotypeClassifier()
        );

        return analyzers.stream()
                .map(analyzer -> analyzer.apply(sessions))
                .collect(Collectors.toList());
    }

    public static void main(String[] args) {
        try {
            List<SleepingSession> sessions = SleepLogParser.parseFromResource();
            SleepTrackerApp app = new SleepTrackerApp();
            List<SleepAnalysisResult> results = app.analyzeAll(sessions);
            results.forEach(System.out::println);
        } catch (IOException e) {
            System.err.println("Ошибка при чтении файла sleep_log.txt: " + e.getMessage());
            e.printStackTrace();
        }
    }
}