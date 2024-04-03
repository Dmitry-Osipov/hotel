package ui;

import annotations.factory.InitializeComponent;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import utils.exceptions.ErrorMessages;

import javax.annotation.PostConstruct;

/**
 * Класс отвечает за навигацию по меню.
 */
@Component
@ToString
public class Navigator implements InitializeComponent {
    private static final Logger logger = LoggerFactory.getLogger("AppProcess");
    private Menu currentMenu;
    private Menu rootMenu;
    private final Builder builder;

    @Autowired
    public Navigator(Builder builder) {
        this.builder = builder;
    }

    /**
     * Метод проводит инициализацию главного и текущего меню.
     */
    @PostConstruct
    @Override
    public void init() {
        currentMenu = builder.getRootMenu();
        rootMenu = currentMenu;
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
            logger.error("Пользователь выбрал несуществующий пункт меню");
            System.out.println("\n" + ErrorMessages.INCORRECT_INPUT.getMessage());
        } else {
            Menu nextMenu = getNextMenu(index);
            logger.info("Пользователь корректно выбрал пункт меню. Выполнение действия \"{}\" и " +
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
