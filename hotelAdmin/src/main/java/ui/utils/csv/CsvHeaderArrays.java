package ui.utils.csv;

import lombok.Getter;

/**
 * Перечисление CsvHeaderArrays представляет собой заголовки CSV файлов для различных типов данных.
 * Каждый элемент перечисления содержит массив строк, представляющих заголовки соответствующих файлов.
 * Это используется для проверки корректности заголовков при импорте данных из CSV файлов.
 */
@Getter
public enum CsvHeaderArrays {
    CLIENTS(new String[] {"ID", "ФИО", "Номер телефона", "Время заезда", "Время выезда"}),
    SERVICES(new String[] {"ID", "Название", "Цена", "Статус", "Время оказания"}),
    ROOMS(new String[] {"ID", "Номер", "Вместительность", "Цена", "Статус", "Количество звёзд", "Время въезда",
            "Время выезда"}),
    RESERVATIONS(new String[] {"ID", "Комната", "Время заезда", "Время выезда", "Список клиентов"}),
    PROVIDED_SERVICES(new String[] {"ID", "Список клиентов", "Услуга", "Время оказания"});

    private final String[] headers;

    CsvHeaderArrays(String[] headers) {
        this.headers = headers;
    }
}
