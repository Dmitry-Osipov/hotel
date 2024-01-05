package test;

import controller.Hotel;
import person.Client;
import room.Room;
import service.Service;

public class TestHotel {
    public static void main(String[] args) {
        Hotel hotel = new Hotel();
        Room room = new Room(1, 110, 2, 200);
        Room newRoom = new Room(2, 111, 5, 3000);
        Service service = new Service(1, "Завтрак в номер", 2000);
        Client client = new Client(1, "Pavlov Pavel Pavlovich", "8-902-902-98-11");
        Client newClient = new Client(2, "Dmitrov Dmitry Dmitrievich", "8-953-180-00-61");
        System.out.println("Hotel: " + hotel);
        hotel.addRoom(room);
        hotel.addRoom(room);
        hotel.addRoom(newRoom);
        System.out.println("Hotel rooms: " + hotel.getRooms());
        hotel.addService(service);
        hotel.addService(service);
        System.out.println("Hotel services: " + hotel.getServices());
        System.out.println(hotel.checkIn(room, client, newClient));
        System.out.println("Hotel clients: " + hotel.getClients());
        System.out.println("Вместимость: " + room.getCapacity() + ". Сейчас в комнате: " +
                room.getClientsNowInRoom().size());
        if (hotel.checkIn(room, client)) {
            System.out.println("Поселили в заполненную комнату");
        } else {
            System.out.println("Не удалось поселить в заполненную комнату");
        }
        hotel.evict(room, newClient);
        System.out.println("Room status: " + room.getStatus());
        System.out.println("Вместимость: " + room.getCapacity() + ". Сейчас в комнате: " +
                room.getClientsNowInRoom().size());
        if (hotel.checkIn(newRoom)) {
            System.out.println("Поселили без клиентов");
        } else {
            System.out.println("Не удалось поселить без клиентов");
        }
        hotel.evict(room, client);
        System.out.println("Room status: " + room.getStatus());
        System.out.println("Now in room: " + room.getClientsNowInRoom());
        System.out.println("Hotel rooms: " + hotel.getRooms());
    }
}
