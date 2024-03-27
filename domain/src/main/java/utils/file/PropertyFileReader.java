package utils.file;

import utils.exceptions.ErrorMessages;
import utils.exceptions.InvalidDataException;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public final class PropertyFileReader {
    private PropertyFileReader() {
    }

    /**
     * Метод получает данные по ключу из property файла.
     * @param key Ключ, по которому хранится значение в файле.
     * @return Значение из файла.
     */
    public static String getValue(String key) {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream(DataPath.PROPERTY_FILE.getPath())) {
            properties.load(fis);
            return properties.getProperty(key);
        } catch (IOException e) {
            System.out.println(ErrorMessages.FILE_ERROR.getMessage());
        }

        throw new InvalidDataException("Значения не вернулись при обработке property файла");
    }
}
