package ui.utils.csv;

import lombok.Getter;

/**
 * Перечисление CsvHeaderLength представляет собой длину (количество элементов) заголовков CSV файлов
 * для различных типов данных. Каждый элемент перечисления содержит целое число, представляющее
 * ожидаемую длину заголовка соответствующего файла.
 * Это используется для проверки корректности длины заголовков при импорте данных из CSV файлов.
 */
@Getter
public enum CsvHeaderLength {
    CLIENTS(5),
    SERVICES(5),
    ROOMS(8),
    RESERVATIONS(5),
    PROVIDED_SERVICES(4);

    private final int headerLength;

    CsvHeaderLength(int headerLength) {
        this.headerLength = headerLength;
    }
}
