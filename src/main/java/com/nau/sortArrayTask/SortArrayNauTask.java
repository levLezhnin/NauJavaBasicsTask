package com.nau.sortArrayTask;

import com.nau.NauTask;
import com.nau.util.InputHelper;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class SortArrayNauTask implements NauTask {

    @Override
    public void solve() {
        int n = InputHelper.readArraySize();
        List<Double> randomNumbersArray = new Random().doubles().limit(n).boxed().collect(Collectors.toList());
        System.out.println("Массив: " + randomNumbersArray);

        quickSort(randomNumbersArray, 0, n);

        System.out.println("Отсортированный массив: " + randomNumbersArray);
    }

    private void quickSort(List<Double> array, int from, int to) {
        if (from + 1 >= to) {
            return;
        }

        int pivot = from + new Random().nextInt(to - from);
        double pivotValue = array.get(pivot);

        int l = from;
        int r = to - 1;

        while (l <= r) {
            while (l <= r && array.get(l) < pivotValue) {
                ++l;
            }
            while (l <= r && array.get(r) >= pivotValue) {
                --r;
            }
            if (l <= r) {
                swap(array, l++, r--);
            }
        }

        quickSort(array, from, l);
        quickSort(array, l, to);
    }

    private void swap(List<Double> array, int i1, int i2) {
        if (i1 == i2) {
            return;
        }
        double temp = array.get(i1);
        array.set(i1, array.get(i2));
        array.set(i2, temp);
    }
}
