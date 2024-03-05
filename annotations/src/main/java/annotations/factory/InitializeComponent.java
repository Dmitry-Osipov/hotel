package annotations.factory;

/**
 * Интерфейс определяет метод init(), который используется для двухэтапной инициализации компонента.
 */
public interface InitializeComponent {
    /**
     * Двухэтапная инициализация компонента.
     */
    void init();
}
