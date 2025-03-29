package ru.job4j.pool;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ParallelSearchTest {

    @Test
    void whenSearchIntThenOk() {
        List<Integer> list = List.of(3, 5, 8, 2, 5, 7, 12, 23, 31);
        int expected = 3;
        int result = ParallelSearch.search(list, 2);
        assertThat(expected).isEqualTo(result);
    }

    @Test
    void whenSearchStringThenOk() {
        List<String> list = List.of("Anton", "Bogdan", "ALex", "Michael", "Veles");
        int expected = 4;
        int result = ParallelSearch.search(list, "Veles");
        assertThat(expected).isEqualTo(result);
    }

    @Test
    void whenSearchBigListThenOk() {
        List<Integer> list = List.of(3, 5, 8, 2, 5, 7, 12, 23, 31, 27, 9, 43, 75, 64, 97, 25, 17, 30, 80, 70, 47);
        int expected = 11;
        int result = ParallelSearch.search(list, 43);
        assertThat(expected).isEqualTo(result);
    }

    @Test
    void whenSearchThenElementNotFound() {
        List<Integer> list = List.of(3, 5, 8, 2, 5, 7, 12, 23, 31, 27, 9, 43, 75, 64, 97, 25, 17, 30, 80, 70, 47);
        int expected = -1;
        int result = ParallelSearch.search(list, 58);
        assertThat(expected).isEqualTo(result);
    }
}