package utils.exceptions;

/**
 * Исключение, бросаемое для обозначения технической ошибки.
 */
public class TechnicalException extends RuntimeException {
    /**
     * Создает новое исключение с заданным сообщением.
     * @param message сообщение об ошибке, которое будет содержаться в исключении.
     */
    public TechnicalException(String message) {
        super(message);
    }
}
