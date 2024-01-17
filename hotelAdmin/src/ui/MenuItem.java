package ui;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class MenuItem {
    private final String title;
    private final IAction action;
    private final Menu nextMenu;

    public MenuItem(String title, IAction action, Menu nextMenu) {
        this.title = title;
        this.action = action;
        this.nextMenu = nextMenu;
    }

    public void doAction() {
        if (action != null) {
            action.execute();
        }
    }
}
