package ru.job4j.concurrent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

public class Wget implements Runnable {

    private final String url;
    private final int speed;

    public Wget(String url, int speed) {
        this.url = url;
        this.speed = speed;
    }

    @Override
    public void run() {
        var file = new File("tmp.xml");
        try (var input = new URL(url).openStream();
             var output = new FileOutputStream(file)) {
            var dataBuffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = input.read(dataBuffer, 0, dataBuffer.length)) != -1) {
                long downloadAt = System.nanoTime();
                output.write(dataBuffer, 0, bytesRead);
                float millis = (System.nanoTime() - downloadAt) / 1_000_000f;
                int speedFact = Math.round(bytesRead / millis);
                try {
                    Thread.sleep(speedFact / speed * 1000L);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Read " + bytesRead + " bytes : " + (System.nanoTime() - downloadAt) + " nano.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean urlValidator(String url) {
        return UrlValidator.urlValidator(url);
    }

    public static void main(String[] args) throws InterruptedException {
        String url = args[0];
        if (!urlValidator(url)) {
            throw new IllegalArgumentException();
        }
        int speed = Integer.parseInt(args[1]);
        Thread wget = new Thread(new Wget(url, speed));
        wget.start();
        wget.join();
    }
}

