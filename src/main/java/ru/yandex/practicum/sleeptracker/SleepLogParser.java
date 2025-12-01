package ru.yandex.practicum.sleeptracker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SleepLogParser {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");

    public static List<SleepingSession> parseFromResource() throws IOException {
        try (InputStream is = SleepLogParser.class.getClassLoader().getResourceAsStream("sleep_log.txt")) {
            if (is == null) {
                throw new IOException("Файл sleep_log.txt не найден в classpath (resources).");
            }

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
                return reader.lines()
                        .map(String::trim)
                        .filter(line -> !line.isEmpty())
                        .map(SleepLogParser::parseLine)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
            }
        }
    }

    private static SleepingSession parseLine(String line) {
        String[] parts = line.split(";");

        if (parts.length != 3) {
            System.err.println("Некорректная строка: " + line);
            return null;
        }

        try {
            LocalDateTime start = LocalDateTime.parse(parts[0], FORMATTER);
            LocalDateTime end = LocalDateTime.parse(parts[1], FORMATTER);
            SleepQuality quality = SleepQuality.valueOf(parts[2].trim().toUpperCase());
            return new SleepingSession(start, end, quality);
        } catch (Exception e) {
            System.err.println("Ошибка парсинга строки: " + line + " — " + e.getMessage());
            return null;
        }
    }
}
