package program;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ui.MenuController;

public class Program {
    public static void main(String[] args) {
        try (AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(AppConfig.class)) {
            MenuController controller = context.getBean("menuController", MenuController.class);
            controller.run();
        }
    }
}
