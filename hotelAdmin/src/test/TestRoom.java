package test;

import room.Room;
import room.RoomStatusTypes;

public class TestRoom {
    public static void main(String[] args) {
        Room room = new Room(1, 110, 2, 200);
        room.setStatus(RoomStatusTypes.REPAIR);
        System.out.println(room.getStatus());
        room.setPrice(2500);
        System.out.println(room.getPrice());
    }
}
