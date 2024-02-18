package utils.file;

import lombok.Getter;

/**
 * Перечисление FileAdditionResult представляет собой возможные результаты добавления файлов.
 * Каждый элемент перечисления содержит соответствующее сообщение о результате операции.
 */
@Getter
public enum FileAdditionResult {
    SUCCESS("Файл успешно добавлен"),
    FAILURE("Файл не был перезаписан");

    private final String message;

    FileAdditionResult(String message) {
        this.message = message;
    }
}
