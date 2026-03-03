package com.nau;

import com.nau.findMinTask.FindMinNauTask;
import com.nau.httpClientTask.HttpClientNauTask;
import com.nau.sortArrayTask.SortArrayNauTask;
import com.nau.streamApiTask.StreamApiNauTask;
import com.nau.taskInterfaceTask.TaskImplementationNauTask;

import java.util.Scanner;

public class App
{
    public static void main( String[] args ) {

        String message = """
                %d) Поиск минимума в массиве длины n;
                %d) Сортировка массива длины n;
                %d) Поиск средней зарплаты в массиве Employee;
                %d) HTTP Client и JSON;
                %d) Скачивание файла (реализация интерфейса Task);
                %d) Выход""".formatted(
                        Command.FIND_MIN.ordinal(),
                        Command.SORT_ARRAY.ordinal(),
                        Command.FIND_MEAN_SALARY.ordinal(),
                        Command.HTTP_JSON.ordinal(),
                        Command.FILE_DOWNLOAD.ordinal(),
                        Command.EXIT.ordinal());

        int option;
        NauTask nauTask;
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println(message);

            String commandStr = sc.nextLine();
            try {
                option = Integer.parseInt(commandStr);
            } catch (NumberFormatException e) {
                continue;
            }

            if (option < 0 || option >= Command.values().length) {
                continue;
            }
            Command command = Command.values()[option];

            nauTask = switch (command) {
                case FIND_MIN -> new FindMinNauTask();
                case SORT_ARRAY -> new SortArrayNauTask();
                case FIND_MEAN_SALARY -> new StreamApiNauTask();
                case HTTP_JSON -> new HttpClientNauTask();
                case FILE_DOWNLOAD -> new TaskImplementationNauTask();
                default -> null;
            };

            if (command == Command.EXIT) {
                break;
            }

            nauTask.solve();
        }

        sc.close();
    }
}
