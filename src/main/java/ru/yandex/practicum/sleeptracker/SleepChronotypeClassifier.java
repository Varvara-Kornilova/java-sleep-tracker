package ru.yandex.practicum.sleeptracker;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SleepChronotypeClassifier implements SleepAnalyzer {

    private static final long MIN_NIGHT_DURATION_MINUTES = 240L;

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        if (sessions.isEmpty()) {
            return new SleepAnalysisResult("Хронотип", "Недостаточно данных");
        }

        List<SleepingSession> nightSessions = sessions.stream()
                .filter(session -> session.getDurationMinutes() >= MIN_NIGHT_DURATION_MINUTES)
                .toList();

        if (nightSessions.isEmpty()) {
            return new SleepAnalysisResult("Хронотип", "Недостаточно данных");
        }

        Map<Chronotype, Long> counts = nightSessions.stream()
                .map(this::classifyNight)
                .collect(Collectors.groupingBy(
                        type -> type,
                        Collectors.counting()
                ));

        long maxCount = counts.values().stream()
                .mapToLong(Long::longValue)
                .max()
                .orElse(0L);

        long typesWithMaxCount = counts.values().stream()
                .filter(count -> count == maxCount)
                .count();

        Chronotype resultType = (typesWithMaxCount == 1)
                ? counts.entrySet().stream()
                .filter(entry -> entry.getValue() == maxCount)
                .findFirst()
                .map(Map.Entry::getKey)
                .orElse(Chronotype.DOVE)
                : Chronotype.DOVE;

        return new SleepAnalysisResult("Хронотип", resultType.getDisplayName());
    }

    private Chronotype classifyNight(SleepingSession session) {
        LocalTime sleepTime = session.getStart().toLocalTime();
        LocalTime wakeTime = session.getEnd().toLocalTime();

        boolean isLateBedtime = sleepTime.isAfter(LocalTime.of(23, 0)) ||
                !sleepTime.isAfter(LocalTime.of(6, 0));

        boolean isLateWakeUp = wakeTime.isAfter(LocalTime.of(9, 0));

        if (isLateBedtime && isLateWakeUp) {
            return Chronotype.OWL;
        }

        if (!sleepTime.isAfter(LocalTime.of(22, 0)) &&
                !wakeTime.isAfter(LocalTime.of(7, 0))) {
            return Chronotype.LARK;
        }

        return Chronotype.DOVE;
    }
}