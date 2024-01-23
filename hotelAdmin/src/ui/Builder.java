package ui;

import lombok.Getter;
import lombok.ToString;
import repository.client.ClientRepository;
import repository.room.RoomRepository;
import repository.room.RoomReservationRepository;
import service.ClientService;
import service.RoomService;
import ui.actions.client.AddClientAction;
import ui.actions.client.GetClientsAction;
import ui.actions.room.AddRoomAction;
import ui.actions.room.AddStarsAction;
import ui.actions.room.CheckInAction;
import ui.actions.room.GetAvailableRoomsByCapacityAction;
import ui.actions.room.GetAvailableRoomsByPriceAction;
import ui.actions.room.GetAvailableRoomsByStarsAction;
import ui.actions.room.GetAvailableRoomsByTimeAction;
import ui.actions.room.GetClientRoomsByCheckOutTimeAction;
import ui.actions.room.GetClientRoomsByNumbersAction;
import ui.actions.room.GetRoomPriceAction;
import ui.actions.room.GetRoomInfoAction;
import ui.actions.room.GetRoomLastClientsAction;
import ui.actions.room.GetRoomsByCapacityAction;
import ui.actions.room.GetRoomsByPriceAction;
import ui.actions.room.GetRoomsByStarsAction;
import ui.actions.service.AddServiceAction;
import ui.actions.service.GetClientServicesByPriceAction;
import ui.actions.service.GetClientServicesByTimeAction;
import ui.actions.service.GetServicePriceAction;
import ui.actions.service.GetServicesAction;
import ui.actions.service.ProvideServiceAction;
import ui.actions.room.EvictAction;


/**
 * Класс отвечает за формирование меню.
 */
@Getter
@ToString
public class Builder {
    private Menu rootMenu;

    /**
     * Метод формирует меню.
     */
    public void buildMenu() {
        rootMenu = buildMainMenu();
    }

    /**
     * Служебный метод предназначен для формирования главного меню.
     * @return Главное меню.
     */
    private Menu buildMainMenu() {
        MenuItem roomsMenu = new MenuItem("Управление комнатами", null, buildRoomsMenu());
        MenuItem serviceMenu = new MenuItem("Управление услугами", null, buildServicesMenu());
        MenuItem clientMenu = new MenuItem("Управление клиентами", null, buildClientsMenu());

        return new Menu("Главное меню", new MenuItem[]{roomsMenu, serviceMenu, clientMenu});
    }

    /**
     * Служебный метод предназначен для формирования меню управления комнатами.
     * @return Меню управления комнатами.
     */
    private Menu buildRoomsMenu() {
        MenuItem addRoom = new MenuItem("Добавить комнату", new AddRoomAction(), null);

        MenuItem getPrice = new MenuItem("Вывести стоимость комнаты", new GetRoomPriceAction(), null);

        MenuItem addStars = new MenuItem("Добавить звёзд комнате", new AddStarsAction(), null);

        MenuItem checkIn = new MenuItem("Заселить клиентов в комнату", new CheckInAction(), null);

        MenuItem evict = new MenuItem("Выселить клиентов из комнаты", new EvictAction(), null);

        MenuItem roomsByStars = new MenuItem("Вывести список всех комнат, отсортированных по убыванию звёзд",
                new GetRoomsByStarsAction(), null);

        MenuItem roomsByPrice = new MenuItem("Вывести список всех комнат, отсортированных по возрастанию цены",
                new GetRoomsByPriceAction(), null);

        MenuItem roomsByCapacity = new MenuItem(
                "Вывести список всех комнат, отсортированных по возрастанию вместимости",
                new GetRoomsByCapacityAction(), null);

        MenuItem availableRoomsByStars = new MenuItem(
                "Вывести список свободных комнат, отсортированных по убыванию звёзд",
                new GetAvailableRoomsByStarsAction(), null);

        MenuItem availableRoomsByPrice = new MenuItem(
                "Вывести список свободных комнат, отсортированных по возрастанию цены",
                new GetAvailableRoomsByPriceAction(), null);

        MenuItem availableRoomsByCapacity = new MenuItem(
                "Вывести список свободных комнат, отсортированных по возрастанию вместимости",
                new GetAvailableRoomsByCapacityAction(), null);

        MenuItem countAvailableRooms = new MenuItem("Вывести количество свободных комнат",
                () -> System.out.println("\nКоличество свободных комнат - " +
                        new RoomService(RoomRepository.getInstance(), RoomReservationRepository.getInstance())
                                .countAvailableRooms()), null);

        MenuItem getRoomLastClients = new MenuItem("Вывести список последних клиентов комнаты",
                new GetRoomLastClientsAction(), null);

        MenuItem getRoomInfo = new MenuItem("Вывести полную информацию о комнате",
                new GetRoomInfoAction(), null);

        MenuItem getClientRoomsByNumbers = new MenuItem(
                "Вывести список всех комнат клиента, отсортированных по возрастанию номера комнаты",
                new GetClientRoomsByNumbersAction(), null);

        MenuItem getClientRoomsByCheckOutTime = new MenuItem(
                "Вывести список всех комнат клиента, отсортированных по убыванию времени выезда",
                new GetClientRoomsByCheckOutTimeAction(), null);

        MenuItem getAvailableRoomsByTime = new MenuItem("Вывести список свободных комнат с конкретного времени",
                new GetAvailableRoomsByTimeAction(), null);

        return new Menu("Управление комнатами", new MenuItem[]{addRoom, getPrice, addStars, checkIn, evict,
                roomsByStars, roomsByPrice, roomsByCapacity, availableRoomsByStars, availableRoomsByPrice,
                availableRoomsByCapacity, countAvailableRooms, getRoomLastClients, getRoomInfo, getClientRoomsByNumbers,
                getClientRoomsByCheckOutTime, getAvailableRoomsByTime});
    }

    /**
     * Служебный метод предназначен для формирования меню услуг.
     * @return Меню услуг.
     */
    private Menu buildServicesMenu() {
        MenuItem addService = new MenuItem("Добавить услугу", new AddServiceAction(), null);

        MenuItem getPrice = new MenuItem("Вывести стоимость услуги", new GetServicePriceAction(), null);

        MenuItem getServices = new MenuItem("Вывести список всех услуг", new GetServicesAction(), null);

        MenuItem provideService = new MenuItem("Провести услугу клиенту", new ProvideServiceAction(),
                null);

        MenuItem getClientServicesByPrice = new MenuItem(
                "Вывести список услуг, оказанных клиенту и отсортированных по возрастанию цены",
                new GetClientServicesByPriceAction(), null);

        MenuItem getClientServicesByTime = new MenuItem(
                "Вывести список услуг, оказанных клиенту и отсортированных по убыванию времени оказания",
                new GetClientServicesByTimeAction(), null);

        return new Menu("Управление услугами", new MenuItem[]{addService, getPrice, getServices, provideService,
                getClientServicesByPrice, getClientServicesByTime});
    }

    /**
     * Служебный метод предназначен для формирования меню управления клиентами.
     * @return Меню управления клиентами.
     */
    private Menu buildClientsMenu() {
        MenuItem addClient = new MenuItem("Добавить клиента", new AddClientAction(), null);

        MenuItem getClients = new MenuItem("Вывести список всех клиентов", new GetClientsAction(), null);

        MenuItem countClients = new MenuItem("Вывести количество клиентов",
                () -> System.out.println("\nКоличество клиентов - " + new ClientService(ClientRepository.getInstance())
                        .countClients()),
                null);

        return new Menu("Управление клиентами", new MenuItem[]{addClient, getClients, countClients});
    }
}
