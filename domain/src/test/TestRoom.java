import essence.room.Room;
import essence.room.RoomStatusTypes;
import utils.comparators.RoomCapacityComparator;
import utils.comparators.RoomPriceComparator;

import java.io.IOException;
import java.util.TreeSet;

public class TestRoom {
    public static void main(String[] args) {
        Room room = new Room(1, 110, 2, 200);
        Room newRoom = new Room(2, 111, 5, 3000);
        Room room1 = new Room(3, 112, 3, 3000);
        try {
            room.setStatus(RoomStatusTypes.REPAIR);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(room.getStatus());
        room.setPrice(2500);
        System.out.println(room.getPrice());
        room.setStars(4);
        newRoom.setStars(2);
        TreeSet<Room> rooms = new TreeSet<>();
        rooms.add(room);
        rooms.add(newRoom);
        rooms.add(room1);
        System.out.println("Сортировка по убыванию звёзд: " + rooms);
        TreeSet<Room> rooms1 = new TreeSet<>(new RoomPriceComparator());
        rooms1.add(room);
        rooms1.add(newRoom);
        System.out.println("Сортировка по возрастанию цены: " + rooms1);
        TreeSet<Room> rooms2 = new TreeSet<>(new RoomCapacityComparator());
        rooms2.add(room);
        rooms2.add(room1);
        rooms2.add(newRoom);
        System.out.println("Сортировка по возрастанию вместимости: " + rooms2);
    }
}
