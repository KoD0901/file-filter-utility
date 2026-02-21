package com.example.utils;

import com.example.models.Statistics;

import java.text.DecimalFormat;

public class StatisticsPrinter {
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.##");

    public static void printStatistics(Statistics stats, boolean fullStats) {
        System.out.println("\n=== СТАТИСТИКА ОБРАБОТКИ ===");

        // Целые числа
        System.out.println("\nЦелые числа:");
        System.out.println("  Количество: " + stats.getIntegerCount());

        if (fullStats && stats.getIntegerCount() > 0) {
            System.out.println("  Минимальное: " +
                    stats.getIntegerMin().orElseThrow());
            System.out.println("  Максимальное: " +
                    stats.getIntegerMax().orElseThrow());
            System.out.println("  Сумма: " +
                    DECIMAL_FORMAT.format(stats.getIntegerSum().orElseThrow()));
            System.out.println("  Среднее: " +
                    DECIMAL_FORMAT.format(stats.getIntegerAverage().orElseThrow()));
        }

        // Вещественные числа
        System.out.println("\nВещественные числа:");
        System.out.println("  Количество: " + stats.getFloatCount());

        if (fullStats && stats.getFloatCount() > 0) {
            System.out.println("  Минимальное: " +
                    DECIMAL_FORMAT.format(stats.getFloatMin().orElseThrow()));
            System.out.println("  Максимальное: " +
                    DECIMAL_FORMAT.format(stats.getFloatMax().orElseThrow()));
            System.out.println("  Сумма: " +
                    DECIMAL_FORMAT.format(stats.getFloatSum().orElseThrow()));
            System.out.println("  Среднее: " +
                    DECIMAL_FORMAT.format(stats.getFloatAverage().orElseThrow()));
        }

        // Строки
        System.out.println("\nСтроки:");
        System.out.println("  Количество: " + stats.getStringCount());

        if (fullStats && stats.getStringCount() > 0) {
            System.out.println("  Мин. длина: " +
                    stats.getStringMinLength().orElseThrow());
            System.out.println("  Макс. длина: " +
                    stats.getStringMaxLength().orElseThrow());
            System.out.println("  Средняя длина: " +
                    DECIMAL_FORMAT.format(stats.getStringAverageLength().orElseThrow()));
        }

        System.out.println("\n==========================");
    }
}