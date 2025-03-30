package ru.job4j.pool;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;

class RowColSumTest {
    @Test
    void whenSyncSumThenTrue() {
        int[][] matrix = {
                {3, 4, 5, 6},
                {10, 8, 6, 2},
                {9, 3, 12, 9},
                {4, 2, 7, 20}
        };
        RowColSum.Sums[] expected = {
                new RowColSum.Sums(18, 26),
                new RowColSum.Sums(26, 17),
                new RowColSum.Sums(33, 30),
                new RowColSum.Sums(33, 37)
        };
        RowColSum.Sums[] result = RowColSum.sum(matrix);
        assertThat(result).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void whenAsyncSumThenTrue() throws ExecutionException, InterruptedException {
        int[][] matrix = {
                {3, 4, 5, 6},
                {10, 8, 6, 2},
                {9, 3, 12, 9},
                {4, 2, 7, 20}
        };
        RowColSum.Sums[] expected = {
                new RowColSum.Sums(18, 26),
                new RowColSum.Sums(26, 17),
                new RowColSum.Sums(33, 30),
                new RowColSum.Sums(33, 37)
        };
        RowColSum.Sums[] result = RowColSum.asyncSum(matrix);
        assertThat(result).usingRecursiveComparison().isEqualTo(expected);
    }
}