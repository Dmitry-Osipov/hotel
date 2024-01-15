package ui;

import lombok.Getter;
import lombok.ToString;

import java.util.Scanner;

@Getter
@ToString
public class MenuController {
    private Builder builder;
    private Navigator navigator;

    public MenuController(Builder builder, Navigator navigator) {
        this.builder = builder;
        this.navigator = navigator;
    }

    public void run() {
        while (true) {
            navigator.printMenu();
            int choice = getUserInput();
            if (choice == -1) {
                exit();
                break;
            }
            navigator.navigate(choice);
        }
    }

    private int getUserInput() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Выберите действие (для выхода введите -1): ");
        while (!scanner.hasNextInt()) {
            System.out.println("Некорректный ввод. Повторите: ");
            scanner.next();
        }

        return scanner.nextInt();
    }

    private void exit() {
        System.out.println("Выход из программы.");
    }
}
