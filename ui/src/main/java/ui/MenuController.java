package ui;

import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import service.ClientService;
import service.RoomService;
import service.ServiceService;
import utils.exceptions.ErrorMessages;

import java.util.Scanner;

/**
 * Класс отвечает за работу UI.
 */
@Controller
@ToString
public class MenuController {
    private static final Logger logger = LoggerFactory.getLogger("AppProcess");
    private final Builder builder;
    private final Navigator navigator;
    private final RoomService roomService;
    private final ServiceService serviceService;
    private final ClientService clientService;

    @Autowired
    public MenuController(Builder builder, Navigator navigator, RoomService roomService, ServiceService serviceService,
                          ClientService clientService) {
        this.builder = builder;
        this.navigator = navigator;
        this.roomService = roomService;
        this.serviceService = serviceService;
        this.clientService = clientService;
    }

    /**
     * Метод запускает приложение отеля.
     */
    public void run() {
        logger.info("Запуск приложения");
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
            logger.info("Пользователь ввёл не целое число");
            System.out.println("\n" + ErrorMessages.INCORRECT_INPUT.getMessage());
            scanner.next();
        }

        int choice = scanner.nextInt();
        logger.info("Пользователь ввёл число {}", choice);
        return choice;
    }

    /**
     * Служебный метод производит user-friendly выход из программы.
     */
    private void exit() {
        logger.info("Вызван метод выхода из приложения");
        System.out.println("\nВыход из программы...");
        logger.info("Выход из приложения");
    }
}
