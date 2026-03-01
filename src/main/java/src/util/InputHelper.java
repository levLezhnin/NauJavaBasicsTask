package src.util;

import java.util.Scanner;

public class InputHelper {

    public static int readArraySize() {
        int n = -1;
        String input;
        Scanner sc = new Scanner(System.in);

        while (n < 0) {
            System.out.println("Введите количество элементов массива: ");

            input = sc.nextLine();
            try {
                n = Integer.parseInt(input);
            } catch (NumberFormatException ignored) {}
        }

        return n;
    }

}
