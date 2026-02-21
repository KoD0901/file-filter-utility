package com.example.models;

import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class Statistics {
    private final boolean fullStats;

    private final List<Long> integers = new ArrayList<>();

    private final List<Double> floats = new ArrayList<>();

    private final List<String> strings = new ArrayList<>();
    private final AtomicLong totalStringsLength = new AtomicLong(0);

    public Statistics(boolean fullStats) {
        this.fullStats = fullStats;
    }

    public void addInteger(String value) {
        try {
            BigInteger bigInt = new BigInteger(value);
            if (bigInt.compareTo(BigInteger.valueOf(Long.MAX_VALUE)) > 0 ||
                    bigInt.compareTo(BigInteger.valueOf(Long.MIN_VALUE)) < 0) {
                strings.add(value);
            } else {
                integers.add(bigInt.longValue());
            }
        } catch (NumberFormatException e) {
            strings.add(value);
        }
    }

    public void addFloat(String value) {
        try {
            floats.add(Double.parseDouble(value));
        } catch (NumberFormatException e) {
            strings.add(value);
        }
    }

    public void addString(String value) {
        strings.add(value);
        if (fullStats) {
            totalStringsLength.addAndGet(value.length());
        }
    }

    public int getIntegerCount() {
        return integers.size();
    }

    public int getFloatCount() {
        return floats.size();
    }

    public int getStringCount() {
        return strings.size();
    }

    public OptionalLong getIntegerMin() {
        return integers.stream().mapToLong(Long::longValue).min();
    }

    public OptionalLong getIntegerMax() {
        return integers.stream().mapToLong(Long::longValue).max();
    }

    public OptionalDouble getIntegerSum() {
        return OptionalDouble.of(integers.stream().mapToLong(Long::longValue).sum());
    }

    public OptionalDouble getIntegerAverage() {
        return integers.isEmpty() ?
                OptionalDouble.empty() :
                OptionalDouble.of(integers.stream().mapToLong(Long::longValue).average().getAsDouble());
    }

    public OptionalDouble getFloatMin() {
        return floats.stream().mapToDouble(Double::doubleValue).min();
    }

    public OptionalDouble getFloatMax() {
        return floats.stream().mapToDouble(Double::doubleValue).max();
    }

    public OptionalDouble getFloatSum() {
        return OptionalDouble.of(floats.stream().mapToDouble(Double::doubleValue).sum());
    }

    public OptionalDouble getFloatAverage() {
        return floats.isEmpty() ?
                OptionalDouble.empty() :
                OptionalDouble.of(floats.stream().mapToDouble(Double::doubleValue).average().getAsDouble());
    }

    public OptionalInt getStringMinLength() {
        return strings.stream().mapToInt(String::length).min();
    }

    public OptionalInt getStringMaxLength() {
        return strings.stream().mapToInt(String::length).max();
    }

    public OptionalDouble getStringAverageLength() {
        return strings.isEmpty() ?
                OptionalDouble.empty() :
                OptionalDouble.of(totalStringsLength.get() / (double) strings.size());
    }
}