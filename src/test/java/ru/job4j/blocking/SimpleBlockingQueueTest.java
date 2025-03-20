package ru.job4j.blocking;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SimpleBlockingQueueTest {
    @Test
    void whenTwoThreadsThenOk() throws InterruptedException {
        SimpleBlockingQueue queue = new SimpleBlockingQueue(5);
        Thread offer = new Thread(() -> {
            try {
                for (int i = 0; i < 100; i++) {
                    long number = Math.round(Math.random() * 50);
                    Thread.sleep(number);
                    queue.offer(number);
                    System.out.println("Added " + (i + 1) + System.lineSeparator() + "Size " + queue.size());
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        Thread poll = new Thread(() -> {
            try {
                for (int i = 0; i < 100; i++) {
                    long number = Math.round(Math.random() * 50);
                    Thread.sleep(number * 3);
                    queue.poll();
                    System.out.println("Deleted " + (i + 1) + System.lineSeparator() + "Size " + queue.size());
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        offer.start();
        poll.start();
        offer.join();
        poll.join();
        assertThat(queue.isEmpty()).isTrue();
    }

}