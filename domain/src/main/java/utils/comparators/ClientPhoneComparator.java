package utils.comparators;

import essence.person.AbstractClient;

import java.util.Comparator;

/**
 * Класс-компаратор предназначен для сравнения клиентов по номеру телефона.
 */
public final class ClientPhoneComparator implements Comparator<AbstractClient> {
    /**
     * Метод проводит лексикографическое сравнение двух клиентов по номеру телефона.
     * @param o1 Первый клиент.
     * @param o2 Второй клиент.
     * @return Результат сравнения.
     */
    @Override
    public int compare(AbstractClient o1, AbstractClient o2) {
        return o1.getPhoneNumber().compareTo(o2.getPhoneNumber());
    }
}
