import repository.ClientRepository;
import repository.ProvidedServicesRepository;
import repository.RoomRepository;
import repository.RoomReservationRepository;
import repository.ServiceRepository;
import service.ClientService;
import service.RoomService;
import service.ServiceService;
import ui.MenuController;

public class TestUI {
    public static void main(String[] args) {
        MenuController controller = new MenuController(
                new RoomService(new RoomRepository(), new RoomReservationRepository()),
                new ServiceService(new ServiceRepository(), new ProvidedServicesRepository()),
                new ClientService(new ClientRepository())
        );
        controller.run();
    }
}
