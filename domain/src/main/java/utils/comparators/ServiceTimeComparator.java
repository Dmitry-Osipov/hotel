package utils.comparators;

import essence.service.AbstractService;

import java.time.LocalDateTime;
import java.util.Comparator;

/**
 * Класс-компаратор предназначен для сравнения времени оказания двух услуг.
 */
public final class ServiceTimeComparator implements Comparator<AbstractService> {
    /**
     * Метод сравнивает время оказания двух услуг.
     * @param o1 Первая услуга.
     * @param o2 Вторая услуга.
     * @return Результат сравнения.
     */
    @Override
    public int compare(AbstractService o1, AbstractService o2) {
        LocalDateTime time1 = o1.getServiceTime();
        LocalDateTime time2 = o2.getServiceTime();

        if (time1 == null && time2 == null) {
            return 0;
        }

        if (time1 == null) {
            return -1;
        }

        if (time2 == null) {
            return 1;
        }

        return time1.compareTo(time2);
    }
}
