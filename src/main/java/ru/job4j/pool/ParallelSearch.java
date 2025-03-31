package ru.job4j.pool;

import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class ParallelSearch<T> extends RecursiveTask<Integer> {
    private final List<T> list;
    private final T target;
    private final int start;
    private final int end;

    public ParallelSearch(List<T> list, T target, int start, int end) {
        this.list = list;
        this.target = target;
        this.start = start;
        this.end = end;
    }

    @Override
    protected Integer compute() {
        if (end - start <= 10) {
            for (int i = start; i < end; i++) {
                if (list.get(i).equals(target)) {
                    return i;
                }
            }
            return -1;
        }
        int middle = (start + end) / 2;
        ParallelSearch<T> leftSearch = new ParallelSearch<>(list, target, start, middle);
        ParallelSearch<T> rightSearch = new ParallelSearch<>(list, target, middle, end);
        leftSearch.fork();
        rightSearch.fork();
        int leftResult = leftSearch.join();
        int rightResult = rightSearch.join();
        return leftResult != -1 ? leftResult : rightResult;
    }

    public static <T> Integer search(List<T> list, T target) {
        if (list == null || list.isEmpty()) {
            return -1;
        }
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        return forkJoinPool.invoke(new ParallelSearch<>(list, target, 0, list.size()));
    }
}
