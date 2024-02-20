package utils.file;

import lombok.Getter;
import utils.exceptions.ErrorMessages;
import utils.exceptions.InvalidDataException;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Перечисление DataPath представляет собой константы директорий и файлов.
 * Каждый элемент перечисления содержит соответствующий путь из property файла.
 */
@Getter
public enum DataPath {
    PROPERTY_FILE("domain/src/main/resources/config.properties"),
    CSV_DIRECTORY(getPathFromPropertyFile("csv_directory")),
    ID_DIRECTORY(getPathFromPropertyFile("id_directory")),
    SERIALIZE_DIRECTORY(getPathFromPropertyFile("serialize_directory"));

    private final String path;

    DataPath(String path) {
        this.path = path;
    }

    /**
     * Служебный метод получает данные по путям из property файла.
     * @param key Ключ, по которому хранится значение пути в файле.
     * @return Значение пути из файла.
     */
    private static String getPathFromPropertyFile(String key) {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream(PROPERTY_FILE.path)) {
            properties.load(fis);
            return properties.getProperty(key);
        } catch (IOException e) {
            System.out.println(ErrorMessages.FILE_ERROR.getMessage());
        }

        throw new InvalidDataException("Значения не вернулись при обработке property файла");
    }
}
