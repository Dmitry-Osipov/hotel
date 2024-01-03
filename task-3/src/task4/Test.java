package task4;

public class Test {
    public static void main(String[] args) {
        System.out.println("Проверка функционала отеля");
        Hotel hotel = new Hotel();
        Room room = new Room(1, 110, 200);
        Service service = new Service(1, "Завтрак в номер", 2000);
        Client client = new Client(1, "Pavlov Pavel Pavlovich", "8-902-902-98-11");
        System.out.println(hotel);
        hotel.addRoom(room);
        hotel.addRoom(room);
        System.out.println(hotel.getRooms());
        hotel.addService(service);
        hotel.addService(service);
        System.out.println(hotel.getServices());
        hotel.checkIn(client, room);
        System.out.println(hotel.getClients());
        if (hotel.checkIn(client, room)) {
            System.out.println("Поселили");
        } else {
            System.out.println("Не удалось поселить клиента");
        }
        hotel.evict(room);
        System.out.println(hotel.getRooms());
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
