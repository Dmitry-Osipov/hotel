package utils;

import annotations.annotation.ConfigProperty;
import annotations.factory.Config;
import utils.file.DataPath;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Properties;

/**
 * Класс-конфигуратор для компонентов.
 */
public class AllowedSetStatusRoomConfigurator implements Config {
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

                Boolean value = readConfigValue(configName, propertyName);
                if (annotation.type() == Boolean.class || annotation.type() == boolean.class) {
                    field.setAccessible(true);
                    field.set(component, value);
                }
            }
        }
    }

    /**
     * Служебный метод достаёт булево-значение из конфигурационного файла.
     * @param configName Путь до конфигурационного файла.
     * @param propertyName Ключ.
     * @return Значение.
     * @throws IOException Низкоуровневая ошибка ввода/вывода.
     */
    private Boolean readConfigValue(String configName, String propertyName) throws IOException {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream(configName)) {
            properties.load(fis);
            return Boolean.parseBoolean(properties.getProperty(propertyName));
        }
    }
}
