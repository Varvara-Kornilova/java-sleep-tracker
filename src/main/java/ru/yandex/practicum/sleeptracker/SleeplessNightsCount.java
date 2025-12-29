package ru.yandex.practicum.sleeptracker;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.LongStream;

public class SleeplessNightsCount implements SleepAnalyzer {

    private static final LocalTime NIGHT_START = LocalTime.MIDNIGHT;
    private static final LocalTime NIGHT_END = LocalTime.of(6, 0);

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        if (sessions.isEmpty()) {
            return new SleepAnalysisResult("Количество бессонных ночей", 0);
        }

        LocalDate firstNight = sessions.stream()
                .map(session -> session.getEnd().toLocalDate())
                .min(LocalDate::compareTo)
                .orElseThrow();

        LocalDate lastNight = sessions.stream()
                .map(session -> session.getEnd().toLocalDate())
                .max(LocalDate::compareTo)
                .orElseThrow();

        if (firstNight.isAfter(lastNight)) {
            return new SleepAnalysisResult("Количество бессонных ночей", 0);
        }

        long daysBetween = ChronoUnit.DAYS.between(firstNight, lastNight);
        int sleeplessCount = (int) LongStream.rangeClosed(0, daysBetween)
                .mapToObj(offset -> firstNight.plusDays(offset))
                .filter(nightDate -> isNightSleepless(nightDate, sessions))
                .count();

        return new SleepAnalysisResult("Количество бессонных ночей", sleeplessCount);
    }

    private boolean isNightSleepless(LocalDate nightDate, List<SleepingSession> sessions) {
        LocalDateTime nightStart = nightDate.atTime(NIGHT_START);
        LocalDateTime nightEnd = nightDate.atTime(NIGHT_END);

        return sessions.stream()
                .noneMatch(session ->
                        session.getStart().isBefore(nightEnd) &&
                                nightStart.isBefore(session.getEnd())
                );
    }
}