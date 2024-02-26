package ui;

import annotations.annotation.Autowired;
import annotations.annotation.Component;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.ClientService;
import service.RoomService;
import service.ServiceService;
import utils.exceptions.ErrorMessages;

import java.util.Scanner;

/**
 * Класс отвечает за работу UI.
 */
@Component
@Getter
@Setter
@ToString
@NoArgsConstructor
public class MenuController {
    private static final Logger logger = LoggerFactory.getLogger("AppProcess");
    @Autowired
    private Builder builder;
    @Autowired
    private Navigator navigator;
    @Autowired
    private RoomService roomService;
    @Autowired
    private ServiceService serviceService;
    @Autowired
    private ClientService clientService;

    /**
     * Класс отвечает за работу UI.
     */
    @Deprecated(forRemoval = true)
    public MenuController(RoomService roomService, ServiceService serviceService, ClientService clientService) {
        this.roomService = roomService;
        this.serviceService = serviceService;
        this.clientService = clientService;
        builder = new Builder(roomService, serviceService, clientService);
        builder.buildMenu();
        navigator = new Navigator(builder.getRootMenu());
    }

    /**
     * Метод запускает приложение отеля.
     */
    public void run() {
        logger.info("Запуск приложения");
        roomService.deserializeRoomsData();
        serviceService.deserializeServicesData();
        clientService.deserializeClientsData();
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
        logger.info("Выход из приложения");
        System.out.println("\nВыход из программы...");
        roomService.serializeRoomsData();
        serviceService.serializeServicesData();
        clientService.serializeClientsData();
    }
}
