package utils;

import essence.Identifiable;
import essence.person.AbstractClient;
import essence.room.AbstractRoom;
import essence.service.AbstractService;
import service.ClientService;
import service.RoomService;
import service.ServiceService;
import utils.exceptions.ErrorMessages;
import utils.exceptions.NoEntityException;
import utils.printers.ServicesPrinter;
import utils.validators.FileValidator;

import java.io.File;
import java.sql.SQLException;
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
     * @throws SQLException если произошла ошибка SQL.
     */
    public static AbstractRoom getRoomByInput(RoomService roomService) throws NoEntityException, SQLException {
        List<AbstractRoom> rooms = roomService.roomsByStars();
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
    private static List<AbstractClient> getClients(ClientService clientService) throws NoEntityException, SQLException {
        List<AbstractClient> clients = clientService.getClients();
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
     * @throws SQLException если произошла ошибка SQL.
     */
    public static AbstractClient getClientByInput(ClientService clientService) throws SQLException {
        System.out.println("\nВыберите клиента: ");
        List<AbstractClient> clients = getClients(clientService);
        int choice = getEntityByInput(clients, false);
        return clients.get(choice);
    }

    /**
     * Пользователю требуется выбрать одного или нескольких клиентов.
     * @return Список клиентов.
     * @throws NoEntityException Ошибка связана с отсутствием клиентов.
     * @throws SQLException если произошла ошибка SQL.
     */
    public static List<AbstractClient> getManyClientsByInput(ClientService clientService) throws SQLException {
        System.out.println("\nСписок всех клиентов: ");
        List<AbstractClient> clients = getClients(clientService);

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
     * @throws SQLException если произошла ошибка SQL.
     */
    public static AbstractService getServiceByInput(ServiceService serviceService) throws NoEntityException,
            SQLException {
        List<AbstractService> services = serviceService.getServices();

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
        File file = new File(fileName);
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
