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
    NO_SERVICES("Пока что нет ни одной услуги");

    private final String message;

    ErrorMessages(String message) {
        this.message = message;
    }
}
