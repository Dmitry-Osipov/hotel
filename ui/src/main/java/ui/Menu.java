package ui;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Класс меню.
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
public class Menu {
    private String name;
    private MenuItem[] menuItems;
}
