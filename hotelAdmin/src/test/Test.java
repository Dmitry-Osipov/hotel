package test;

import person.Client;
import room.Room;
import room.RoomStatusTypes;
import service.Service;
import service.ServiceStatusTypes;
import controller.Hotel;

public class Test {
    public static void main(String[] args) {
        System.out.println("Проверка функционала отеля");
        Hotel hotel = new Hotel();
        Room room = new Room(1, 110, 2, 200);
        Service service = new Service(1, "Завтрак в номер", 2000);
        Client client = new Client(1, "Pavlov Pavel Pavlovich", "8-902-902-98-11");
        Client newClient = new Client(2, "Dmitrov Dmitry Dmitrievich", "8-953-180-00-61");
        System.out.println("Hotel: " + hotel);
        hotel.addRoom(room);
        hotel.addRoom(room);
        System.out.println("Hotel rooms: " + hotel.getRooms());
        hotel.addService(service);
        hotel.addService(service);
        System.out.println("Hotel services: " + hotel.getServices());
        System.out.println(hotel.checkIn(room, client, newClient));
        System.out.println("Hotel clients: " + hotel.getClients());
        if (hotel.checkIn(room, client)) {
            System.out.println("Поселили");
        } else {
            System.out.println("Не удалось поселить клиента");
        }
        if (hotel.checkIn(room)) {
            System.out.println("Поселили");
        } else {
            System.out.println("Не удалось поселить клиента");
        }
        hotel.evict(room, client);
        System.out.println("Room status: " + room.getStatus());
        System.out.println("Now in room: " + room.getClientsNowInRoom());
        hotel.evict(room, newClient);
        System.out.println("Now in room: " + room.getClientsNowInRoom());
        System.out.println("Hotel rooms: " + hotel.getRooms());
        System.out.println("---------------------------");
        System.out.println("Проверка функционала комнаты");
        room.setStatus(RoomStatusTypes.REPAIR);
        System.out.println(room.getStatus());
        room.setPrice(2500);
        System.out.println(room.getPrice());
        System.out.println("---------------------------");
        System.out.println("Проверка функционала услуги");
        service.setStatus(ServiceStatusTypes.PAID);
        System.out.println(service.getStatus());
        service.setPrice(3000);
        System.out.println(service.getPrice());
        System.out.println("---------------------------");
    }
}
