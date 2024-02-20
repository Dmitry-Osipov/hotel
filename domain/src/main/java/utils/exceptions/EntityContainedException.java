package utils.exceptions;

/**
 * Исключение, бросаемое для обозначения сущностей, которые уже содержатся, но не должны иметь дубликатов.
 * Это исключение является подклассом RuntimeException.
 */
public class EntityContainedException extends RuntimeException {
    /**
     * Создает новое исключение с заданным сообщением.
     * @param message сообщение об ошибке, которое будет содержаться в исключении.
     */
    public EntityContainedException(String message) {
        super(message);
    }
}
