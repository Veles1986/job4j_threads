package ru.job4j.pool;

import java.util.concurrent.*;

public class EmailNotification {
    private final ExecutorService executorService = Executors.newFixedThreadPool(
            Runtime.getRuntime().availableProcessors()
    );

    public void emailTo(User user) {
        executorService.submit(() -> {
            String subject = String.format("Notification %s to email %s", user.username(), user.email());
            String body = String.format("Add a new event to %s", user.username());
            send(subject, body, user.email());
        });
    }

    public void send(String subject, String body, String email) {
    }

    public void close() {
        executorService.shutdown();
        while (!executorService.isTerminated()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
