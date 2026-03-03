package com.nau.util;

import java.util.Scanner;

public class InputHelper {

    private static final String INVALID_INPUT_MESSAGE = "Некорректный ввод. Необходимо ввести целое неотрицательное число.";

    public static int readArraySize() {
        int n = -1;
        String input;
        Scanner sc = new Scanner(System.in);

        while (n < 0) {
            System.out.println("Введите количество элементов массива: ");

            input = sc.nextLine();
            try {
                n = Integer.parseInt(input);
                if (n < 0) {
                    System.out.println(INVALID_INPUT_MESSAGE);
                }
            } catch (NumberFormatException e) {
                System.out.println(INVALID_INPUT_MESSAGE);
            }
        }

        return n;
    }

}
