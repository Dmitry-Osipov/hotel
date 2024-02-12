package utils.exceptions;

/**
 * Исключение, бросаемое для обозначения невалидных данных.
 * Это исключение является подклассом RuntimeException.
 */
public class InvalidDataException extends RuntimeException {
    /**
     * Создает новое исключение с заданным сообщением.
     * @param message сообщение об ошибке, которое будет содержаться в исключении.
     */
    public InvalidDataException(String message) {
        super(message);
    }
}
