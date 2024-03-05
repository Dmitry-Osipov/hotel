package annotations.factory;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Класс проводит упрощённую создание и настройку компонентов.
 */
public class ApplicationContext {
    private final ComponentFactory factory = new ComponentFactory();
    private final List<String> packages;

    /**
     * Конструктор принимает пакет(-ы) для сканирования.
     * @param packages Пакет(-ы). Важно: вложенные пакеты не сканируются.
     */
    public ApplicationContext(List<String> packages) {
        this.packages = packages;
    }

    /**
     * Метод инициализирует в принятых конструктором пакетах компоненты, внедряет зависимости и настраивает компоненты.
     * @param configs Конфигураторы.
     * @throws IllegalArgumentException Ошибка выбрасывается, когда не было передано пакетов для сканирования.
     * @throws IOException Низкоуровневая ошибка ввода/вывода.
     * @throws URISyntaxException Ошибка, указывающая, что строка не может быть разобрана как ссылка на URI.
     * @throws IllegalAccessException Исключение выбрасывается, когда приложение пытается рефлексивно создать экземпляр
     * (кроме массива), установить или получить поле или вызвать метод, но выполняющийся в данный момент метод не имеет
     * доступа к определению указанного класса, поля, метода или конструктора.
     * @throws ClassNotFoundException Выбрасывается, когда приложение пытается загрузить класс через его строковое имя,
     * но определения класса с указанным именем найти не удалось.
     * @throws NoSuchMethodException Выбрасывается, когда определенный метод не может быть найден.
     * @throws InvocationTargetException Проверяемое исключение, которое оборачивает исключение, брошенное вызванным
     * методом или конструктором.
     * @throws InstantiationException Выбрасывается, когда приложение пытается создать экземпляр класса с помощью метода
     * newInstance в классе Class, но указанный объект класса не может быть инстанцирован. Инстанцирование может не
     * произойти по целому ряду причин, включая, но не ограничиваясь ими:
     * <p>- объект класса представляет собой абстрактный класс, интерфейс, класс массива, примитивный тип или void
     * <p>- класс не имеет нулевого конструктора
     */
    public void run(Config ...configs) throws IOException, URISyntaxException, ClassNotFoundException,
            InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
        if (packages.isEmpty()) {
            throw new IllegalArgumentException("Не было передано пакетов для сканирования");
        }

        for (String packageName : packages) {
            factory.componentInitialization(packageName);
        }
        factory.dependencySetting();

        for (Config config : configs) {
            factory.addConfig(config);
        }
        factory.configureComponents();

        factory.secondStepComponentInitialization();
    }

    /**
     * Метод получения компонента по названию его класса в camel-case.
     * @param componentName Название компонента.
     * @return Компонент.
     */
    public Object getComponent(String componentName) {
        return factory.getComponent(componentName);
    }
}
