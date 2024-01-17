package ui;

/**
 * Класс подменю.
 * @param title Заголовок.
 * @param action Действие.
 * @param nextMenu Следующее меню.
 */
public record MenuItem(String title, IAction action, Menu nextMenu) {

    /**
     * Выполнение действия.
     */
    public void doAction() {
        if (action != null) {
            action.execute();
        }
    }
}
