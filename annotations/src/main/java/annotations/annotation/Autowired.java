package annotations.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Указывает, что поле должно быть автоматически связано с зависимостью с помощью внедрения зависимостей.
 * По умолчанию аннотация Autowired сохраняется во время выполнения и может применяться только к полям.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Autowired {
}
