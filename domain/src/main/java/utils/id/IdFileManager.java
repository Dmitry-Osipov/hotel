package utils.id;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Класс управляет чтением и записью максимального идентификатора из файла.
 * Этот класс предоставляет статические методы для чтения и записи максимального идентификатора.
 */
public final class IdFileManager {
    private static final Logger logger = LoggerFactory.getLogger(IdFileManager.class);

    private IdFileManager() {
    }

    /**
     * Считывает максимальный идентификатор из указанного файла.
     * @param fileName Имя файла, из которого будет считываться максимальный идентификатор.
     * @return Максимальный идентификатор, считанный из файла, или значение по умолчанию (1), если чтение не удалось.
     */
    public static int readMaxId(String fileName) {
        logger.info("Вызван метод чтения ID из файла {}", fileName);
        int defaultId = 1;
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line = reader.readLine();
            if (line != null && !line.isEmpty()) {
                int id = Integer.parseInt(line);
                logger.info("Получено ID {} из файла {}", id, fileName);
                return id;
            }
        } catch (IOException e) {
            logger.error("Произошла ошибка во время обработки файла {}. Передано стандартное ID {}",
                    fileName, defaultId);
            return defaultId;
        }

        logger.warn("Передано стандартное ID {}", defaultId);
        return defaultId;
    }

    /**
     * Записывает указанный максимальный идентификатор в указанный файл.
     * @param fileName Имя файла, в который будет записан максимальный идентификатор.
     * @param id Максимальный идентификатор, который нужно записать в файл.
     * @throws IOException Если возникла ошибка ввода-вывода при записи в файл.
     */
    public static void writeMaxId(String fileName, int id) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(String.valueOf(id));
        } catch (IOException e) {
            logger.error("Произошла ошибка во время обработки файла {} с переданным ID {}", fileName, id);
            throw new IOException(e.getMessage());
        }
    }
}
