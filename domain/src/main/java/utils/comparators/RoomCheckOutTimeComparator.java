package utils.comparators;

import essence.reservation.RoomReservation;

import java.time.chrono.ChronoLocalDateTime;
import java.util.Comparator;
import java.util.Objects;

/**
 * Класс-компаратор предназначен для сравнения времени выезда у двух резерваций.
 */
public final class RoomCheckOutTimeComparator implements Comparator<RoomReservation> {
    /**
     * Метод сравнивает время выезда двух резерваций.
     * @param o1 Первая резервация.
     * @param o2 Вторая резервация.
     * @return Результат сравнения.
     */
    @Override
    public int compare(RoomReservation o1, RoomReservation o2) {
        return Objects.compare(o1.getRoom().getCheckOutTime(), o2.getRoom().getCheckOutTime(),
                Comparator.nullsLast(ChronoLocalDateTime::compareTo));
    }
}
