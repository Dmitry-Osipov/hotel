package annotations.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Указывает, что класс является компонентом, управляемым контейнером внедрения зависимостей.
 * Эта аннотация используется для обозначения классов, которые должны быть автоматически обнаружены и зарегистрированы в
 * контексте приложения.
 * Классы, помеченные аннотацией Component, становятся кандидатами на автоматическую инъекцию зависимостей и управление
 * их жизненным циклом.
 * По умолчанию аннотация Component сохраняется во время выполнения.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Component {
}
