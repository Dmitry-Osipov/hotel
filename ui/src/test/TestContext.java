import annotations.factory.ApplicationContext;
import lombok.SneakyThrows;
import ui.MenuController;
import utils.AllowedSetStatusRoomConfigurator;

import java.util.List;

public class TestContext {
    @SneakyThrows
    public static void main(String[] args) {
        ApplicationContext context = new ApplicationContext(List.of("ui", "service", "repository"),
                new AllowedSetStatusRoomConfigurator());
        var temp = (MenuController) context.getComponent("menuController");
        temp.run();
    }
}
