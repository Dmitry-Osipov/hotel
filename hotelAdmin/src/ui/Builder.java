package ui;

import essence.person.AbstractClient;
import essence.person.Client;
import essence.room.AbstractRoom;
import essence.room.Room;
import essence.service.AbstractService;
import essence.service.Service;
import essence.service.ServiceNames;
import lombok.Getter;
import lombok.ToString;
import service.ClientService;
import service.RoomService;
import service.ServiceService;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

@Getter
@ToString
public class Builder {
    private Menu rootMenu;
    private final RoomService roomService;
    private final ServiceService serviceService;
    private final ClientService clientService;

    public Builder(RoomService roomService, ServiceService serviceService, ClientService clientService) {
        this.roomService = roomService;
        this.serviceService = serviceService;
        this.clientService = clientService;
    }

    public void buildMenu() {
        rootMenu = buildMainMenu();
    }

    private Menu buildMainMenu() {
        MenuItem roomsMenu = new MenuItem("Управление комнатами", null, buildRoomsMenu());
        MenuItem serviceMenu = new MenuItem("Управление услугами", null, buildServicesMenu());
        MenuItem clientMenu = new MenuItem("Управление клиентами", null, buildClientsMenu());

        return new Menu("Главное меню", new MenuItem[]{roomsMenu, serviceMenu, clientMenu});
    }

