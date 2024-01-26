package ui.utils;

import essence.person.AbstractClient;
import essence.room.AbstractRoom;
import essence.service.AbstractService;
import repository.client.ClientRepository;
import repository.room.RoomRepository;
import repository.room.RoomReservationRepository;
import repository.service.ProvidedServicesRepository;
import repository.service.ServiceRepository;
import service.ClientService;
import service.RoomService;
import service.ServiceService;
import ui.utils.printers.ServicesPrinter;
import ui.utils.validators.FileValidator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Класс является финальным и предоставляет статические методы для обработки ввода пользователя.
 */
public final class InputHandler {
    private InputHandler() {
    }

    /**
     * Метод возвращает строку, введённую пользователем.
     * @return Полная строка, введённая пользователем.
     */
    public static String getUserInput() {
        return new Scanner(System.in).nextLine();
    }

    /**
     * Метод запрашивает у пользователя число, проверяя число на валидность.
     * @return Первое число, которое введёт пользователь.
     */
    public static int getUserIntegerInput() {
        Scanner scanner = new Scanner(System.in);
        while (!scanner.hasNextInt()) {
            System.out.println("\n" + ErrorMessages.INCORRECT_INPUT.getMessage());
            scanner.next();
        }

        return scanner.nextInt();
    }

    /**
     * Метод выводит в консоль все комнаты, отсортированные в порядке убывания звёзд. Пользователю требуется выбрать
     * какой-либо из номеров.
     * @return Номер или null, если список комнат пуст.
     */
    public static AbstractRoom getRoomByInput() {
        List<AbstractRoom> rooms =
                new RoomService(RoomRepository.getInstance(), RoomReservationRepository.getInstance()).roomsByStars();
        if (rooms.isEmpty()) {
            return null;
        }

        System.out.println("\nВыберите комнату: ");
        for (int i = 0; i < rooms.size(); i++) {
            System.out.println(i+1 + ". " + rooms.get(i));
        }

        return rooms.get(getUserIntegerInput() - 1);
    }

    /**
     * Служебный метод предназначен для снижения дублирования кода. Метод выводит в консоль всех клиентов.
     * @return Список клиентов.
     */
    private static List<AbstractClient> getClients() {
        List<AbstractClient> clients = new ClientService(ClientRepository.getInstance()).getClients();
        if (clients.isEmpty()) {
            return clients;
        }

        for (int i = 0; i < clients.size(); i++) {
            System.out.println(i+1 + ". " + clients.get(i));
        }

        return clients;
    }

    /**
     * Пользователю требуется выбрать какого-либо клиента.
     * @return Клиент или null, если список клиентов пуст.
     */
    public static AbstractClient getClientByInput() {
        System.out.println("\nВыберите клиента: ");
        List<AbstractClient> clients = getClients();
        if (clients.isEmpty()) {
            return null;
        }

        return clients.get(getUserIntegerInput() - 1);
    }

    /**
     * Пользователю требуется выбрать одного или нескольких клиентов.
     * @return Список клиентов.
     */
    public static List<AbstractClient> getManyClientsByInput() {
        System.out.println("\nСписок всех клиентов: ");
        List<AbstractClient> clients = getClients();
        if (clients.isEmpty()) {
            return clients;
        }

        List<AbstractClient> guests = new ArrayList<>(2);
        while (true) {
            System.out.println("\nВыберите клиента (для выхода введите -1): ");
            int choice = getUserIntegerInput() - 1;
            if (choice == -2) {
                break;
            }
            guests.add(clients.get(choice));
        }

        return guests;
    }

    /**
     * Пользователю требуется выбрать услугу.
     * @return Услуга или null, если список услуг пуст.
     */
    public static AbstractService getServiceByInput() {
        List<AbstractService> services =
                new ServiceService(ServiceRepository.getInstance(), ProvidedServicesRepository.getInstance())
                        .getServices();

        if(services.isEmpty()) {
            return null;
        }

        System.out.println("\nВыберите услугу: ");
        ServicesPrinter.printServices(services);

        return services.get(getUserIntegerInput() - 1);
    }

    /**
     * Метод запрашивает у пользователя ответ, требуется ли перезаписать существующий файл.
     * @param fileName Имя файла. Обязательно прописывать полностью имя файла, иначе будет файл храниться в корне.
     * @return Решение пользователя о перезаписи файла или дозаписи данных в пустой файл.
     */
    public static String getUserOverwriteChoice(String fileName) {
        File file = new File(fileName + ".csv");
        String choice = "да";

        if (file.exists()) {
            System.out.println("Вы уверены, что хотите перезаписать файл? (Да/нет): ");
            choice = InputHandler.getUserInput().toLowerCase();
        }

        return choice;
    }

    /**
     * Метод getFileNameFromUser предназначен для получения названия файла от пользователя.
     * @return Название файла, введенное пользователем и прошедшее проверку на корректность.
     */
    public static String getFileNameFromUser() {
        System.out.println("\nВведите название файла без указания расширения. Название может состоять только из " +
                "латинских букв, цифр, подчёркивания и тире: ");
        String fileName = InputHandler.getUserInput();
        while (!FileValidator.isValidFileName(fileName)) {
            System.out.println("\n" + ErrorMessages.INCORRECT_INPUT.getMessage());
            fileName = InputHandler.getUserInput();
        }

        return fileName;
    }
}
