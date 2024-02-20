import repository.client.ClientRepository;
import repository.room.RoomRepository;
import repository.room.RoomReservationRepository;
import repository.service.ProvidedServicesRepository;
import repository.service.ServiceRepository;
import service.ClientService;
import service.RoomService;
import service.ServiceService;
import ui.MenuController;

public class TestUI {
    public static void main(String[] args) {
        MenuController controller = new MenuController(
                new RoomService(RoomRepository.getInstance(), RoomReservationRepository.getInstance()),
                new ServiceService(ServiceRepository.getInstance(), ProvidedServicesRepository.getInstance()),
                new ClientService(ClientRepository.getInstance())
        );
        controller.run();
    }
}
