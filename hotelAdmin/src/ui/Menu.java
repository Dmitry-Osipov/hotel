package ui;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Класс меню.
 */
@Setter
@Getter
@ToString
public class Menu {
    private String name;
    private MenuItem[] menuItems;

    /**
     * Класс меню.
     * @param name Название.
     * @param menuItems Подменю.
     */
    public Menu(String name, MenuItem[] menuItems) {
        this.name = name;
        this.menuItems = menuItems;
    }
}
