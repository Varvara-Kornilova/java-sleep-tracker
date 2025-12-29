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
        if (args.length != 1) {
            System.err.println("Использование: java SleepTrackerApp <путь_к_файлу_лога>");
            System.exit(1);
        }

        String logFilePath = args[0];

        try {
            List<SleepingSession> sessions = SleepLogParser.parseFromFile(logFilePath);
            SleepTrackerApp app = new SleepTrackerApp();
            List<SleepAnalysisResult> results = app.analyzeAll(sessions);

            System.out.println("=== Анализ сна ===");
            results.forEach(System.out::println);

        } catch (IOException e) {
            System.err.println("Ошибка при чтении файла '" + logFilePath + "': " + e.getMessage());
            System.exit(1);
        } catch (Exception e) {
            System.err.println("Ошибка при анализе: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}