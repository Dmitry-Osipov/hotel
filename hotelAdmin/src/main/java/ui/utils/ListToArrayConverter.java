package ui.utils;

import java.lang.reflect.Array;
import java.util.List;

/**
 * Класс является финальным и предоставляет статический метод для конвертации списка в массив.
 */
public final class ListToArrayConverter {
    private ListToArrayConverter() {
    }

    /**
     * Метод конвертирует список в массив.
     * @param list Список.
     * @param elementType Класс, с которым требуется вернуть массив.
     * @return Массив.
     */
    public static <T> T[] convertListToArray (List<T> list, Class<T> elementType) {
        T[] array = (T[]) Array.newInstance(elementType, list.size());
        return list.toArray(array);
    }
}
