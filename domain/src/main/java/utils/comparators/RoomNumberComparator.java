package utils.comparators;

import essence.reservation.RoomReservation;

import java.util.Comparator;

public class RoomNumberComparator implements Comparator<RoomReservation> {
    @Override
    public int compare(RoomReservation o1, RoomReservation o2) {
        return Integer.compare(o1.getRoom().getNumber(), o2.getRoom().getNumber());
    }
}
