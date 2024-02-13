package utils.file.serialize;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;

public final class SerializationUtils {
    private static final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    private SerializationUtils() {
    }

    public static void serialize(Object obj, String filePath) throws IOException {
        filePath = filePath + ".json";
        mapper.writeValue(new File(filePath), obj);
    }

    public static <T> T deserialize(Class<T> clazz, String filePath) throws IOException {
        filePath = filePath + ".json";
        return mapper.readValue(new File(filePath), clazz);
    }
}
