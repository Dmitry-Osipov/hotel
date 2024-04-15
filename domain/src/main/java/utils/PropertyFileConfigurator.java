package utils;

import annotations.annotation.ConfigProperty;
import annotations.factory.Config;
import utils.exceptions.InvalidDataException;
import utils.exceptions.TechnicalException;
import utils.file.DataPath;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.function.Function;

/**
 * Класс-конфигуратор для компонентов, помеченных аннотацией @ConfigProperty.
 */
public final class PropertyFileConfigurator implements Config {
    private final Map<Class<?>, Function<String, Object>> typeFunctionMap =
            new HashMap<>(Map.of(
                    String.class, s -> s,
                    Integer.class, Integer::parseInt,
                    int.class, Integer::parseInt,
                    Long.class, Long::parseLong,
                    Float.class, Float::parseFloat,
                    float.class, Float::parseFloat,
                    Double.class, Double::parseDouble,
                    double.class, Double::parseDouble,
                    Boolean.class, Boolean::parseBoolean,
                    boolean.class, Boolean::parseBoolean
            ));

    /**
     * Метод проводит конфигурацию поля, помеченного аннотацией @ConfigProperty.
     * @param component Компонент.
     * @throws IOException Низкоуровневая ошибка ввода/вывода.
     * @throws IllegalAccessException Исключение выбрасывается, когда приложение пытается рефлексивно создать экземпляр
     * (кроме массива), установить или получить поле или вызвать метод, но выполняющийся в данный момент метод не имеет
     * доступа к определению указанного класса, поля, метода или конструктора.
     */
    @Override
    public void configure(Object component) throws IOException, IllegalAccessException {
        Class<?> clazz = component.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(ConfigProperty.class)) {
                ConfigProperty annotation = field.getAnnotation(ConfigProperty.class);

                String configName = annotation.configName();
                if (configName.isEmpty()) {
                    configName = DataPath.PROPERTY_FILE.getPath();
                }

                String propertyName = annotation.propertyName();
                if (propertyName.isEmpty()) {
                    propertyName = clazz.getName() + "." + field.getName();
                }

                String value = readConfigValue(configName, propertyName);
                field.setAccessible(true);
                field.set(component, getValueWithType(annotation.type(), value));
            }
        }
    }

    /**
     * Служебный метод достаёт значение из конфигурационного файла.
     * @param configName Путь до конфигурационного файла.
     * @param propertyName Ключ.
     * @return Значение.
     * @throws IOException Низкоуровневая ошибка ввода/вывода.
     */
    private String readConfigValue(String configName, String propertyName) throws IOException {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream(configName)) {
            properties.load(fis);
            return properties.getProperty(propertyName);
        }
    }

    /**
     * Служебный метод приводит значение к нужному типу.
     * @param type Тип, к которому нужно привести значение.
     * @param value Значение, которое нужно обработать.
     * @return Значение с приведённым типом.
     * @throws InvalidDataException Ошибка связана с поступившим {@code null} в качестве {@code value}.
     * @throws TechnicalException Ошибка оповещает о невозможности приведения {@code value} к {@code type}.
     */
    private <T> T getValueWithType(Class<T> type, String value) {
        if (value == null) {
            throw new InvalidDataException("В файле конфигурации отсутствует значение");
        }

        Function<String, Object> stringObjectFunction;
        if (type == short.class
                || type == Short.class
                || type == byte.class
                || type == Byte.class) {
            stringObjectFunction = typeFunctionMap.get(Integer.class);
        } else if (type == long.class) {
            stringObjectFunction = typeFunctionMap.get(Long.class);
        } else {
            stringObjectFunction = typeFunctionMap.get(type);
        }

        if (stringObjectFunction == null) {
            throw new TechnicalException("Приведение значения к типу " + type.getName() + " не поддерживается");
        }

        return (T) stringObjectFunction.apply(value);
    }
}
