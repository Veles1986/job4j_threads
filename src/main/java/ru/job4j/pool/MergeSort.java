package ru.job4j.pool;

public class MergeSort {

    public int[] sort(int[] array) {
        return sort(array, 0, array.length - 1);
    }

    private int[] sort(int[] array, int from, int to) {
        if (from >= to) {
            return new int[] {array[from]};
        }
        int middle = (from + to) / 2;
        return merge(
                sort(array, from, middle),
                sort(array, middle + 1, to)
        );
    }

    private int[] merge(int[] left, int[] right) {
        int leftIndex = 0;
        int rightIndex = 0;
        int resultIndex = 0;
        int[] result = new int[left.length + right.length];
        while (resultIndex != result.length) {
            if (leftIndex == left.length) {
                result[resultIndex++] = right[rightIndex++];
            } else if (rightIndex == right.length) {
                result[resultIndex++] = left[leftIndex++];
            } else if (left[leftIndex] <= right[rightIndex]) {
                result[resultIndex++] = left[leftIndex++];
            } else {
                result[resultIndex++] = right[rightIndex++];
            }
        }
        return result;
    }

}
