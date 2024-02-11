package utils.exceptions;

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
    INCORRECT_FILE_DATA_TYPE("Поступил неверный тип данных из файла"),
    ROOM_CONTAINED("Такой номер уже есть в отеле"),
    SERVICE_CONTAINED("Такая услуга уже есть в отеле"),
    CLIENT_CONTAINED("Такой клиент уже есть в отеле"),
    NO_ROOM("Такой комнаты нет в отеле"),
    NO_SERVICE("Такой услуги нет в отеле"),
    NO_CLIENT("Такого клиента нет в отеле"),
    NO_RESERVATION("Такой резервации нет в отеле"),
    NO_PROVIDED_SERVICE("Такой проведённой услуги нет в отеле"),
    INVALID_DATA("Поступили некорректные данные");

    private final String message;

    ErrorMessages(String message) {
        this.message = message;
    }
}
