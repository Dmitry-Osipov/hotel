package annotations.factory;

import annotations.annotation.Autowired;
import annotations.annotation.Component;
import annotations.annotation.ConfigProperty;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Класс отвечает за создание и настройку компонентов.
 */
public class ComponentFactory {
    private final Map<String, Object> singletons = new HashMap<>();
    private final List<Config> configs = new ArrayList<>();

    /**
     * Метод получения компонента по названию его класса в camel-case.
     * @param componentName Название компонента.
     * @return Компонент.
     */
    public Object getComponent(String componentName) {
        return singletons.get(componentName);
    }

    /**
     * Метод добавляет конфигуратор в список.
     * @param config Конфигуратор, который должен настроить компоненты.
     */
    public void addConfig(Config config) {
        configs.add(config);
    }

    /**
     * Метод проводит инициализацию всех компонентов переданного пакета (вложенные пакеты не сканируются).
     * @param packageName Название пакета.
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
    public void componentInitialization(String packageName) throws IOException, URISyntaxException,
            IllegalAccessException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException,
            InstantiationException {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();

        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);

        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();

            File file = new File(resource.toURI());
            for (File classFile : Objects.requireNonNull(file.listFiles())) {
                String fileName = classFile.getName();
                if (fileName.endsWith(".class")) {
                    String className = fileName.substring(0, fileName.lastIndexOf("."));

                    Class<?> classObject = Class.forName(packageName + "." + className);
                    if (classObject.isAnnotationPresent(Component.class)) {
                        Object instance = classObject.getDeclaredConstructor().newInstance();
                        String beanName = className.substring(0, 1).toLowerCase() + className.substring(1);
                        singletons.put(beanName, instance);
                    }
                }
            }
        }
    }

    /**
     * Метод устанавливает зависимости компонентов.
     * @throws IllegalAccessException Исключение выбрасывается, когда приложение пытается рефлексивно создать экземпляр
     * (кроме массива), установить или получить поле или вызвать метод, но выполняющийся в данный момент метод не имеет
     * доступа к определению указанного класса, поля, метода или конструктора.
     */
    public void dependencySetting() throws IllegalAccessException {
        Collection<Object> objects = singletons.values();
        for (Object component : objects) {
            for (Field field : component.getClass().getDeclaredFields()) {
                if (field.isAnnotationPresent(Autowired.class)) {
                    for (Object dependency : objects) {
                        if (dependency.getClass().equals(field.getType())) {
                            field.setAccessible(true);
                            field.set(component, dependency);
                        }
                    }
                }
            }
        }
    }

    /**
     * Метод проводит конфигурацию всех компонентов.
     * @throws IOException Ошибка ввода/вывода.
     * @throws IllegalAccessException Исключение выбрасывается, когда приложение пытается рефлексивно создать экземпляр
     * (кроме массива), установить или получить поле или вызвать метод, но выполняющийся в данный момент метод не имеет
     * доступа к определению указанного класса, поля, метода или конструктора.
     */
    public void configureComponents() throws IOException, IllegalAccessException {
        for (Object component : singletons.values()) {
            for (Field field : component.getClass().getDeclaredFields()) {
                if (field.isAnnotationPresent(ConfigProperty.class)) {
                    for (Config config : configs) {
                        config.configure(component);
                    }
                }
            }
        }
    }

    /**
     * Метод проводит двухфакторную инициализацию для компонентов.
     */
    public void secondStepComponentInitialization() {
        for (Object component : singletons.values()) {
            if (component instanceof InitializeComponent obj) {
                obj.init();
            }
        }
    }
}
