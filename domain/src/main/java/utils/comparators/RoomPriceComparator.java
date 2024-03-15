package utils.comparators;

import essence.room.AbstractRoom;

import java.util.Comparator;

/**
 * Класс-компаратор предназначен для сравнения стоимости двух комнат.
 */
public final class RoomPriceComparator implements Comparator<AbstractRoom> {
    /**
     * Метод сравнивает цену двух комнат.
     * @param o1 Первая комната.
     * @param o2 Вторая комната.
     * @return Результат сравнения.
     */
    @Override
    public int compare(AbstractRoom o1, AbstractRoom o2) {
        return Integer.compare(o1.getPrice(), o2.getPrice());
    }
}
