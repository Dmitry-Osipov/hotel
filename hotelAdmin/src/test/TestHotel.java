package test;

import controller.Hotel;
import person.Client;
import room.Room;
import service.Service;
import view.HotelInfo;

public class TestHotel {
    public static void main(String[] args) {
        Hotel hotel = new Hotel();
        Room room = new Room(1, 110, 2, 200);
        Room newRoom = new Room(2, 111, 5, 4000);
        Room room1 = new Room(3, 112, 1, 3000);
        Service service = new Service(1, "Завтрак в номер", 2000);
        Client client = new Client(1, "Pavlov Pavel Pavlovich", "8-902-902-98-11");
        Client newClient = new Client(2, "Dmitrov Dmitry Dmitrievich", "8-953-180-00-61");
        System.out.println("Hotel: " + hotel);
        hotel.addRoom(room);
        hotel.addRoom(room);
        hotel.addRoom(newRoom);
        hotel.addRoom(room1);
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
        hotel.addStarsToRoom(room, 5);
        System.out.println(room);
        System.out.println(hotel.addStarsToRoom(room, 10));
        hotel.addStarsToRoom(newRoom, 2);
        hotel.checkIn(room, client);

        HotelInfo info = new HotelInfo(hotel);
        System.out.println("DESC stars: " + info.roomsByStars());
        System.out.println("ASC price: " + info.roomsByPrice());
        System.out.println("ASC capacity: " + info.roomsByCapacity());
        System.out.println("DESC stars available: " + info.availableRoomsByStars());
        System.out.println("ASC price available: " + info.availableRoomsByPrice());
        System.out.println("ASC capacity available: " + info.availableRoomsByCapacity());
        System.out.println("Всего свободных комнат: " + info.countAvailableRooms());
        System.out.println("Всего клиентов: " + info.countClients());
        System.out.println("Стоимость номера id=1: " + info.getFavorPrice(room));
        System.out.println("Стоимость номера id=2: " + info.getFavorPrice(newRoom));
        System.out.println("Стоимость номера id=3: " + info.getFavorPrice(room1));
        System.out.println("Стоимость услуги: " + info.getFavorPrice(service));
        System.out.println("Последние 3 клиента номера: " + info.getRoomClients(room));
        System.out.println("Полная о номере: " + info.getRoomInfo(room));
    }
}