    private Menu buildRoomsMenu() {
        MenuItem addRoom = new MenuItem("Добавить комнату",
                () -> {
                    System.out.println("\nВведите номер, вместимость и цену комнаты через пробел: ");
                    String[] userInput = getUserInput().split(" ");

                    String result = roomService.addRoom(new Room(
                            Integer.parseInt(userInput[0]),
                            Integer.parseInt(userInput[1]),
                            Integer.parseInt(userInput[2]))) ? "Удалось добавить комнату"
                            : "Не удалось добавить комнату";
                    System.out.println("\n" + result);
                },
                null);

        MenuItem getPrice = new MenuItem("Вывести стоимость комнаты",
                () -> {
                    Room room = (Room) getRoomByInput();
                    System.out.println("\nСтоимость комнаты - " + roomService.getFavorPrice(room));
                },
                null);

        MenuItem addStars = new MenuItem("Добавить звёзд комнате",
                () -> {
                    AbstractRoom room = getRoomByInput();

                    System.out.println("\nВведите количесто звёзд (от 1 до 5): ");
                    int stars = getUserIntegerInput();

                    String result = roomService.addStarsToRoom(room, stars) ? "Добавление звёзд прошло успешно" :
                            "Введено недопустимое количество звёзд";
                    System.out.println("\n" + result);
                },
                null);

        MenuItem checkIn = new MenuItem("Заселить клиентов в комнату",
                () -> {
                    AbstractRoom room = getRoomByInput();
                    AbstractClient[] guests = convertListToArray(getManyClientsByInput(), AbstractClient.class);

                    String result = roomService.checkIn(room, guests) ? "Заселение прошло успешно" :
                            "Заселить не удалось";
                    System.out.println("\n" + result);
                },
                null);

        MenuItem evict = new MenuItem("Выселить клиентов из комнаты",
                () -> {
                    AbstractRoom room = getRoomByInput();
                    AbstractClient[] guests = convertListToArray(getManyClientsByInput(), AbstractClient.class);

                    String result = roomService.evict(room, guests) ? "Выселение прошло успешно" :
                            "Выселить не удалось";
                    System.out.println("\n" + result);
                },
                null);

        MenuItem roomsByStars = new MenuItem("Вывести список всех комнат, отсортированных по убыванию звёзд",
                () -> {
                    System.out.println("\nСписок всех комнат по убыванию звёзд: ");
                    printRooms(roomService.roomsByStars());
                },
                null);

        MenuItem roomsByPrice = new MenuItem("Высети список всех комнат, отсортированных по возрастанию цены",
                () -> {
                    System.out.println("\nСписок всех комнат по возрастанию цены: ");
                    printRooms(roomService.roomsByPrice());
                },
                null);

        MenuItem roomsByCapacity = new MenuItem(
                "Вывести список всех комнат, отсортированных по возрастанию вместимости",
                () -> {
                    System.out.println("\nСписок всех комнат по возрастанию вместимости: ");
                    printRooms(roomService.roomsByCapacity());
                },
                null);

        MenuItem availableRoomsByStars = new MenuItem(
                "Вывести список свободных комнат, отсортированных по убыванию звёзд",
                () -> {
                    System.out.println("\nСписок свобожных комнат по убыванию звёзд: ");
                    printRooms(roomService.availableRoomsByStars());
                },
                null);

        MenuItem availableRoomsByPrice = new MenuItem(
                "Вывести список свободных комнат, отсортированных по возрастанию цены",
                () -> {
                    System.out.println("\nСписок свободных комнат по возрастанию цены: ");
                    printRooms(roomService.availableRoomsByPrice());
                },
                null);

        MenuItem availableRoomsByCapacity = new MenuItem(
                "Вывести список свобожынх комнат, отсортированных по возрастанию вместимости",
                () -> {
                    System.out.println("\nСписок свободных комнат по возрастанию вместимости: ");
                    printRooms(roomService.availableRoomsByCapacity());
                },
                null);

        MenuItem countAvailableRooms = new MenuItem("Вывести количество свободных комнат",
                () -> System.out.println("\nКоличество свободных комнат - " + roomService.countAvailableRooms()),
                null);

        MenuItem getRoomLastClients = new MenuItem("Вывести список последних клиентов комнаты",
                () -> {
                    AbstractRoom room = getRoomByInput();

                    System.out.println("\nВведите количество последних клиентов: ");
                    int count = getUserIntegerInput();

                    List<AbstractClient> clients = roomService.getRoomLastClients(room, count);
                    System.out.println("\nПоследние клиенты комнаты: ");
                    for (int i = 0; i < clients.size(); i++) {
                        System.out.println(i+1 + ". " + clients.get(i));
                    }
                },
                null);

        MenuItem getRoomInfo = new MenuItem("Вывести полную информацию о комнате",
                () -> {
                    AbstractRoom room = getRoomByInput();
                    System.out.println("\nПолная информация о комнате - " + roomService.getRoomInfo((Room) room));
                },
                null);

        MenuItem getClientRoomsByNumbers = new MenuItem(
                "Вывести список всех комнат клиента, отстортированных по возрастанию номера комнаты",
                () -> {
                    AbstractClient client = getClientByInput();
                    System.out.println("\nСписок всех комнат клиента по возрастанию номера: ");
                    printRooms(roomService.getClientRoomsByNumbers(client));
                },
                null);

        MenuItem getClientRoomsByCheckOutTime = new MenuItem(
                "Вывести список всех комнат клиента, отсортированных по убыванию времени выезда",
                () -> {
                    AbstractClient client = getClientByInput();
                    System.out.println("\nСписок всех комнат клиента по убыванию времени выезда: ");
                    printRooms(roomService.getClientRoomsByCheckOutTime(client));
                },
                null);

        MenuItem getAvailableRoomsByTime = new MenuItem("Вывести список свободных комнат с конкретного времени",
                () -> {
                    System.out.println("\nВведите год, месяц, день, час и минуты через пробел: ");
                    String[] dateTime = getUserInput().split(" ");
                    System.out.println("\nСвободные комнаты с " + Arrays.toString(dateTime) + ": ");
                    printRooms(roomService.getAvailableRoomsByTime(
                            LocalDateTime.of(
                                    Integer.parseInt(dateTime[0]),
                                    Integer.parseInt(dateTime[1]),
                                    Integer.parseInt(dateTime[2]),
                                    Integer.parseInt(dateTime[3]),
                                    Integer.parseInt(dateTime[4])
                            )));
                },
                null);

        return new Menu("Управление комнатами", new MenuItem[]{addRoom, getPrice, addStars, checkIn, evict,
                roomsByStars, roomsByPrice, roomsByCapacity, availableRoomsByStars, availableRoomsByPrice,
                availableRoomsByCapacity, countAvailableRooms, getRoomLastClients, getRoomInfo, getClientRoomsByNumbers,
                getClientRoomsByCheckOutTime, getAvailableRoomsByTime});
    }

