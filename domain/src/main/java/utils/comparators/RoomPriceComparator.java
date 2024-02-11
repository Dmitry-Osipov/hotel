package utils.comparators;

import essence.room.AbstractRoom;

import java.util.Comparator;

public class RoomPriceComparator implements Comparator<AbstractRoom> {
    @Override
    public int compare(AbstractRoom o1, AbstractRoom o2) {
        return Integer.compare(o1.getPrice(), o2.getPrice());
    }
}
