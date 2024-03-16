package utils.file;

import lombok.Getter;
import utils.PropertyFileReader;

/**
 * Перечисление DataPath представляет собой константы директорий и файлов.
 * Каждый элемент перечисления содержит соответствующий путь из property файла.
 */
@Getter
public enum DataPath {
    PROPERTY_FILE("domain/src/main/resources/config.properties"),
    CSV_DIRECTORY(PropertyFileReader.getValue("csv_directory")),
    ID_DIRECTORY(PropertyFileReader.getValue("id_directory")),
    SERIALIZE_DIRECTORY(PropertyFileReader.getValue("serialize_directory"));

    private final String path;

    DataPath(String path) {
        this.path = path;
    }
}
