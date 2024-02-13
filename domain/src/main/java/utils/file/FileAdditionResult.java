package utils.file;

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
    private static final String CSV_DIRECTORY = "domain/src/main/java/utils/file/csv/data/";
    private static final String ID_DIRECTORY = "domain/src/main/java/utils/file/id/data/";
    private static final String PROPERTY_FILE = "domain/src/main/resources/config.properties";
    private static final String SERIALIZE_DIRECTORY = "domain/src/main/java/utils/file/serialize/data/";

    FileAdditionResult(String message) {
        this.message = message;
    }

    /**
     * Метод получения директории данных, где хранятся CSV файлы.
     * @return Директория CSV.
     */
    public static String getCsvDirectory() {
        return CSV_DIRECTORY;
    }

    /**
     * Метод получения директории к файлу, содержащему ID.
     * @return Директория файлов с ID.
     */
    public static String getIdDirectory() {
        return ID_DIRECTORY;
    }

    /**
     * Метод получения пути к property файлу.
     * @return Путь к property файлу.
     */
    public static String getPropertyFile() {
        return PROPERTY_FILE;
    }

    /**
     * Метод получения директории к файлу сериализации.
     * @return Директория файлов сериализации.
     */
    public static String getSerializeDirectory() {
        return SERIALIZE_DIRECTORY;
    }
}
