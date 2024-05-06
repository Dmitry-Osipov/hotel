import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import program.AppConfig;
import ui.MenuController;

public class TestSpring {
    public static void main(String[] args) {
        try (AnnotationConfigApplicationContext context =
                     new AnnotationConfigApplicationContext(AppConfig.class)) {
            MenuController controller = context.getBean("menuController", MenuController.class);
            controller.run();
        }
    }
}
