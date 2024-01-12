package comparators;

import essence.room.AbstractRoom;

import java.util.Comparator;

public class RoomNumberComparator implements Comparator<AbstractRoom> {
    @Override
    public int compare(AbstractRoom o1, AbstractRoom o2) {
        return Integer.compare(o1.getNumber(), o2.getNumber());
    }
}
