package utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * Утилитарный класс для преобразования объектов в формат JSON и обратно.
 */
public final class JsonConverter {
    private static final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    private JsonConverter() {
    }

    /**
     * Преобразует объект в его JSON-представление.
     * @param obj Объект для преобразования в JSON
     * @return Строка JSON, представляющая указанный объект
     * @throws JsonProcessingException если возникла ошибка при преобразовании объекта в JSON
     */
    public static String toJson(Object obj) throws JsonProcessingException {
        return mapper.writeValueAsString(obj);
    }

    /**
     * Преобразует JSON-строку в объект указанного класса.
     * @param json JSON-строка для преобразования в объект
     * @param clazz Класс, в который требуется преобразовать JSON
     * @return Объект указанного класса, полученный из JSON-строки
     * @throws JsonProcessingException если возникла ошибка при преобразовании JSON в объект
     */
    public static <T> T fromJson(String json, Class<T> clazz) throws JsonProcessingException {
        return mapper.readValue(json, clazz);
    }
}
