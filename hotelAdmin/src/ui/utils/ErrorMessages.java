package ui.utils;

import lombok.Getter;

/**
 * Класс предназначен для отработки сообщений об ошибке.
 */
@Getter
public enum ErrorMessages {
    INCORRECT_INPUT("Неверный ввод. Повторите: "),
    NO_ROOMS("Пока что нет ни одной комнаты"),
    NO_CLIENTS("Пока что нет ни одного клиента"),
    NO_SERVICES("Пока что нет ни одной услуги"),
    FILE_ERROR("Произошла ошибка обработки файла"),
    ROOM_ADDITION_FAILURE("Не удалось добавить комнату"),
    SERVICE_ADDITION_FAILURE("Не удалось добавить услугу"),
    CLIENT_ADDITION_FAILURE("Не удалось добавить клиента"),
    RESERVATION_ADDITION_FAILURE("Не удалось добавить резервацию"),
    SERVICE_PROVIDED_ADDITION_FAILURE("Не удалось добавить проведённую услугу"),
    INCORRECT_FILE_DATA_TYPE("Поступил неверный тип данных из файла");

    private final String message;

    ErrorMessages(String message) {
        this.message = message;
    }
}
