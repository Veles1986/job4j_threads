package ru.job4j.blocking;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CopyOnWriteArrayList;

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

    @Test
    void whenBlockingTestThenOk() throws InterruptedException {
        final CopyOnWriteArrayList<Integer> buffer = new CopyOnWriteArrayList<>();
        final SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>(2);
        Thread producer = new Thread(
                () -> {
                    for (int i = 0; i < 20; i++) {
                        try {
                            queue.offer(i);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            Thread.currentThread().interrupt();
                        }
                    }
                }
        );
        producer.start();
        Thread consumer = new Thread(
                () -> {
                    try {
                        while (true) {
                            Integer value = queue.poll();
                            if (value == null) {
                                break; // Выход из цикла, если очередь пуста и больше не будет новых данных
                            }
                            buffer.add(value);
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
        );
        consumer.start();
        producer.join();
        consumer.interrupt();
        consumer.join();
        assertThat(buffer).containsExactly(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19);
    }
}