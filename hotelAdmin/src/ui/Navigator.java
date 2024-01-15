package ui;

import lombok.Getter;

@Getter
public class Navigator {
    private Menu currentMenu;

    public Navigator(Menu currentMenu) {
        this.currentMenu = currentMenu;
    }

    public void printMenu() {
        currentMenu.printMenu();
    }

    public void navigate(Integer index) {
        currentMenu.getMenuItems()[index].doAction();
        currentMenu = currentMenu.navigate(index);
    }
}
