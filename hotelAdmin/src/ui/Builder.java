package ui;

import essence.person.Client;
import essence.room.Room;
import essence.service.Service;
import essence.service.ServiceNames;
import lombok.Getter;
import lombok.ToString;
import service.ClientService;
import service.RoomService;
import service.ServiceService;

@Getter
@ToString
public class Builder {
    private Menu rootMenu;
    private RoomService roomService;
    private ServiceService serviceService;
    private ClientService clientService;

    public Builder(RoomService roomService, ServiceService serviceService, ClientService clientService) {
        this.roomService = roomService;
        this.serviceService = serviceService;
        this.clientService = clientService;
    }

    public void buildMenu() {
        rootMenu = buildMainMenu();
    }

    private Menu buildMainMenu() {
        MenuItem roomsMenu = new MenuItem("Управление комнатами",
                () -> System.out.println("Переход...\n"), buildRoomsMenu());
        MenuItem serviceMenu = new MenuItem("Управление услугами",
                () -> System.out.println("Переход...\n"), buildServicesMenu());
        MenuItem clientMenu = new MenuItem("Управление клиентами",
                () -> System.out.println("Переход...\n"), buildClientsMenu());

        return new Menu("Главное меню", new MenuItem[]{roomsMenu, serviceMenu, clientMenu});
    }

    private Menu buildRoomsMenu() {
        MenuItem addRoom = new MenuItem("Добавить комнату",
                () -> System.out.println(roomService.addRoom(
                        new Room(100, 2, 3000))), null);  //TODO: Изменить
        MenuItem addStars = new MenuItem("Добавить звёзд комнате",
                () -> System.out.println(roomService.addStarsToRoom(roomService.roomsByStars().getFirst(), 3)), //TODO: Изменить
                null);

        return new Menu("Управление комнатами", new MenuItem[]{addRoom, addStars});
    }

    private Menu buildServicesMenu() {
        MenuItem addService = new MenuItem("Добавить услугу",
                () -> System.out.println(serviceService.addService(new Service(ServiceNames.BREAKFAST, 3200))),
                null);

        return new Menu("Управление услугами", new MenuItem[]{addService});
    }

    private Menu buildClientsMenu() {
        MenuItem addClient = new MenuItem("Добавить клиента",
                () -> System.out.println(clientService.addClient(
                        new Client("Osipov Dmitry Romanovich", "8-902-902-98-11"))),
                null);

        return new Menu("Управление клиентами", new MenuItem[]{addClient});
    }
}
