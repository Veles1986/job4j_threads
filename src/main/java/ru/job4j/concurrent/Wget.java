package ru.job4j.concurrent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

public class Wget implements Runnable {
    private final String file;
    private final String url;
    private final int speed;

    public Wget(String url, String file, int speed) {
        this.url = url;
        this.file = file;
        this.speed = speed;
    }

    @Override
    public void run() {
        var file = new File(this.file);
        try (var input = new URL(url).openStream();
             var output = new FileOutputStream(file)) {
            var dataBuffer = new byte[1024];
            int sumBytes = 0;
            int bytesRead;
            long downloadAt = System.currentTimeMillis();
            while ((bytesRead = input.read(dataBuffer, 0, dataBuffer.length)) != -1) {
                sumBytes += bytesRead;
                if (sumBytes >= speed) {
                    long millis = System.currentTimeMillis() - downloadAt;
                    if (millis < 1000) {
                        try {
                            Thread.sleep(1000 - millis);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    downloadAt = System.currentTimeMillis();
                }
                output.write(dataBuffer, 0, bytesRead);
                System.out.println("Read " + bytesRead + " bytes");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean urlValidator(String url) {
        return UrlValidator.urlValidator(url);
    }

    public static void main(String[] args) throws InterruptedException {
        if (args.length != 3) {
            throw new IllegalArgumentException();
        }
        String url = args[0];
        String file = args[1];
        int speed = Integer.parseInt(args[2]);
        if (!urlValidator(url)) {
            throw new IllegalArgumentException();
        }
        Thread wget = new Thread(new Wget(url, file, speed));
        wget.start();
        wget.join();
    }
}