import annotations.factory.ApplicationContext;
import annotations.factory.ComponentFactory;
import lombok.SneakyThrows;
import utils.AllowedSetStatusRoomConfigurator;

import java.util.ArrayList;
import java.util.List;

public class TestFactory {
    @SneakyThrows
    public static void main(String[] args) {
        ComponentFactory factory = new ComponentFactory();
        factory.addConfig(new AllowedSetStatusRoomConfigurator());
        List<String> packages = new ArrayList<>(List.of("repository", "service"));
        for (String packageName : packages) {
            factory.componentInitialization(packageName);
        }
        factory.dependencySetting();
        factory.configureComponents();

        System.out.println(factory.getComponent("clientRepository"));
        System.out.println(factory.getComponent("roomRepository"));
        System.out.println(factory.getComponent("roomReservationRepository"));
        System.out.println(factory.getComponent("serviceRepository"));
        System.out.println(factory.getComponent("providedServicesRepository"));
        System.out.println(factory.getComponent("clientService"));
        System.out.println(factory.getComponent("roomService"));
        System.out.println(factory.getComponent("serviceService"));

        System.out.println("*************************************************");
        ApplicationContext context = new ApplicationContext(List.of("repository", "service"),
                new AllowedSetStatusRoomConfigurator());
        System.out.println(context.getComponent("clientRepository"));
        System.out.println(context.getComponent("roomRepository"));
        System.out.println(context.getComponent("roomReservationRepository"));
        System.out.println(context.getComponent("serviceRepository"));
        System.out.println(context.getComponent("providedServicesRepository"));
        System.out.println(context.getComponent("clientService"));
        System.out.println(context.getComponent("roomService"));
        System.out.println(context.getComponent("serviceService"));
    }
}
