package HomeWork06;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

class HomeWork06Test {
    private HomeWork06 homeWork06;

    @BeforeEach
    public void init() {
        homeWork06 = new HomeWork06();
    }

    public static Stream<Arguments> remakeArrayData() {
        List<Arguments> out = new ArrayList<>();
        out.add(Arguments.arguments(new int[]{1, 4, 2, 5, 6, 4, 2, 1}, new int[]{2, 1}));
        out.add(Arguments.arguments(new int[]{1, 1, 6, 3, 2, 4}, new int[]{}));
        out.add(Arguments.arguments(new int[]{2, 4, 2, 5, 4, 8, 2, 1}, new int[]{8, 2, 1}));
        return out.stream();
    }

    @ParameterizedTest
    @MethodSource("remakeArrayData")
    public void remakeArrayTest(int[] arr, int[] result) {
        int[] methodResult = homeWork06.remakeArray(arr);
        Assertions.assertArrayEquals(result, methodResult);
    }

    @Test
    void remakeArrayException() {
        int[] arr = {1, 5, 7, 3};
        Assertions.assertThrows(RuntimeException.class, () -> homeWork06.remakeArray(arr));
        int[] empty = {};
        Assertions.assertThrows(RuntimeException.class, () -> homeWork06.remakeArray(empty));
    }


    public static Stream<Arguments> hasAnyRequestedNumbersData() {
        List<Arguments> out = new ArrayList<>();
        out.add(Arguments.arguments(new int[]{1, 1, 1, 4, 4, 1, 4, 4}, true));
        out.add(Arguments.arguments(new int[]{1, 1, 1, 1, 1, 1}, false));
        out.add(Arguments.arguments(new int[]{4, 4, 4, 4}, false));
        out.add(Arguments.arguments(new int[]{1, 4, 4, 1, 1, 4, 3}, true));
        return out.stream();
    }

    @ParameterizedTest
    @MethodSource("hasAnyRequestedNumbersData")
    public void hasAnyRequestedNumbers(int[] arr, boolean result) {
        boolean methodResult = homeWork06.hasAnyRequestedNumbers(arr);
        Assertions.assertEquals(methodResult, result);
    }
}