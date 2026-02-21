package com.example.processors;

import com.example.models.DataType;
import com.example.models.Statistics;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class FileProcessor {
    private final String outputPath;
    private final String prefix;
    private final boolean appendMode;
    private final Statistics statistics;
    private final Map<DataType, DataWriter> writers = new HashMap<>();

    private static final Pattern INTEGER_PATTERN =
            Pattern.compile("^[+-]?\\d+$");
    private static final Pattern FLOAT_PATTERN =
            Pattern.compile("^[+-]?\\d+\\.\\d+([eE][+-]?\\d+)?$|^[+-]?\\d+[eE][+-]?\\d+$");

    public FileProcessor(String outputPath, String prefix, boolean appendMode, Statistics statistics) {
        this.outputPath = outputPath;
        this.prefix = prefix;
        this.appendMode = appendMode;
        this.statistics = statistics;
    }

    public boolean processFiles(List<String> inputFiles) {
        boolean hasErrors = false;

        try {
            Path outputDir = Paths.get(outputPath);
            if (!Files.exists(outputDir)) {
                Files.createDirectories(outputDir);
            }
        } catch (IOException e) {
            System.err.println("Ошибка создания директории: " + e.getMessage());
            return false;
        }

        for (String fileName : inputFiles) {
            try {
                processFile(fileName);
            } catch (IOException e) {
                System.err.println("Ошибка обработки файла " + fileName + ": " + e.getMessage());
                hasErrors = true;
            }
        }

        closeWriters();

        return !hasErrors;
    }

    private void processFile(String fileName) throws IOException {
        Path filePath = Paths.get(fileName);

        if (!Files.exists(filePath)) {
            throw new IOException("Файл не найден: " + fileName);
        }

        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                line = line.trim();

                if (line.isEmpty()) {
                    continue;
                }

                try {
                    processLine(line);
                } catch (Exception e) {
                    System.err.println("Ошибка обработки строки " + lineNumber +
                            " в файле " + fileName + ": " + e.getMessage());
                }
            }
        }
    }

    private void processLine(String line) throws IOException {
        DataType type = determineType(line);

        switch (type) {
            case INTEGER:
                statistics.addInteger(line);
                getWriter(DataType.INTEGER).write(line);
                break;

            case FLOAT:
                statistics.addFloat(line);
                getWriter(DataType.FLOAT).write(line);
                break;

            case STRING:
                statistics.addString(line);
                getWriter(DataType.STRING).write(line);
                break;
        }
    }

    private DataType determineType(String line) {
        if (INTEGER_PATTERN.matcher(line).matches()) {
            return DataType.INTEGER;
        } else if (FLOAT_PATTERN.matcher(line).matches()) {
            return DataType.FLOAT;
        } else {
            return DataType.STRING;
        }
    }

    private DataWriter getWriter(DataType type) throws IOException {
        if (!writers.containsKey(type)) {
            String fileName = getFileName(type);
            writers.put(type, new DataWriter(fileName, appendMode));
        }
        return writers.get(type);
    }

    private String getFileName(DataType type) {
        String baseName;
        switch (type) {
            case INTEGER:
                baseName = "integers.txt";
                break;
            case FLOAT:
                baseName = "floats.txt";
                break;
            case STRING:
                baseName = "strings.txt";
                break;
            default:
                throw new IllegalArgumentException("Неизвестный тип данных");
        }

        Path path = Paths.get(outputPath, prefix + baseName);
        return path.toString();
    }

    private void closeWriters() {
        for (DataWriter writer : writers.values()) {
            try {
                writer.close();
            } catch (IOException e) {
                System.err.println("Ошибка при закрытии файла: " + e.getMessage());
            }
        }
    }
}