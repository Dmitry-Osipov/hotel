package test;

import essence.person.Client;
import essence.room.Room;
import essence.service.Service;
import essence.service.ServiceNames;
import repository.client.ClientRepository;
import repository.room.RoomRepository;
import repository.room.RoomReservationRepository;
import repository.service.ProvidedServicesRepository;
import repository.service.ServiceRepository;
import service.ClientService;
import service.RoomService;
import service.ServiceService;

import java.time.LocalDateTime;
import java.util.Scanner;

public class Temp {
    public static void main(String[] args) {
        RoomRepository rooms = new RoomRepository();
        Room room = new Room(100, 2, 3000);
        RoomReservationRepository reservations = new RoomReservationRepository();
        RoomService roomService = new RoomService(rooms, reservations);
        roomService.addRoom(room);

        ServiceRepository services = new ServiceRepository();
        Service service = new Service(ServiceNames.FITNESS, 5000);
        ProvidedServicesRepository providedServices = new ProvidedServicesRepository();
        ServiceService serviceService = new ServiceService(services, providedServices);
        serviceService.addService(service);

        ClientRepository clients = new ClientRepository();
        Client client = new Client("Osipov Dmitry Romanovich", "8-902-902-98-11");
        ClientService clientService = new ClientService(clients);
        clientService.addClient(client);

        Scanner scanner = new Scanner(System.in);
        System.out.println("Главное меню: ");
        System.out.println("1. Управление комнатами");
        System.out.println("2. Управление услугами");
        System.out.println("3. Управление клиентами");
        int choice = scanner.nextInt();
        switch (choice) {
            case 1 -> {
                System.out.println("Переключаю на управление комнатами");
                System.out.println("Введите действие:");
                System.out.println("1. Добавить комнату");
                System.out.println("2. Заселить в комнату");
                System.out.println("3. Добавить звёзд комнате");
                System.out.println("4. Выселить из комнаты");
                System.out.println("5. Вывести список комнат по убыванию звёзд");
                System.out.println("6. Вывести список комнат по возрастанию цены");
                System.out.println("7. Вывести список комнат по возрастанию вместимости");
                System.out.println("8. Вывести список свободных комнат по убыванию звёзд");
                System.out.println("9. Вывести список свободных комнат по возрастанию цены");
                System.out.println("10. Вывести список свободных комнат по возрастанию вместимости");
                System.out.println("11. Посчитать количество свободных комнат");
                System.out.println("12. Вывести полную информацию о комнате");
                System.out.println("13. Вывести список комнат клиента по возрастанию номера комнаты");
                System.out.println("14. Вывести список комнат клиента по убыванию времени выезда");
                System.out.println("15. Вывести список свободных комнат с определённого времени");
                System.out.println("16. Посчитать цену номера");
                choice = scanner.nextInt();
                switch (choice) {
                    case 1 -> System.out.println(roomService.addRoom(new Room(101, 5, 10000)));
                    case 2 -> System.out.println(roomService.checkIn(room, client));
                    case 3 -> System.out.println(roomService.addStarsToRoom(room, 4));
                    case 4 -> System.out.println(roomService.evict(room, client));
                    case 5 -> System.out.println(roomService.roomsByStars());
                    case 6 -> System.out.println(roomService.roomsByPrice());
                    case 7 -> System.out.println(roomService.roomsByCapacity());
                    case 8 -> System.out.println(roomService.availableRoomsByStars());
                    case 9 -> System.out.println(roomService.availableRoomsByPrice());
                    case 10 -> System.out.println(roomService.availableRoomsByCapacity());
                    case 11 -> System.out.println(roomService.countAvailableRooms());
                    case 12 -> System.out.println(roomService.getRoomInfo(room));
                    case 13 -> System.out.println(roomService.getClientRoomsByNumbers(client));
                    case 14 -> System.out.println(roomService.getClientRoomsByCheckOutTime(client));
                    case 15 -> System.out.println(roomService.getAvailableRoomsByTime(
                            LocalDateTime.of(2024, 1, 15, 18, 35, 0)));
                    case 16 -> System.out.println(roomService.getFavorPrice(room));
                    default -> System.out.println("Ошибка ввода");
                }
            }
            case 2 -> {
                System.out.println("Переключаю на управление услугами");
                System.out.println("1. Добавить услугу");
                System.out.println("2. Оказать услугу клиенту");
                System.out.println("3. Вывести список услуг клиента по возрастанию цены");
                System.out.println("4. Вывести список услуг клиента по убыванию времени оказания");
                System.out.println("5. Посчитать цену услуги");
                choice = scanner.nextInt();
                switch (choice) {
                    case 1 -> System.out.println(serviceService.addService(
                            new Service(ServiceNames.CONFERENCE, 3500)));
                    case 2 -> System.out.println(serviceService.provideService(client, service));
                    case 3 -> System.out.println(serviceService.getClientServicesByPrice(client));
                    case 4 -> System.out.println(serviceService.getClientServicesByTime(client));
                    case 5 -> System.out.println(serviceService.getFavorPrice(service));
                    default -> System.out.println("Ошибка ввода");
                }
            }
            case 3 -> {
                System.out.println("Переключаю на управление клиентами");
                System.out.println("1. Добавить клиента");
                System.out.println("2. Вывести всех клиентов");
                System.out.println("3. Посчитать количество клиентов");
                choice = scanner.nextInt();
                switch (choice) {
                    case 1 -> System.out.println(clientService.addClient(new Client(
                            "Musofranova Nadezhda Sergeevna", "8-961-150-09-91")));
                    case 2 -> System.out.println(clientService.getClients());
                    case 3 -> System.out.println(clientService.countClients());
                    default -> System.out.println("Ошибка ввода");
                }

            }
            default -> System.out.println("Ошибка ввода");
        }

    }
}
