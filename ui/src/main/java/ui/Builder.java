package ui;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.ClientService;
import service.RoomService;
import service.ServiceService;
import ui.actions.client.AddClientAction;
import ui.actions.client.ExportClientsDataAction;
import ui.actions.client.GetClientsAction;
import ui.actions.client.ImportClientsDataAction;
import ui.actions.room.AddRoomAction;
import ui.actions.room.AddStarsAction;
import ui.actions.room.CheckInAction;
import ui.actions.room.EvictAction;
import ui.actions.room.ExportReservationsDataAction;
import ui.actions.room.ExportRoomsDataAction;
import ui.actions.room.GetAvailableRoomsByCapacityAction;
import ui.actions.room.GetAvailableRoomsByPriceAction;
import ui.actions.room.GetAvailableRoomsByStarsAction;
import ui.actions.room.GetAvailableRoomsByTimeAction;
import ui.actions.room.GetClientRoomsByCheckOutTimeAction;
import ui.actions.room.GetClientRoomsByNumbersAction;
import ui.actions.room.GetRoomInfoAction;
import ui.actions.room.GetRoomLastClientsAction;
import ui.actions.room.GetRoomPriceAction;
import ui.actions.room.GetRoomsByCapacityAction;
import ui.actions.room.GetRoomsByPriceAction;
import ui.actions.room.GetRoomsByStarsAction;
import ui.actions.room.ImportReservationDataAction;
import ui.actions.room.ImportRoomsDataAction;
import ui.actions.service.AddServiceAction;
import ui.actions.service.ExportProvidedServicesDataAction;
import ui.actions.service.ExportServicesDataAction;
import ui.actions.service.GetClientServicesByPriceAction;
import ui.actions.service.GetClientServicesByTimeAction;
import ui.actions.service.GetServicePriceAction;
import ui.actions.service.GetServicesAction;
import ui.actions.service.ImportProvidedServicesDataAction;
import ui.actions.service.ImportServiceDataAction;
import ui.actions.service.ProvideServiceAction;

/**
 * Класс отвечает за формирование меню.
 */
@Getter
public class Builder {
    private static final Logger logger = LoggerFactory.getLogger("AppProcess");
    private Menu rootMenu;
    private final RoomService roomService;
    private final ServiceService serviceService;
    private final ClientService clientService;

    /**
     * Класс отвечает за формирование меню.
     * @param roomService Класс обработки данных по комнатам.
     * @param serviceService Класс обработки данных по услугам.
     * @param clientService Класс обработки данных по клиентам.
     */
    public Builder(RoomService roomService, ServiceService serviceService, ClientService clientService) {
        this.roomService = roomService;
        this.serviceService = serviceService;
        this.clientService = clientService;
    }

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
        logger.info("Формирование главного меню");
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
        logger.info("Формирование меню комнат");
        MenuItem addRoom = new MenuItem("Добавить комнату", new AddRoomAction(roomService), null);

        MenuItem exportRoomsData = new MenuItem("Экспортировать данные по комнатам в CSV",
                new ExportRoomsDataAction(roomService), null);

        MenuItem exportReservationsData = new MenuItem("Экспортировать данные по резервациям в CSV",
                new ExportReservationsDataAction(roomService), null);

        MenuItem importRoomsData = new MenuItem("Импортировать данные по комнатам из CSV",
                new ImportRoomsDataAction(roomService), null);

        MenuItem importReservationsData = new MenuItem("Импортировать данные по резервациям из CSV",
                new ImportReservationDataAction(roomService), null);

        MenuItem getPrice = new MenuItem("Вывести стоимость комнаты", new GetRoomPriceAction(roomService),
                null);

        MenuItem addStars = new MenuItem("Добавить звёзд комнате", new AddStarsAction(roomService), null);

        MenuItem checkIn = new MenuItem("Заселить клиентов в комнату", new CheckInAction(roomService),
                null);

        MenuItem evict = new MenuItem("Выселить клиентов из комнаты", new EvictAction(roomService), null);

        MenuItem roomsByStars = new MenuItem("Вывести список всех комнат, отсортированных по убыванию звёзд",
                new GetRoomsByStarsAction(roomService), null);

        MenuItem roomsByPrice = new MenuItem("Вывести список всех комнат, отсортированных по возрастанию цены",
                new GetRoomsByPriceAction(roomService), null);

        MenuItem roomsByCapacity = new MenuItem(
                "Вывести список всех комнат, отсортированных по возрастанию вместимости",
                new GetRoomsByCapacityAction(roomService), null);

        MenuItem availableRoomsByStars = new MenuItem(
                "Вывести список свободных комнат, отсортированных по убыванию звёзд",
                new GetAvailableRoomsByStarsAction(roomService), null);

        MenuItem availableRoomsByPrice = new MenuItem(
                "Вывести список свободных комнат, отсортированных по возрастанию цены",
                new GetAvailableRoomsByPriceAction(roomService), null);

