package test;

import essence.person.Client;
import essence.room.Room;
import essence.service.Service;
import repository.ClientRepository;
import repository.RoomRepository;
import repository.ServiceRepository;
import service.ClientService;
import service.RoomService;
import service.ServiceService;

import java.time.LocalDateTime;

public class TestHotel {
    public static void main(String[] args) {
        ClientRepository clients = new ClientRepository();
        ClientService cs = new ClientService(clients);
        Client client1 = new Client(1, "Osipov Dmitry Romanovich", "8-902-902-98-11");
        String result = cs.addClient(client1) ? "Удалось добавить клиента" : "Не удалось добавить клиента";
        System.out.println(result);
        System.out.println("Количество клиентов: " + cs.countClients());
        System.out.println("Все клиенты: " + cs.getClients());

        RoomRepository rooms = new RoomRepository();
        RoomService rs = new RoomService(rooms);
        Room room1 = new Room(1, 110, 2, 1000);
        result = room1.getPrice() == room1.getMIN_PRICE() ? "Установлена цена по умолчанию" : "Автопроверка дала сбой";
        System.out.println(result);
        Room room2 = new Room(2, 111, 5, 3000);
        Room room3 = new Room(3, 112, 2, 9000);
        Room room4 = new Room(4, 113, 3, 15000);
        rs.addRoom(room4);
        rs.addRoom(room3);
        rs.addStarsToRoom(room4, 5);
        rs.addStarsToRoom(room3, 1);
        rs.addRoom(room1);
        result = rs.addRoom(room2) ? "Комната успешно добавлена" : "Комната не была добавлена";
        System.out.println(result);

        result = rs.checkIn(room1, client1) ? "Удалось заселить" : "Не удалось заселить";
        System.out.println(result);
        result = rs.checkIn(room1) ? "Удалось заселить в пустую комнату" : "Не удалось заселить в пустую комнату";
        System.out.println(result);
        result = rs.evict(room1, client1) ? "Удалось выселить" : "Не удалось выселить";
        System.out.println(result);
        result = rs.evict(room1)? "Удалось выселить без клиентов" : "Не удалось выселить без клиентов";
        System.out.println(result);
        result = rs.addStarsToRoom(room1, 4) ? "Удалось поставить оценку" : "Не удалось поставить оценку";
        System.out.println(result);
        result = rs.addStarsToRoom(room1, 7) ? "Удалось поставить неверную оценку" :
                "Не удалось поставить неверную оценку";
        System.out.println(result);
        System.out.println("Комнаты по звёздам: " + rs.roomsByStars());
        System.out.println(rs.roomsByCapacity());
        System.out.println(rs.roomsByPrice());
        rs.checkIn(room1, client1);
        System.out.println("Свободные комнаты по звёздам: " + rs.availableRoomsByStars());
        System.out.println(rs.availableRoomsByCapacity());
        System.out.println(rs.availableRoomsByPrice());
        System.out.println("Количество свободных комнат: " + rs.countAvailableRooms());
        rs.evict(room1, client1);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
        rs.checkIn(room2, client1);

        System.out.println("История комнаты 1: ");
        rs.getRoomLastClients(room1, 2);
        System.out.println("История комнаты 2: ");
        rs.getRoomLastClients(room2, 3);
        rs.getRoomLastClients(room2, -1);
        System.out.println("Полная информация о комнате 2: " + rs.getRoomInfo(room2));
        System.out.println("Комнаты клиента по номерам: " + rs.getClientRoomsByNumbers(client1));
        System.out.println("Комнаты клиента по времени: " + rs.getClientRoomsByCheckOutTime(client1));
        for (int i = 0; i < 2; i++) {
            System.out.println(rs.roomsByStars().get(i).getCheckOutTime());
        }
        System.out.println("Комнаты, которые свободны по времени: " + rs.getAvailableRoomsByTime(
                LocalDateTime.of(2024, 1, 8, 21, 0)));
        System.out.println("Цена комнаты: " + rs.getFavorPrice(room1));

        ServiceRepository serviceRepository = new ServiceRepository();
        ServiceService ss = new ServiceService(serviceRepository);
        Service service1 = new Service(1, "Погладить вещи", 4000);
        Service service2 = new Service(2, "Поменять номер", 2000);
        Service service3 = new Service(3, "Постирать вещи", 4000);
        Service service4 = new Service(4, "Принести завтрак", 7000);
        ss.addService(service3);
        ss.addService(service4);
        ss.addService(service2);
        result = ss.addService(service1) ? "Удалось добавить услугу" : "Не удалось добавить услугу";
        System.out.println(result);
        result = ss.provideService(client1, service1) ? "Удалось оказать услугу" : "Не удалось оказать услугу";
        System.out.println(result);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
        ss.provideService(client1, service2);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
        ss.provideService(client1, service3);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
        ss.provideService(client1, service4);
        System.out.println("Услуги по цене: " + ss.getClientServicesByPrice(client1));
        System.out.println("Услуги по времени: " + ss.getClientServicesByTime(client1));
    }
}
