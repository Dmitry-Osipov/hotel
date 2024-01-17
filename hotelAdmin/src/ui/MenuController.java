package ui;

import lombok.Getter;
import lombok.ToString;
import repository.client.ClientRepository;
import repository.room.RoomRepository;
import repository.room.RoomReservationRepository;
import repository.service.ProvidedServicesRepository;
import repository.service.ServiceRepository;
import service.ClientService;
import service.RoomService;
import service.ServiceService;

import java.util.Scanner;

@Getter
@ToString
public class MenuController {
    private final Builder builder;
    private final Navigator navigator;

    public MenuController() {
        builder = new Builder(
                new RoomService(new RoomRepository(), new RoomReservationRepository()),
                new ServiceService(new ServiceRepository(), new ProvidedServicesRepository()),
                new ClientService(new ClientRepository()));

        builder.buildMenu();
        navigator = new Navigator(builder.getRootMenu());
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
        System.out.println("\nВыберите действие (для выхода введите -1): ");
        while (!scanner.hasNextInt()) {
            System.out.println("\nНекорректный ввод. Повторите: ");
            scanner.next();
        }

        return scanner.nextInt();
    }

    private void exit() {
        System.out.println("\nВыход из программы...");
    }
}
