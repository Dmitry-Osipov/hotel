package ui;

import lombok.Getter;
import lombok.ToString;
import ui.utils.ErrorMessages;

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
        if (index < 0 || currentMenu.getMenuItems().length <= index) {
            System.out.println("\n" + ErrorMessages.INCORRECT_INPUT.getMessage());
        } else {
            currentMenu.getMenuItems()[index].doAction();
            currentMenu = getNextMenu(index);
        }
    }

    /**
     * Служебный метод проводит проверку наличия следующего меню.
     * @param index Индекс.
     * @return Следующее меню, если оно есть, иначе главное меню.
     */
    private Menu getNextMenu(Integer index) {
        if (currentMenu.getMenuItems()[index].nextMenu() != null) {
            return currentMenu.getMenuItems()[index].nextMenu();
        }

        return rootMenu;
    }
}
