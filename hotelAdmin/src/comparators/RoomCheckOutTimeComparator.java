package comparators;

import essence.room.AbstractRoom;

import java.time.chrono.ChronoLocalDateTime;
import java.util.Comparator;
import java.util.Objects;

public class RoomCheckOutTimeComparator implements Comparator<AbstractRoom> {
    @Override
    public int compare(AbstractRoom o1, AbstractRoom o2) {
        return Objects.compare(o1.getCheckOutTime(), o2.getCheckOutTime(),
                Comparator.nullsLast(ChronoLocalDateTime::compareTo));
    }
}