        MenuItem availableRoomsByCapacity = new MenuItem(
                "Вывести список свободных комнат, отсортированных по возрастанию вместимости",
                new GetAvailableRoomsByCapacityAction(roomService), null);

        MenuItem countAvailableRooms = new MenuItem("Вывести количество свободных комнат",
                () -> System.out.println("\nКоличество свободных комнат - " + roomService.countAvailableRooms()),
                null);

        MenuItem getRoomLastClients = new MenuItem("Вывести список последних клиентов комнаты",
                new GetRoomLastClientsAction(roomService), null);

        MenuItem getRoomInfo = new MenuItem("Вывести полную информацию о комнате",
                new GetRoomInfoAction(roomService), null);

        MenuItem getClientRoomsByNumbers = new MenuItem(
                "Вывести список всех комнат клиента, отсортированных по возрастанию номера комнаты",
                new GetClientRoomsByNumbersAction(roomService), null);

        MenuItem getClientRoomsByCheckOutTime = new MenuItem(
                "Вывести список всех комнат клиента, отсортированных по убыванию времени выезда",
                new GetClientRoomsByCheckOutTimeAction(roomService), null);

        MenuItem getAvailableRoomsByTime = new MenuItem("Вывести список свободных комнат с конкретного времени",
                new GetAvailableRoomsByTimeAction(roomService), null);

        logger.info("Меню комнат сформировано");
        return new Menu("Управление комнатами", new MenuItem[]{addRoom, importRoomsData, importReservationsData,
                exportRoomsData, exportReservationsData, getPrice, addStars, checkIn, evict, roomsByStars,
                roomsByPrice, roomsByCapacity, availableRoomsByStars, availableRoomsByPrice, availableRoomsByCapacity,
                countAvailableRooms, getRoomLastClients, getRoomInfo, getClientRoomsByNumbers,
                getClientRoomsByCheckOutTime, getAvailableRoomsByTime});
    }

    /**
     * Служебный метод предназначен для формирования меню услуг.
     * @return Меню услуг.
     */
    private Menu buildServicesMenu() {
        logger.info("Формирование меню услуг");
        MenuItem addService = new MenuItem("Добавить услугу", new AddServiceAction(serviceService), null);

        MenuItem exportServicesData = new MenuItem("Экспортировать данные по услугам в CSV",
                new ExportServicesDataAction(serviceService), null);

        MenuItem exportProvidedServicesData = new MenuItem("Экспортировать данные по проведённым услугам в CSV",
                new ExportProvidedServicesDataAction(serviceService), null);

        MenuItem importServicesData = new MenuItem("Импортировать данные по услугам из CSV",
                new ImportServiceDataAction(serviceService), null);

        MenuItem importProvidedServicesData = new MenuItem("Импортировать данные по проведённым услугам из CSV",
                new ImportProvidedServicesDataAction(serviceService), null);

        MenuItem getPrice = new MenuItem("Вывести стоимость услуги", new GetServicePriceAction(serviceService),
                null);

        MenuItem getServices = new MenuItem("Вывести список всех услуг", new GetServicesAction(serviceService),
                null);

        MenuItem provideService = new MenuItem("Провести услугу клиенту", new ProvideServiceAction(serviceService),
                null);

        MenuItem getClientServicesByPrice = new MenuItem(
                "Вывести список услуг, оказанных клиенту и отсортированных по возрастанию цены",
                new GetClientServicesByPriceAction(serviceService), null);

        MenuItem getClientServicesByTime = new MenuItem(
                "Вывести список услуг, оказанных клиенту и отсортированных по убыванию времени оказания",
                new GetClientServicesByTimeAction(serviceService), null);

        logger.info("Меню услуг сформировано");
        return new Menu("Управление услугами", new MenuItem[]{addService, importServicesData,
                importProvidedServicesData, exportServicesData, exportProvidedServicesData, getPrice, getServices,
                provideService, getClientServicesByPrice, getClientServicesByTime});
    }

    /**
     * Служебный метод предназначен для формирования меню управления клиентами.
     * @return Меню управления клиентами.
     */
    private Menu buildClientsMenu() {
        logger.info("Формирование меню клиентов");
        MenuItem addClient = new MenuItem("Добавить клиента", new AddClientAction(clientService), null);

        MenuItem exportClientsData = new MenuItem("Экспортировать данные по клиентам в CSV",
                new ExportClientsDataAction(clientService), null);

        MenuItem importClientsData = new MenuItem("Импортировать данные по клиентам из CSV",
                new ImportClientsDataAction(clientService), null);

        MenuItem getClients = new MenuItem("Вывести список всех клиентов", new GetClientsAction(clientService),
                null);

        MenuItem countClients = new MenuItem("Вывести количество клиентов",
                () -> System.out.println("\nКоличество клиентов - " + clientService.countClients()),
                null);

        logger.info("Меню клиентов сформировано");
        return new Menu("Управление клиентами", new MenuItem[]{addClient, importClientsData, exportClientsData,
                getClients, countClients});
    }
}
