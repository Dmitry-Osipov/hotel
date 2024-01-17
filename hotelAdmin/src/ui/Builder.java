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

import java.time.LocalDateTime;
import java.util.ArrayList;
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

                    System.out.println("\n" + roomService.addRoom(new Room(
                            Integer.parseInt(userInput[0]),
                            Integer.parseInt(userInput[1]),
                            Integer.parseInt(userInput[2]))));
                },
                null);

        MenuItem getPrice = new MenuItem("Вывести стоимость комнаты",
                () -> {
                    Room room = (Room) getRoomByInput();
                    System.out.println("\n" + roomService.getFavorPrice(room));
                },
                null);

        MenuItem addStars = new MenuItem("Добавить звёзд комнате",
                () -> {
                    AbstractRoom room = getRoomByInput();

                    System.out.println("\nВведите количесто звёзд (от 1 до 5): ");
                    int stars = getUserIntegerInput();

                    System.out.println("\n" + roomService.addStarsToRoom(room, stars));
                },
                null);

        MenuItem checkIn = new MenuItem("Заселить клиентов в комнату",
                () -> {
                    AbstractRoom room = getRoomByInput();
                    AbstractClient[] clients = getManyClientsByInput();

                    System.out.println("\n" + roomService.checkIn(room, clients));
                },
                null);

        MenuItem evict = new MenuItem("Выселить клиентов из комнаты",
                () -> {
                    AbstractRoom room = getRoomByInput();
                    AbstractClient[] clients = getManyClientsByInput();

                    System.out.println("\n" + roomService.evict(room, clients));
                },
                null);

        MenuItem roomsByStars = new MenuItem("Вывести список всех комнат, отсортированных по убыванию звёзд",
                () -> printRooms(roomService.roomsByStars()),
                null);

        MenuItem roomsByPrice = new MenuItem("Высети список всех комнат, отсортированных по возрастанию цены",
                () -> printRooms(roomService.roomsByPrice()),
                null);

        MenuItem roomsByCapacity = new MenuItem(
                "Вывести список всех комнат, отсортированных по возрастанию вместимости",
                () -> printRooms(roomService.roomsByCapacity()),
                null);

        MenuItem availableRoomsByStars = new MenuItem(
                "Вывести список свободных комнат, отсортированных по убыванию звёзд",
                () -> printRooms(roomService.availableRoomsByStars()),
                null);

        MenuItem availableRoomsByPrice = new MenuItem(
                "Вывести список свободных комнат, отсортированных по возрастанию цены",
                () -> printRooms(roomService.availableRoomsByPrice()),
                null);

        MenuItem availableRoomsByCapacity = new MenuItem(
                "Вывести список свобожынх комнат, отсортированных по возрастанию вместимости",
                () -> printRooms(roomService.availableRoomsByCapacity()),
                null);

        MenuItem countAvailableRooms = new MenuItem("Вывести количество свободных комнат",
                () -> System.out.println("\n" + roomService.countAvailableRooms()),
                null);

        MenuItem getRoomLastClients = new MenuItem("Вывести список последних клиентов комнаты",
                () -> {
                    AbstractRoom room = getRoomByInput();
                    System.out.println("\nВведите количество последних клиентов: ");
                    int count = getUserIntegerInput();
                    List<AbstractClient> clients = roomService.getRoomLastClients(room, count);
                    for (int i = 0; i < clients.size(); i++) {
                        System.out.println(i + ". " + clients.get(i));
                    }
                },
                null);

        MenuItem getRoomInfo = new MenuItem("Вывести полную информацию о комнате",
                () -> {
                    AbstractRoom room = getRoomByInput();
                    System.out.println("\n" + roomService.getRoomInfo((Room) room));
                },
                null);

        MenuItem getClientRoomsByNumbers = new MenuItem(
                "Вывести список всех комнат клиента, отстортированных по возрастанию номера комнаты",
                () -> {
                    AbstractClient client = getClientByInput();
                    printRooms(roomService.getClientRoomsByNumbers(client));
                },
                null);

        MenuItem getClientRoomsByCheckOutTime = new MenuItem(
                "Вывести список всех комнат клиента, отсортированных по убыванию времени выезда",
                () -> {
                    AbstractClient client = getClientByInput();
                    printRooms(roomService.getClientRoomsByCheckOutTime(client));
                },
                null);

        MenuItem getAvailableRoomsByTime = new MenuItem("Вывести список свободных комнат с конкретного времени",
                () -> {
                    System.out.println("\nВведите год, месяц, день, час и минуты через пробел: ");
                    String[] dateTime = getUserInput().split(" ");
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
                        System.out.println(i + ". " + names[i]);
                    }
                    ServiceNames name = names[getUserIntegerInput()];

                    System.out.println("\nВведите стоимость услуги: ");
                    int cost = getUserIntegerInput();

                    System.out.println(serviceService.addService(new Service(name, cost)));
                },
                null);

        MenuItem getPrice = new MenuItem("Вывести стоимость услуги",
                () -> {
                    Service service = (Service) getServiceByInput();
                    System.out.println("\n" + serviceService.getFavorPrice(service));
                },
                null);

        MenuItem getServices = new MenuItem("Вывести список всех услуг",
                () -> printServices(serviceService.getServices()),
                null);

        MenuItem provideService = new MenuItem("Провести услугу клиенту",
                () -> {
                    AbstractClient client = getClientByInput();
                    AbstractService service = getServiceByInput();

                    System.out.println(serviceService.provideService(client, service));
                },
                null);

        MenuItem getClientServicesByPrice = new MenuItem(
                "Вывести список услуг, оказанных клиенту и отсортированных по возрастанию цены",
                () -> {
                    AbstractClient client = getClientByInput();
                    printServices(serviceService.getClientServicesByPrice(client));
                },
                null);

        MenuItem getClientServicesByTime = new MenuItem(
                "Вывести список услуг, оказанных клиенту и отсортированных по убыванию времени оказания",
                () -> {
                    AbstractClient client = getClientByInput();
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

                    System.out.println(clientService.addClient(new Client(name, phone)));
                },
                null);

        MenuItem getClients = new MenuItem("Вывести список всех клиентов",
                () -> {
                    List<AbstractClient> clients = clientService.getClients();

                    System.out.println();
                    for (int i = 0; i < clients.size(); i++) {
                        System.out.println(i + ". " + clients.get(i));
                    }
                },
                null);

        MenuItem countClients = new MenuItem("Вывести количество клиентов",
                () -> System.out.println("\n" + clientService.countClients()),
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
            System.out.println(i + ". " + rooms.get(i));
        }
        return rooms.get(getUserIntegerInput());
    }

    private List<AbstractClient> getClients() {
        List<AbstractClient> clients = clientService.getClients();
        for (int i = 0; i < clients.size(); i++) {
            System.out.println(i + ". " + clients.get(i));
        }

        return clients;
    }

    private AbstractClient getClientByInput() {
        System.out.println("\nВыберите клиента: ");
        List<AbstractClient> clients = getClients();

        return clients.get(getUserIntegerInput());
    }

    private AbstractClient[] getManyClientsByInput() {
        System.out.println("\nВыберите клиентов (для выхода введите -1): ");
        List<AbstractClient> clients = getClients();

        List<AbstractClient> guests = new ArrayList<>(2);
        while (true) {
            int choice = getUserIntegerInput();
            if (choice == -1) {
                break;
            }
            guests.add(clients.get(choice));
        }

        return (AbstractClient[]) guests.toArray();
    }

    private void printRooms(List<AbstractRoom> rooms) {
        System.out.println();
        for (int i = 0; i < rooms.size(); i++) {
            System.out.println(i + ". " + rooms.get(i));
        }
    }

    private AbstractService getServiceByInput() {
        System.out.println("\nВыберите услугу: ");
        List<AbstractService> services = serviceService.getServices();
        for (int i = 0; i < services.size(); i++) {
            System.out.println(i + ". " + services.get(i));
        }

        return services.get(getUserIntegerInput());
    }

    private void printServices(List<AbstractService> services) {
        System.out.println();
        for (int i = 0; i < services.size(); i++) {
            System.out.println(i + ". " + services.get(i));
        }
    }
}
