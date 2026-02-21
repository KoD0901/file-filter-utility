package com.example.processors;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class DataWriter {
    private final BufferedWriter writer;
    private final String filePath;
    private boolean hasWrittenData = false;

    public DataWriter(String filePath, boolean appendMode) throws IOException {
        this.filePath = filePath;
        Path path = Paths.get(filePath);

        if (appendMode && Files.exists(path)) {
            this.writer = Files.newBufferedWriter(path,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND);
            this.hasWrittenData = true;
        } else {
            this.writer = Files.newBufferedWriter(path,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);
        }
    }

    public void write(String data) throws IOException {
        if (hasWrittenData) {
            writer.newLine();
        }
        writer.write(data);
        writer.flush();
        hasWrittenData = true;
    }

    public void close() throws IOException {
        writer.close();

        if (!hasWrittenData) {
            Files.deleteIfExists(Paths.get(filePath));
        }
    }
}