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
import utils.exceptions.EntityContainedException;
import utils.exceptions.InvalidDataException;
import utils.exceptions.NoEntityException;

import java.time.LocalDateTime;

public class TestHotel {
    public static void main(String[] args) {
        try {
            ClientRepository clients = ClientRepository.getInstance();
            ClientService cs = new ClientService(clients);
            Client client1 = new Client(1, "Osipov Dmitry Romanovich", "8-902-902-98-11");
            Client client2 = new Client(2, "Musofranova Nadezhda Sergeevna", "8-961-120-09-91");
            try {
                cs.addClient(client1);
                System.out.println("Удалось добавить клиента");
            } catch (EntityContainedException e) {
                System.out.println("Не удалось добавить клиента");
            }
            System.out.println("Количество клиентов: " + cs.countClients());
            System.out.println("Все клиенты: " + cs.getClients());

            RoomReservationRepository reservations = RoomReservationRepository.getInstance();
            RoomRepository rooms = RoomRepository.getInstance();
            RoomService rs = new RoomService(rooms, reservations);
            Room room1 = new Room(1, 110, 2, 1000);
            String result = room1.getPrice() == room1.getMIN_PRICE() ? "Установлена цена по умолчанию"
                    : "Автопроверка дала сбой";
            System.out.println(result);
            Room room2 = new Room(2, 111, 5, 3000);
            Room room3 = new Room(3, 112, 2, 9000);
            Room room4 = new Room(4, 113, 3, 15000);
            rs.addRoom(room4);
            rs.addRoom(room3);
            rs.addStarsToRoom(room4, 5);
            rs.addStarsToRoom(room3, 1);
            rs.addRoom(room1);
            try {
                rs.addRoom(room2);
                System.out.println("Комната успешно добавлена");
            } catch (EntityContainedException e) {
                System.out.println("Комната не была добавлена");
            }

            try {
                rs.checkIn(room1, client1);
                System.out.println("Удалось заселить");
            } catch (InvalidDataException e) {
                System.out.println("Не удалось заселить");
            }
            Thread.sleep(5000);
            try {
                rs.checkIn(room1);
                System.out.println("Удалось заселить в пустую комнату");
            } catch (InvalidDataException e) {
                System.out.println("Не удалось заселить в пустую комнату");
            }
            try {
                rs.evict(room1, client1);
                System.out.println("Удалось выселить");
                rs.evict(room1);
                System.out.println("Удалось выселить без клиентов");
            } catch (InvalidDataException e) {
                System.out.println("Не удалось выселить без клиентов");
            }
            rs.addStarsToRoom(room1, 4);
            System.out.println("Удалось поставить оценку");
            try {
                rs.addStarsToRoom(room1, 7);
                System.out.println("Удалось поставить неверную оценку");
            } catch (InvalidDataException e) {
                System.out.println("Не удалось поставить неверную оценку");
            }
            System.out.println("Комнаты по звёздам: " + rs.roomsByStars());
            System.out.println(rs.roomsByCapacity());
            System.out.println(rs.roomsByPrice());
            rs.checkIn(room1, client1);
            System.out.println("Свободные комнаты по звёздам: " + rs.availableRoomsByStars());
            System.out.println(rs.availableRoomsByCapacity());
            System.out.println(rs.availableRoomsByPrice());
            System.out.println("Количество свободных комнат: " + rs.countAvailableRooms());
            rs.evict(room1, client1);
            Thread.sleep(2000);
            rs.checkIn(room2, client1, client2);

            System.out.println("История комнаты 1: ");
            System.out.println(rs.getRoomLastClients(room1, 2));
            System.out.println("История комнаты 2: ");
            System.out.println(rs.getRoomLastClients(room2, 3));
            System.out.println("История комнаты 2: ");
            System.out.println(rs.getRoomLastClients(room2, -1));
            System.out.println("Полная информация о комнате 2: " + rs.getRoomInfo(room2));
            System.out.println("Комнаты клиента по номерам: " + rs.getClientRoomsByNumbers(client1));
            System.out.println("Комнаты клиента по времени: " + rs.getClientRoomsByCheckOutTime(client1));
            for (int i = 0; i < 2; i++) {
                System.out.println(rs.roomsByStars().get(i).getCheckOutTime());
            }
            System.out.println("Комнаты, которые свободны по времени: " + rs.getAvailableRoomsByTime(
                    LocalDateTime.of(2024, 1, 8, 21, 0)));
            System.out.println("Цена комнаты: " + rs.getFavorPrice(room1));

            ProvidedServicesRepository providedServicesRepository = ProvidedServicesRepository.getInstance();
            ServiceRepository serviceRepository = ServiceRepository.getInstance();
            ServiceService ss = new ServiceService(serviceRepository, providedServicesRepository);
            Service service1 = new Service(1, ServiceNames.CLEANING, 4000);
            Service service2 = new Service(2, ServiceNames.BREAKFAST, 2000);
            Service service3 = new Service(3, ServiceNames.CONFERENCE, 4000);
            Service service4 = new Service(4, ServiceNames.EXCURSION, 7000);
            ss.addService(service3);
            ss.addService(service4);
            ss.addService(service2);
            try {
                ss.addService(service1);
                System.out.println("Удалось добавить услугу");
            } catch (EntityContainedException e) {
                System.out.println("Не удалось добавить услугу");
            }
            try {
                ss.provideService(client1, service1);
                System.out.println("Удалось оказать услугу");
            } catch (NoEntityException e) {
                System.out.println("Не удалось оказать услугу");
            }
            Thread.sleep(2000);
            ss.provideService(client1, service2);
            Thread.sleep(3000);
            ss.provideService(client1, service3);
            Thread.sleep(1000);
            ss.provideService(client1, service4);
            System.out.println("Услуги по цене: " + ss.getClientServicesByPrice(client1));
            System.out.println("Услуги по времени: " + ss.getClientServicesByTime(client1));
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }
}
