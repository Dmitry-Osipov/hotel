package utils.file.serialize;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;

/**
 * Утилитарный класс для сериализации и десериализации объектов в формат JSON.
 */
public final class SerializationUtils {
    private static final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    /**
     * Приватный конструктор класса для предотвращения создания экземпляров.
     */
    private SerializationUtils() {
    }

    /**
     * Сериализует объект в формат JSON и записывает его в указанный файл.
     * @param obj Объект для сериализации.
     * @param filePath Путь к файлу без указания расширения, в который будет записан JSON.
     * @throws IOException Ошибка ввода/вывода.
     */
    public static void serialize(Object obj, String filePath) throws IOException {
        filePath = filePath + ".json";
        mapper.writeValue(new File(filePath), obj);
    }

    /**
     * Десериализует JSON из указанного файла в объект указанного класса.
     * @param <T> Тип объекта, в который будет произведена десериализация.
     * @param clazz Класс объекта, в который будет произведена десериализация.
     * @param filePath Путь к файлу без указания расширения, из которого будет считан JSON.
     * @return Объект, десериализованный из JSON.
     * @throws IOException Ошибка ввода/вывода.
     */
    public static <T> T deserialize(Class<T> clazz, String filePath) throws IOException {
        filePath = filePath + ".json";
        return mapper.readValue(new File(filePath), clazz);
    }
}
