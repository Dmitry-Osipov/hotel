package ui.utils.csv;

import lombok.Getter;

/**
 * Перечисление FileAdditionResult представляет собой возможные результаты добавления файлов.
 * Каждый элемент перечисления содержит соответствующее сообщение о результате операции.
 * Также в перечислении определен статический метод для получения директории данных и файла с ID.
 */
@Getter
public enum FileAdditionResult {
    SUCCESS("Файл успешно добавлен"),
    FAILURE("Файл не был перезаписан");

    private final String message;
    private static final String DATA_DIRECTORY = "hotelAdmin/src/ui/utils/csv/data/";
    private static final String ID_DIRECTORY = "hotelAdmin/src/ui/utils/id/data/";

    FileAdditionResult(String message) {
        this.message = message;
    }

    /**
     * Метод получения директории данных, где хранятся файлы.
     * @return Директория данных.
     */
    public static String getDataDirectory() {
        return DATA_DIRECTORY;
    }

    /**
     * Метод получения пути к файлу, содержащему ID.
     * @return Путь к файлу с ID.
     */
    public static String getIdDirectory() {
        return ID_DIRECTORY;
    }
}