    private Menu buildServicesMenu() {
        MenuItem addService = new MenuItem("Добавить услугу",
                () -> {
                    System.out.println("\nВыберите название услуги: ");
                    ServiceNames[] names = ServiceNames.values();
                    for (int i = 0; i < names.length; i++) {
                        System.out.println(i+1 + ". " + names[i]);
                    }
                    ServiceNames name = names[getUserIntegerInput() - 1];

                    System.out.println("\nВведите стоимость услуги: ");
                    int cost = getUserIntegerInput();

                    String result = serviceService.addService(new Service(name, cost)) ? "Удалось добавить услугу" :
                            "Не удалось добавить услугу";
                    System.out.println("\n" + result);
                },
                null);

        MenuItem getPrice = new MenuItem("Вывести стоимость услуги",
                () -> {
                    Service service = (Service) getServiceByInput();
                    System.out.println("\nСтоимость услуги - " + serviceService.getFavorPrice(service));
                },
                null);

        MenuItem getServices = new MenuItem("Вывести список всех услуг",
                () -> {
                    System.out.println("\nСписок всех услуг: ");
                    printServices(serviceService.getServices());
                },
                null);

        MenuItem provideService = new MenuItem("Провести услугу клиенту",
                () -> {
                    AbstractClient client = getClientByInput();
                    AbstractService service = getServiceByInput();

                    String result = serviceService.provideService(client, service) ? "Удалось провести услугу" :
                            "Не удалось провести услугу";
                    System.out.println("\n" + result);
                },
                null);

        MenuItem getClientServicesByPrice = new MenuItem(
                "Вывести список услуг, оказанных клиенту и отсортированных по возрастанию цены",
                () -> {
                    AbstractClient client = getClientByInput();
                    System.out.println("\nСписок услуг, оказанных клиенту, по возрастанию цены: ");
                    printServices(serviceService.getClientServicesByPrice(client));
                },
                null);

        MenuItem getClientServicesByTime = new MenuItem(
                "Вывести список услуг, оказанных клиенту и отсортированных по убыванию времени оказания",
                () -> {
                    AbstractClient client = getClientByInput();
                    System.out.println("Список услуг, оказанных клиенту, по убыванию времени оказания: ");
                    printServices(serviceService.getClientServicesByTime(client));
                },
                null);

        return new Menu("Управление услугами", new MenuItem[]{addService, getPrice, getServices, provideService,
                getClientServicesByPrice, getClientServicesByTime});
    }

    private Menu buildClientsMenu() {
        MenuItem addClient = new MenuItem("Добавить клиента",
                () -> {
                    System.out.println("\nВведите ФИО клиента: ");
                    String name = getUserInput();

                    System.out.println("\nВведите номер телефона клиента: ");
                    String phone = getUserInput();

                    String result = clientService.addClient(new Client(name, phone)) ? "Удалось добавить клиента" :
                            "Не удалось добавить клиента";
                    System.out.println("\n" + result);
                },
                null);

        MenuItem getClients = new MenuItem("Вывести список всех клиентов",
                () -> {
                    List<AbstractClient> clients = clientService.getClients();

                    System.out.println("\nСписок всех клиентов: ");
                    for (int i = 0; i < clients.size(); i++) {
                        System.out.println(i+1 + ". " + clients.get(i));
                    }
                },
                null);

        MenuItem countClients = new MenuItem("Вывести количество клиентов",
                () -> System.out.println("\nКоличество клиентов - " + clientService.countClients()),
                null);

        return new Menu("Управление клиентами", new MenuItem[]{addClient, getClients, countClients});
    }

    private String getUserInput() {
        return new Scanner(System.in).nextLine();
    }

    private int getUserIntegerInput() {
        return new Scanner(System.in).nextInt();
    }

    private AbstractRoom getRoomByInput() {
        System.out.println("\nВыберите комнату: ");
        List<AbstractRoom> rooms = roomService.roomsByStars();
        for (int i = 0; i < rooms.size(); i++) {
            System.out.println(i+1 + ". " + rooms.get(i));
        }
        return rooms.get(getUserIntegerInput() - 1);
    }

    private List<AbstractClient> getClients() {
        List<AbstractClient> clients = clientService.getClients();
        for (int i = 0; i < clients.size(); i++) {
            System.out.println(i+1 + ". " + clients.get(i));
        }

        return clients;
    }

    private AbstractClient getClientByInput() {
        System.out.println("\nВыберите клиента: ");
        List<AbstractClient> clients = getClients();

        return clients.get(getUserIntegerInput() - 1);
    }

    private List<AbstractClient> getManyClientsByInput() {
        System.out.println("\nСписок всех клиентов: ");
        List<AbstractClient> clients = getClients();

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

    private void printRooms(List<AbstractRoom> rooms) {
        for (int i = 0; i < rooms.size(); i++) {
            System.out.println(i+1 + ". " + rooms.get(i));
        }
    }

    private AbstractService getServiceByInput() {
        System.out.println("\nВыберите услугу: ");
        List<AbstractService> services = serviceService.getServices();
        for (int i = 0; i < services.size(); i++) {
            System.out.println(i+1 + ". " + services.get(i));
        }

        return services.get(getUserIntegerInput() - 1);
    }

    private void printServices(List<AbstractService> services) {
        for (int i = 0; i < services.size(); i++) {
            System.out.println(i+1 + ". " + services.get(i));
        }
    }

    private <T> T[] convertListToArray (List<T> list, Class<T> elementType) {
        T[] array = (T[]) Array.newInstance(elementType, list.size());
        return list.toArray(array);
    }
}
