package ui;

import annotations.annotation.Autowired;
import annotations.annotation.Component;
import annotations.annotation.InjectByInterface;
import annotations.factory.InitializeComponent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.ClientService;
import service.RoomService;
import service.ServiceService;
import ui.actions.IAction;
import ui.actions.client.AddClientAction;
import ui.actions.client.CountClientsAction;
import ui.actions.client.ExportClientsDataAction;
import ui.actions.client.GetClientsAction;
import ui.actions.client.ImportClientsDataAction;
import ui.actions.room.AddRoomAction;
import ui.actions.room.AddStarsAction;
import ui.actions.room.CheckInAction;
import ui.actions.room.CountAvailableRoomsAction;
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
@Component
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Builder implements InitializeComponent {
    private static final Logger logger = LoggerFactory.getLogger("AppProcess");
    private Menu rootMenu;
    @Autowired
    private RoomService roomService;
    @Autowired
    private ServiceService serviceService;
    @Autowired
    private ClientService clientService;
    @InjectByInterface(clazz = AddRoomAction.class)
    private IAction addRoomAction;
    @InjectByInterface(clazz = AddStarsAction.class)
    private IAction addStarsAction;
    @InjectByInterface(clazz = CheckInAction.class)
    private IAction checkInAction;
    @InjectByInterface(clazz = CountAvailableRoomsAction.class)
    private IAction countAvailableRoomsAction;
    @InjectByInterface(clazz = EvictAction.class)
    private IAction evictAction;
    @InjectByInterface(clazz = ExportReservationsDataAction.class)
    private IAction exportReservationsDataAction;
    @InjectByInterface(clazz = ExportRoomsDataAction.class)
    private IAction exportRoomsDataAction;
    @InjectByInterface(clazz = GetAvailableRoomsByCapacityAction.class)
    private IAction getAvailableRoomsByCapacityAction;
    @InjectByInterface(clazz = GetAvailableRoomsByPriceAction.class)
    private IAction getAvailableRoomsByPriceAction;
    @InjectByInterface(clazz = GetAvailableRoomsByStarsAction.class)
    private IAction getAvailableRoomsByStarsAction;
    @InjectByInterface(clazz = GetAvailableRoomsByTimeAction.class)
    private IAction getAvailableRoomsByTimeAction;
    @InjectByInterface(clazz = GetClientRoomsByCheckOutTimeAction.class)
    private IAction getClientRoomsByCheckOutTimeAction;
    @InjectByInterface(clazz = GetClientRoomsByNumbersAction.class)
    private IAction getClientRoomsByNumbersAction;
    @InjectByInterface(clazz = GetRoomInfoAction.class)
    private IAction getRoomInfoAction;
    @InjectByInterface(clazz = GetRoomLastClientsAction.class)
    private IAction getRoomLastClientsAction;
    @InjectByInterface(clazz = GetRoomPriceAction.class)
    private IAction getRoomPriceAction;
    @InjectByInterface(clazz = GetRoomsByCapacityAction.class)
    private IAction getRoomsByCapacityAction;
    @InjectByInterface(clazz = GetRoomsByPriceAction.class)
    private IAction getRoomsByPriceAction;
    @InjectByInterface(clazz = GetRoomsByStarsAction.class)
    private IAction getRoomsByStarsAction;
    @InjectByInterface(clazz = ImportReservationDataAction.class)
    private IAction importReservationDataAction;
    @InjectByInterface(clazz = ImportRoomsDataAction.class)
    private IAction importRoomsDataAction;
    @InjectByInterface(clazz = AddServiceAction.class)
    private IAction addServiceAction;
    @InjectByInterface(clazz = ExportProvidedServicesDataAction.class)
    private IAction exportProvidedServicesDataAction;
    @InjectByInterface(clazz = ExportServicesDataAction.class)
    private IAction exportServicesDataAction;
    @InjectByInterface(clazz = GetClientServicesByPriceAction.class)
    private IAction getClientServicesByPriceAction;
    @InjectByInterface(clazz = GetClientServicesByTimeAction.class)
    private IAction getClientServicesByTimeAction;
    @InjectByInterface(clazz = GetServicePriceAction.class)
    private IAction getServicePriceAction;
    @InjectByInterface(clazz = GetServicesAction.class)
    private IAction getServicesAction;
    @InjectByInterface(clazz = ImportProvidedServicesDataAction.class)
    private IAction importProvidedServicesDataAction;
    @InjectByInterface(clazz = ImportServiceDataAction.class)
    private IAction importServiceDataAction;
    @InjectByInterface(clazz = ProvideServiceAction.class)
    private IAction provideServiceAction;
    @InjectByInterface(clazz = AddClientAction.class)
    private IAction addClientAction;
    @InjectByInterface(clazz = CountClientsAction.class)
    private IAction countClientsAction;
    @InjectByInterface(clazz = ExportClientsDataAction.class)
    private IAction exportClientsDataAction;
    @InjectByInterface(clazz = GetClientsAction.class)
    private IAction getClientsAction;
    @InjectByInterface(clazz = ImportClientsDataAction.class)
    private IAction importClientsDataAction;

    /**
     * Класс отвечает за формирование меню.
     * @param roomService Класс обработки данных по комнатам.
     * @param serviceService Класс обработки данных по услугам.
     * @param clientService Класс обработки данных по клиентам.
     */
    @Deprecated(forRemoval = true)
    public Builder(RoomService roomService, ServiceService serviceService, ClientService clientService) {
        this.roomService = roomService;
        this.serviceService = serviceService;
        this.clientService = clientService;
    }

    /**
     * Метод проводит настройку главного меню.
     */
    @Override
    public void init() {
        rootMenu = buildMainMenu();
    }

    /**
     * Метод формирует меню.
     */
    @Deprecated(forRemoval = true)
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
        MenuItem addRoom = new MenuItem("Добавить комнату", addRoomAction, null);

        MenuItem exportRoomsData = new MenuItem("Экспортировать данные по комнатам в CSV",
                exportRoomsDataAction, null);

        MenuItem exportReservationsData = new MenuItem("Экспортировать данные по резервациям в CSV",
                exportReservationsDataAction, null);

        MenuItem importRoomsData = new MenuItem("Импортировать данные по комнатам из CSV",
                importRoomsDataAction, null);

        MenuItem importReservationsData = new MenuItem("Импортировать данные по резервациям из CSV",
                importReservationDataAction, null);

        MenuItem getPrice = new MenuItem("Вывести стоимость комнаты", getRoomPriceAction, null);

        MenuItem addStars = new MenuItem("Добавить звёзд комнате", addStarsAction, null);

        MenuItem checkIn = new MenuItem("Заселить клиентов в комнату", checkInAction, null);

        MenuItem evict = new MenuItem("Выселить клиентов из комнаты", evictAction, null);

        MenuItem roomsByStars = new MenuItem("Вывести список всех комнат, отсортированных по убыванию звёзд",
                getRoomsByStarsAction, null);

        MenuItem roomsByPrice = new MenuItem("Вывести список всех комнат, отсортированных по возрастанию цены",
                getRoomsByPriceAction, null);

        MenuItem roomsByCapacity = new MenuItem(
                "Вывести список всех комнат, отсортированных по возрастанию вместимости",
                getRoomsByCapacityAction, null);

        MenuItem availableRoomsByStars = new MenuItem(
                "Вывести список свободных комнат, отсортированных по убыванию звёзд",
                getAvailableRoomsByStarsAction, null);

        MenuItem availableRoomsByPrice = new MenuItem(
                "Вывести список свободных комнат, отсортированных по возрастанию цены",
                getAvailableRoomsByPriceAction, null);

        MenuItem availableRoomsByCapacity = new MenuItem(
                "Вывести список свободных комнат, отсортированных по возрастанию вместимости",
                getAvailableRoomsByCapacityAction, null);

        MenuItem countAvailableRooms = new MenuItem("Вывести количество свободных комнат",
                countAvailableRoomsAction, null);

        MenuItem getRoomLastClients = new MenuItem("Вывести список последних клиентов комнаты",
                getRoomLastClientsAction, null);

        MenuItem getRoomInfo = new MenuItem("Вывести полную информацию о комнате",
                getRoomInfoAction, null);

        MenuItem getClientRoomsByNumbers = new MenuItem(
                "Вывести список всех комнат клиента, отсортированных по возрастанию номера комнаты",
                getClientRoomsByNumbersAction, null);

        MenuItem getClientRoomsByCheckOutTime = new MenuItem(
                "Вывести список всех комнат клиента, отсортированных по убыванию времени выезда",
                getClientRoomsByCheckOutTimeAction, null);

        MenuItem getAvailableRoomsByTime = new MenuItem("Вывести список свободных комнат с конкретного времени",
                getAvailableRoomsByTimeAction, null);

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
        MenuItem addService = new MenuItem("Добавить услугу", addServiceAction, null);

        MenuItem exportServicesData = new MenuItem("Экспортировать данные по услугам в CSV",
                exportServicesDataAction, null);

        MenuItem exportProvidedServicesData = new MenuItem("Экспортировать данные по проведённым услугам в CSV",
                exportProvidedServicesDataAction, null);

        MenuItem importServicesData = new MenuItem("Импортировать данные по услугам из CSV",
                importServiceDataAction, null);

        MenuItem importProvidedServicesData = new MenuItem("Импортировать данные по проведённым услугам из CSV",
                importProvidedServicesDataAction, null);

        MenuItem getPrice = new MenuItem("Вывести стоимость услуги", getServicePriceAction, null);

        MenuItem getServices = new MenuItem("Вывести список всех услуг", getServicesAction, null);

        MenuItem provideService = new MenuItem("Провести услугу клиенту", provideServiceAction, null);

        MenuItem getClientServicesByPrice = new MenuItem(
                "Вывести список услуг, оказанных клиенту и отсортированных по возрастанию цены",
                getClientServicesByPriceAction, null);

        MenuItem getClientServicesByTime = new MenuItem(
                "Вывести список услуг, оказанных клиенту и отсортированных по убыванию времени оказания",
                getClientServicesByTimeAction, null);

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
        MenuItem addClient = new MenuItem("Добавить клиента", addClientAction, null);

        MenuItem exportClientsData = new MenuItem("Экспортировать данные по клиентам в CSV",
                exportClientsDataAction, null);

        MenuItem importClientsData = new MenuItem("Импортировать данные по клиентам из CSV",
                importClientsDataAction, null);

        MenuItem getClients = new MenuItem("Вывести список всех клиентов", getClientsAction, null);

        MenuItem countClients = new MenuItem("Вывести количество клиентов", countClientsAction, null);

        logger.info("Меню клиентов сформировано");
        return new Menu("Управление клиентами", new MenuItem[]{addClient, importClientsData, exportClientsData,
                getClients, countClients});
    }
}
