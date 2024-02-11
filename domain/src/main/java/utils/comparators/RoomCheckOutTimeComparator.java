package utils.comparators;

import essence.reservation.RoomReservation;

import java.time.chrono.ChronoLocalDateTime;
import java.util.Comparator;
import java.util.Objects;

public class RoomCheckOutTimeComparator implements Comparator<RoomReservation> {
    @Override
    public int compare(RoomReservation o1, RoomReservation o2) {
        return Objects.compare(o1.getRoom().getCheckOutTime(), o2.getRoom().getCheckOutTime(),
                Comparator.nullsLast(ChronoLocalDateTime::compareTo));
    }
}
