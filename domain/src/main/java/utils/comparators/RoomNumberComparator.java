package utils.comparators;

import essence.reservation.RoomReservation;

import java.util.Comparator;

/**
 * Класс-компаратор предназначен для сравнения номера комнат в резервациях.
 */
public final class RoomNumberComparator implements Comparator<RoomReservation> {
    /**
     * Метод сравнивает номера комнат по двум резервациям.
     * @param o1 Первая резервация.
     * @param o2 Вторая резервация.
     * @return Результат сравнения.
     */
    @Override
    public int compare(RoomReservation o1, RoomReservation o2) {
        return Integer.compare(o1.getRoom().getNumber(), o2.getRoom().getNumber());
    }
}
