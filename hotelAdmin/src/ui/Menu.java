package ui;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Menu {
    private String name;
    private MenuItem[] menuItems;

    public Menu(String name, MenuItem[] menuItems) {
        this.name = name;
        this.menuItems = menuItems;
    }
}
