package ui.utils.csv;

import lombok.Getter;

/**
 * Перечисление FileAdditionResult представляет собой возможные результаты добавления файлов.
 * Каждый элемент перечисления содержит соответствующее сообщение о результате операции.
 * Также в перечислении определен статический метод для получения директории данных.
 */
@Getter
public enum FileAdditionResult {
    SUCCESS("Файл успешно добавлен"),
    FAILURE("Файл не был перезаписан");

    private final String message;
    private static final String DATA_DIRECTORY = "hotelAdmin/src/ui/utils/csv/data/";

    FileAdditionResult(String message) {
        this.message = message;
    }

    public static String getDataDirectory() {
        return DATA_DIRECTORY;
    }
}
