package ru.job4j.io;

import java.io.*;

public class ContentWriter {
    private final File file;

    public ContentWriter(File file) {
        this.file = file;
    }

    public void saveContent(String content) throws IOException {
        try (OutputStream o = new FileOutputStream(file)) {
            for (int i = 0; i < content.length(); i++) {
                o.write(content.charAt(i));
            }
        }
    }
}
