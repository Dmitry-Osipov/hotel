package ui;

import lombok.Getter;
import lombok.ToString;
import ui.utils.ErrorMessages;

import java.util.Scanner;

/**
 * Класс отвечает за работу UI.
 */
@Getter
@ToString
public class MenuController {
    private final Builder builder;
    private final Navigator navigator;

    /**
     * Класс отвечает за работу UI.
     */
    public MenuController() {
        builder = new Builder();
        builder.buildMenu();
        navigator = new Navigator(builder.getRootMenu());
    }

    /**
     * Метод запускает приложение отеля.
     */
    public void run() {
        while (true) {
            navigator.printMenu();
            int choice = getUserInput() - 1;
            if (choice == -2) {
                exit();
                break;
            }
            navigator.navigate(choice);
        }
    }

    /**
     * Служебный метод запрашивает у пользователя число, проверяет валидность поступивших данных от пользователя.
     * @return Число.
     */
    private int getUserInput() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nВыберите действие (для выхода введите -1): ");
        while (!scanner.hasNextInt()) {
            System.out.println("\n" + ErrorMessages.INCORRECT_INPUT.getMessage());
            scanner.next();
        }

        return scanner.nextInt();
    }

    /**
     * Служебный метод производит user-friendly выход из программы.
     */
    private void exit() {
        System.out.println("\nВыход из программы...");
    }
}
