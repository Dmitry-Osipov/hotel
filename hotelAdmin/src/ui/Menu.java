package ui;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Arrays;

@Setter
@Getter
@ToString
public class Menu {
    private String name;
    private MenuItem[] menuItems;
    private MenuItem[] mainMenuItems;

    public Menu(String name, MenuItem[] menuItems) {
        this.name = name;
        this.menuItems = menuItems;
        this.mainMenuItems = menuItems;
    }

    public void printMenu() {
        System.out.println("Меню: " + name);
        for (int i = 0; i < menuItems.length; i++) {
            System.out.println(i + ". " + menuItems[i].getTitle());
        }
    }

    public Menu navigate(Integer index) {
        if (index >= 0 && index <= menuItems.length-1 && menuItems[index].getNextMenu() != null) {
            return menuItems[index].getNextMenu();
        }
        return new Menu("Главное меню", mainMenuItems);  //TODO: ВОзвращает текущие айтемы
    }
}
