package utils.exceptions;

/**
 * Исключение, бросаемое для обозначения запрета доступа к операции.
 * Это исключение является подклассом RuntimeException.
 */
public class AccessDeniedException extends RuntimeException {
    /**
     * Создает новое исключение с заданным сообщением.
     * @param message сообщение об ошибке, которое будет содержаться в исключении.
     */
    public AccessDeniedException(String message) {
        super(message);
    }
}
