package ui;

import lombok.Getter;
import lombok.ToString;

/**
 * Класс отвечает за навигацию по меню.
 */
@Getter
@ToString
public class Navigator {
    private Menu currentMenu;
    private final Menu rootMenu;

    /**
     * Класс отвечает за навигацию по меню.
     * @param currentMenu Меню.
     */
    public Navigator(Menu currentMenu) {
        this.currentMenu = currentMenu;
        this.rootMenu = currentMenu;
    }

    /**
     * Метод выводит меню.
     */
    public void printMenu() {
        System.out.println("\nМеню: " + currentMenu.getName());
        for (int i = 0; i < currentMenu.getMenuItems().length; i++) {
            System.out.println(i+1 + ". " + currentMenu.getMenuItems()[i].title());
        }
    }

    /**
     * Метод производит навигацию по меню.
     * @param index Индекс меню/действия, к которому произойдёт переход.
     */
    public void navigate(Integer index) {
        currentMenu.getMenuItems()[index].doAction();
        currentMenu = getNextMenu(index);
    }

    /**
     * Служебный метод проводит проверку валидности индекса и наличия следующего меню.
     * @param index Индекс.
     * @return Следующее меню, если оно есть, иначе главное меню.
     */
    private Menu getNextMenu(Integer index) {
        if (index >= 0 && index <= currentMenu.getMenuItems().length-1
                && currentMenu.getMenuItems()[index].nextMenu() != null) {
            return currentMenu.getMenuItems()[index].nextMenu();
        }

        return rootMenu;
    }
}
