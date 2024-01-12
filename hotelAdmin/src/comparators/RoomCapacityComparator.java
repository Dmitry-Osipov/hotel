package comparators;

import essence.room.AbstractRoom;

import java.util.Comparator;

public class RoomCapacityComparator implements Comparator<AbstractRoom> {
    @Override
    public int compare(AbstractRoom o1, AbstractRoom o2) {
        return Integer.compare(o1.getCapacity(), o2.getCapacity());
    }
}
