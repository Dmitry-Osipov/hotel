package utils.comparators;

import essence.room.AbstractRoom;

import java.util.Comparator;

/**
 * Класс-компаратор предназначен для сравнения комнат по вместимости.
 */
public final class RoomCapacityComparator implements Comparator<AbstractRoom> {
    /**
     * Метод сравнивает вместимость у двух комнат.
     * @param o1 Первая комната.
     * @param o2 Вторая комната.
     * @return Результат сравнения.
     */
    @Override
    public int compare(AbstractRoom o1, AbstractRoom o2) {
        return Integer.compare(o1.getCapacity(), o2.getCapacity());
    }
}
