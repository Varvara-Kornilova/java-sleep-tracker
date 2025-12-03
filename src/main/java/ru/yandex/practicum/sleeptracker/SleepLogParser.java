package ru.yandex.practicum.sleeptracker;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SleepLogParser {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");

    public static List<SleepingSession> parseFromFile(String filePath) throws IOException {
        return Files.lines(Path.of(filePath))
                .map(String::trim)
                .filter(line -> !line.isEmpty())
                .map(SleepLogParser::parseLine)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private static SleepingSession parseLine(String line) {
        String[] parts = line.split(";", -1);

        if (parts.length != 3) {
            System.err.println("Пропущена некорректная строка (ожидается 3 поля): " + line);
            return null;
        }

        String startStr = parts[0].trim();
        String endStr = parts[1].trim();
        String qualityStr = parts[2].trim();

        if (startStr.isEmpty() || endStr.isEmpty() || qualityStr.isEmpty()) {
            System.err.println("Пропущена строка с пустыми полями: " + line);
            return null;
        }

        try {
            LocalDateTime start = LocalDateTime.parse(startStr, FORMATTER);
            LocalDateTime end = LocalDateTime.parse(endStr, FORMATTER);
            SleepQuality quality = SleepQuality.valueOf(qualityStr.toUpperCase());
            return new SleepingSession(start, end, quality);
        } catch (Exception e) {
            System.err.println("Пропущена строка из-за ошибки парсинга: " + line + " (" + e.getMessage() + ")");
            return null;
        }
    }
}
