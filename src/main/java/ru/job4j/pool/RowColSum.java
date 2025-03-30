package ru.job4j.pool;

import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.IntStream;

public class RowColSum {
    public static class Sums {
        private int rowSum;
        private int colSum;

        public Sums(int rowSum, int colSum) {
            this.rowSum = rowSum;
            this.colSum = colSum;
        }

        public Sums() {
        }

        public int getRowSum() {
            return rowSum;
        }

        public void setRowSum(int rowSum) {
            this.rowSum = rowSum;
        }

        public int getColSum() {
            return colSum;
        }

        public void setColSum(int colSum) {
            this.colSum = colSum;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Sums sums = (Sums) o;
            return rowSum == sums.rowSum && colSum == sums.colSum;
        }

        @Override
        public int hashCode() {
            return Objects.hash(rowSum, colSum);
        }
    }

    private static void validate(int[][] matrix) {
        if (matrix.length == 0 || matrix[0].length == 0) {
            throw new IllegalArgumentException("Матрица не может быть пустой.");
        }
        int expectedLength = matrix[0].length;
        if (expectedLength == 0) {
            throw new IllegalArgumentException("Матрица не может содержать пустую строку.");
        }
        for (int[] row : matrix) {
            if (row.length != expectedLength) {
                throw new IllegalArgumentException("Матрица должна быть квадратной и содержать строки одинаковой длины.");
            }
        }
    }

    public static Sums[] sum(int[][] matrix) {
        validate(matrix);
        Sums[] result = new Sums[matrix.length];
        for (int i = 0; i < result.length; i++) {
            int col = i;
            result[i] = new Sums(
                    Arrays.stream(matrix[i]).sum(),
                    Arrays.stream(matrix).mapToInt(ints -> ints[col]).sum()
            );
        }
        return result;
    }

    public static Sums[] asyncSum(int[][] matrix) throws ExecutionException, InterruptedException {
        validate(matrix);
        int size = matrix.length;
        Sums[] result = new Sums[size];

        CompletableFuture<Integer>[] rowFutures = new CompletableFuture[size];
        CompletableFuture<Integer>[] colFutures = new CompletableFuture[size];

        for (int i = 0; i < size; i++) {
            rowFutures[i] = asyncRowSum(matrix, i);
            colFutures[i] = asyncColSum(matrix, i);
        }

        CompletableFuture<Void> allRows = CompletableFuture.allOf(rowFutures);
        CompletableFuture<Void> allCols = CompletableFuture.allOf(colFutures);
        CompletableFuture.allOf(allRows, allCols).get();

        for (int i = 0; i < result.length; i++) {
            result[i] = new Sums();
            result[i].setRowSum(rowFutures[i].join());
            result[i].setColSum(colFutures[i].join());
        }
        return result;
    }

    private static CompletableFuture<Integer> asyncRowSum(int[][] matrix, int row) {
        return CompletableFuture.supplyAsync(() -> IntStream.of(matrix[row]).sum());
    }

    private static CompletableFuture<Integer> asyncColSum(int[][] matrix, int col) {
        return CompletableFuture.supplyAsync(() -> Arrays.stream(matrix).mapToInt(ints -> ints[col]).sum());
    }
}