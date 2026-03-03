package com.nau.findMinTask;

import com.nau.NauTask;
import com.nau.util.InputHelper;

import java.util.Arrays;
import java.util.OptionalInt;
import java.util.Random;

public class FindMinNauTask implements NauTask {

    @Override
    public void solve() {
        int n = InputHelper.readArraySize();
        int[] randomNumbersArray = new Random().ints().limit(n).toArray();
        OptionalInt minimumAbsoluteArrayValue = Arrays.stream(randomNumbersArray).map(Math::abs).min();

        System.out.println("Массив: " + Arrays.toString(randomNumbersArray));
        if (minimumAbsoluteArrayValue.isPresent()) {
            System.out.println("Минимальное по модулю значение массива: " + minimumAbsoluteArrayValue.getAsInt());
        } else {
            System.out.println("Минимальное по модулю значение массива: -");
        }
    }

}
