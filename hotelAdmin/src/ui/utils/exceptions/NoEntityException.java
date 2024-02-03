package ui.utils.exceptions;

/**
 * Исключение, бросаемое для обозначения отсутствия сущности.
 * Это исключение является подклассом RuntimeException.
 */
public class NoEntityException extends RuntimeException {
    /**
     * Создает новое исключение с заданным сообщением.
     * @param message сообщение об ошибке, которое будет содержаться в исключении.
     */
    public NoEntityException(String message) {
        super(message);
    }
}
