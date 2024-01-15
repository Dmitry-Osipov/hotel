package test;

import essence.person.Client;
import essence.room.Room;
import essence.service.Service;
import essence.service.ServiceNames;
import repository.client.ClientRepository;
import repository.room.RoomRepository;
import repository.room.RoomReservationRepository;
import repository.service.ProvidedServicesRepository;
import repository.service.ServiceRepository;
import service.ClientService;
import service.RoomService;
import service.ServiceService;
import ui.Builder;
import ui.MenuController;
import ui.Navigator;

public class TestUI {
    public static void main(String[] args) {
        RoomRepository rooms = new RoomRepository();
        RoomReservationRepository reservations = new RoomReservationRepository();
        RoomService roomService = new RoomService(rooms, reservations);

        ServiceRepository services = new ServiceRepository();
        ProvidedServicesRepository providedServices = new ProvidedServicesRepository();
        ServiceService serviceService = new ServiceService(services, providedServices);

        ClientRepository clients = new ClientRepository();
        ClientService clientService = new ClientService(clients);

        Builder builder = new Builder(roomService, serviceService, clientService);
        builder.buildMenu();
        Navigator navigator = new Navigator(builder.getRootMenu());
        MenuController controller = new MenuController(builder, navigator);
        controller.run();
    }
}
