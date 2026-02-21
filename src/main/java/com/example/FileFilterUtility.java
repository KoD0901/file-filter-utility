package com.example;

import com.example.models.Statistics;
import com.example.processors.FileProcessor;
import com.example.utils.StatisticsPrinter;

import java.util.ArrayList;
import java.util.List;

public class FileFilterUtility {

    private static String outputPath = ".";
    private static String prefix = "";
    private static boolean appendMode = false;
    private static boolean shortStats = true;
    private static List<String> inputFiles = new ArrayList<>();

    public static void main(String[] args) {
        try {
            if (!parseArguments(args)) {
                printUsage();
                return;
            }

            Statistics statistics = new Statistics(!shortStats);
            FileProcessor processor = new FileProcessor(outputPath, prefix, appendMode, statistics);

            boolean success = processor.processFiles(inputFiles);

            if (success) {
                StatisticsPrinter.printStatistics(statistics, !shortStats);
                System.out.println("Обработка завершена успешно.");
            } else {
                System.err.println("Обработка завершена с ошибками.");
                System.exit(1);
            }

        } catch (Exception e) {
            System.err.println("Критическая ошибка: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static boolean parseArguments(String[] args) {
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];

            switch (arg) {
                case "-o":
                    if (i + 1 < args.length) {
                        outputPath = args[++i];
                    } else {
                        System.err.println("Ошибка: после -o должен быть указан путь");
                        return false;
                    }
                    break;

                case "-p":
                    if (i + 1 < args.length) {
                        prefix = args[++i];
                    } else {
                        System.err.println("Ошибка: после -p должен быть указан префикс");
                        return false;
                    }
                    break;

                case "-a":
                    appendMode = true;
                    break;

                case "-s":
                    shortStats = true;
                    break;

                case "-f":
                    shortStats = false;
                    break;

                default:
                    if (arg.startsWith("-")) {
                        System.err.println("Неизвестная опция: " + arg);
                        return false;
                    } else {
                        inputFiles.add(arg);
                    }
                    break;
            }
        }

        if (inputFiles.isEmpty()) {
            System.err.println("Ошибка: не указаны входные файлы");
            return false;
        }

        return true;
    }

    private static void printUsage() {
        System.out.println("Использование: java -jar target/util.jar [опции] файл1 файл2 ...");
        System.out.println("Опции:");
        System.out.println("  -o <путь>    путь для выходных файлов");
        System.out.println("  -p <префикс> префикс имен выходных файлов");
        System.out.println("  -a           режим добавления в существующие файлы");
        System.out.println("  -s           краткая статистика (по умолчанию)");
        System.out.println("  -f           полная статистика");
        System.out.println("Пример: java -jar target/util.jar -s -a -p sample- in1.txt in2.txt");
    }
}