package annotations.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Указывает, что поле является конфигурационным параметром, который должен быть заполнен значением из конфигурационного
 * файла.
 * Аннотация ConfigProperty позволяет задать различные атрибуты конфигурационного параметра, такие как названия файла
 * конфигурации, название поля в файле конфигурации, тип данных.
 * Если атрибуты не заданы, будут использованы значения по умолчанию:
 * <p>- configName: пустая строка
 * <p>- propertyName: пустая строка
 * <p>- type: String
 * <p>Поля, помеченные аннотацией ConfigProperty, будут автоматически заполняться значениями из конфигурационного файла
 * в соответствии с указанными атрибутами.
 * По умолчанию аннотация ConfigProperty сохраняется во время выполнения и может применяться только к полям.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ConfigProperty {
    String configName() default "";
    String propertyName() default "";
    Class<?> type() default String.class;
}
