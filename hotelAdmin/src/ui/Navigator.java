package ui;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Navigator {
    private Menu currentMenu;
    private final Menu rootMenu;

    public Navigator(Menu currentMenu) {
        this.currentMenu = currentMenu;
        this.rootMenu = currentMenu;
    }

    public void printMenu() {
        System.out.println("\nМеню: " + currentMenu.getName());
        for (int i = 0; i < currentMenu.getMenuItems().length; i++) {
            System.out.println(i+1 + ". " + currentMenu.getMenuItems()[i].title());
        }
    }

    public void navigate(Integer index) {
        currentMenu.getMenuItems()[index].doAction();
        currentMenu = getNextMenu(index);
    }

    private Menu getNextMenu(Integer index) {
        if (index >= 0 && index <= currentMenu.getMenuItems().length-1
                && currentMenu.getMenuItems()[index].nextMenu() != null) {
            return currentMenu.getMenuItems()[index].nextMenu();
        }

        return rootMenu;
    }
}
