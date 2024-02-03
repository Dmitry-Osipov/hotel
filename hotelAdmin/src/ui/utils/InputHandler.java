package ui.utils;

import essence.Identifiable;
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
import ui.utils.exceptions.ErrorMessages;
import ui.utils.exceptions.NoEntityException;
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
     * @return Номер.
     * @throws NoEntityException Ошибка связана с отсутствием комнат.
     */
    public static AbstractRoom getRoomByInput() throws NoEntityException {
        List<AbstractRoom> rooms =
                new RoomService(RoomRepository.getInstance(), RoomReservationRepository.getInstance()).roomsByStars();
        if (rooms.isEmpty()) {
            throw new NoEntityException(ErrorMessages.NO_ROOMS.getMessage());
        }

        System.out.println("\nВыберите комнату: ");
        for (int i = 0; i < rooms.size(); i++) {
            System.out.println(i+1 + ". " + rooms.get(i));
        }
        int choice = getEntityByInput(rooms, false);

        return rooms.get(choice);
    }

    /**
     * Служебный метод предназначен для снижения дублирования кода. Метод выводит в консоль всех клиентов.
     * @return Список клиентов.
     * @throws NoEntityException Ошибка связана с отсутствием клиентов.
     */
    private static List<AbstractClient> getClients() throws NoEntityException {
        List<AbstractClient> clients = new ClientService(ClientRepository.getInstance()).getClients();
        if (clients.isEmpty()) {
            throw new NoEntityException(ErrorMessages.NO_CLIENTS.getMessage());
        }

        for (int i = 0; i < clients.size(); i++) {
            System.out.println(i+1 + ". " + clients.get(i));
        }

        return clients;
    }

    /**
     * Пользователю требуется выбрать какого-либо клиента.
     * @return Клиент.
     * @throws NoEntityException Ошибка связана с отсутствием клиентов.
     */
    public static AbstractClient getClientByInput() {
        System.out.println("\nВыберите клиента: ");
        List<AbstractClient> clients = getClients();
        int choice = getEntityByInput(clients, false);
        return clients.get(choice);
    }

    /**
     * Пользователю требуется выбрать одного или нескольких клиентов.
     * @return Список клиентов.
     * @throws NoEntityException Ошибка связана с отсутствием клиентов.
     */
    public static List<AbstractClient> getManyClientsByInput() {
        System.out.println("\nСписок всех клиентов: ");
        List<AbstractClient> clients = getClients();

        List<AbstractClient> guests = new ArrayList<>(2);
        while (true) {
            System.out.println("\nВыберите клиента (для выхода введите -1): ");
            int choice = getEntityByInput(clients, true);
            if (choice == -2) {
                break;
            }
            guests.add(clients.get(choice));
        }

        return guests;
    }

    /**
     * Пользователю требуется выбрать услугу.
     * @return Услуга.
     * @throws NoEntityException Ошибка связана с отсутствием услуг.
     */
    public static AbstractService getServiceByInput() throws NoEntityException {
        List<AbstractService> services =
                new ServiceService(ServiceRepository.getInstance(), ProvidedServicesRepository.getInstance())
                        .getServices();

        if (services.isEmpty()) {
            throw new NoEntityException(ErrorMessages.NO_SERVICES.getMessage());
        }

        System.out.println("\nВыберите услугу: ");
        ServicesPrinter.printServices(services);
        int choice = getEntityByInput(services, false);
        return services.get(choice);
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

    /**
     * Служебный метод предназначен для снижения дублирования кода. Метод получает услугу, клиента или комнату от
     * пользователя.
     * @param list Список сущностей.
     * @param manyEntity {@code true}, если нужно получить несколько сущностей. {@code false}, если нужно получить одну
     * сущность.
     * @return Выбор пользователя.
     */
    private static <T extends Identifiable> int getEntityByInput(List<T> list, boolean manyEntity) {
        int choice = getUserIntegerInput() - 1;
        while (choice < 0 || list.size() < choice) {
            if (manyEntity && choice == -2) {
                break;
            }

            System.out.println("\n" + ErrorMessages.INCORRECT_INPUT.getMessage());
            choice = getUserIntegerInput() - 1;
        }

        return choice;
    }
}
