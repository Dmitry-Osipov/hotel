package annotations.factory;

import java.io.IOException;

/**
 * Интерфейс конфигурации компонентов.
 */
public interface Config {
    /**
     * Метод проводит конфигурацию поля, помеченного аннотацией @ConfigProperty.
     * @param component Компонент.
     * @throws IOException Низкоуровневая ошибка ввода/вывода.
     * @throws IllegalAccessException Исключение выбрасывается, когда приложение пытается рефлексивно создать экземпляр
     * (кроме массива), установить или получить поле или вызвать метод, но выполняющийся в данный момент метод не имеет
     * доступа к определению указанного класса, поля, метода или конструктора.
     */
    void configure(Object component) throws IOException, IllegalAccessException;
}
