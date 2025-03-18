package ru.job4j.io;

import java.io.*;
import java.util.function.Predicate;

public class ContentReader {
    private final File file;

    public ContentReader(File file) {
        this.file = file;
    }

    private String content(Predicate<Character> filter) throws IOException {
        StringBuilder output = new StringBuilder();
        try (InputStream input = new FileInputStream(file)) {
            byte[] buffer = input.readAllBytes();
            for (byte oneByte : buffer) {
                char ch = (char) oneByte;
                if (filter.test(ch)) {
                    output.append(ch);
                }
            }
        }
        return output.toString();
    }

    public String getContent() throws IOException {
        return content(ch ->  true);
    }

    public String getContentWithoutUnicode() throws IOException {
        return content(ch -> ch < 0x80);
    }
}