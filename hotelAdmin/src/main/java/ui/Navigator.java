package ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ui.utils.exceptions.ErrorMessages;

/**
 * Класс отвечает за навигацию по меню.
 */
public class Navigator {
    private static final Logger logger = LoggerFactory.getLogger("AppProcess");
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
        String menuName = currentMenu.getName();
        logger.info("Вызов метода печати меню \"{}\"", menuName);
        System.out.println("\nМеню: " + menuName);
        for (int i = 0; i < currentMenu.getMenuItems().length; i++) {
            System.out.println(i+1 + ". " + currentMenu.getMenuItems()[i].title());
        }
    }

    /**
     * Метод производит навигацию по меню.
     * @param index Индекс меню/действия, к которому произойдёт переход.
     */
    public void navigate(Integer index) {
        logger.info("Вызов метода навигации по меню");
        if (index < 0 || currentMenu.getMenuItems().length <= index) {
            logger.warn("Пользователь выбрал несуществующий пункт меню");
            System.out.println("\n" + ErrorMessages.INCORRECT_INPUT.getMessage());
        } else {
            Menu nextMenu = getNextMenu(index);
            logger.warn("Пользователь корректно выбрал пункт меню. Выполнение действия \"{}\" и " +
                    "переход на следующее меню \"{}\"", currentMenu.getMenuItems()[index].title(), nextMenu.getName());
            currentMenu.getMenuItems()[index].doAction();
            currentMenu = nextMenu;
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
