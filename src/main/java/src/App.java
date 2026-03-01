package src;

import src.findMinTask.FindMinTask;
import src.httpClientTask.HttpClientTask;
import src.sortArrayTask.SortArrayTask;
import src.streamApiTask.StreamApiTask;

import java.util.Scanner;

public class App
{
    private enum Command {
        FIND_MIN,
        SORT_ARRAY,
        FIND_MEAN_SALARY,
        HTTP_JSON,
        EXIT
    }
    public static void main( String[] args ) {

        String message = """
                %d) Поиск минимума в массиве длины n;
                %d) Сортировка массива длины n;
                %d) Поиск средней зарплаты в массиве Employee;
                %d) HTTP Client и JSON
                %d) Выход""".formatted(
                        Command.FIND_MIN.ordinal(),
                        Command.SORT_ARRAY.ordinal(),
                        Command.FIND_MEAN_SALARY.ordinal(),
                        Command.HTTP_JSON.ordinal(),
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
                case FIND_MIN -> new FindMinTask();
                case SORT_ARRAY -> new SortArrayTask();
                case FIND_MEAN_SALARY -> new StreamApiTask();
                case HTTP_JSON -> new HttpClientTask();
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
