package ui;

import annotations.factory.InitializeComponent;
import lombok.Getter;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ui.actions.IAction;

import javax.annotation.PostConstruct;

/**
 * Класс отвечает за формирование меню.
 */
@Component
@ToString
public class Builder implements InitializeComponent {
    private static final Logger logger = LoggerFactory.getLogger("AppProcess");
    @Getter
    private Menu rootMenu;
    private final IAction addRoomAction;
    private final IAction addStarsAction;
    private final IAction checkInAction;
    private final IAction countAvailableRoomsAction;
    private final IAction evictAction;
    private final IAction exportReservationsDataAction;
    private final IAction exportRoomsDataAction;
    private final IAction getAvailableRoomsByCapacityAction;
    private final IAction getAvailableRoomsByPriceAction;
    private final IAction getAvailableRoomsByStarsAction;
    private final IAction getAvailableRoomsByTimeAction;
    private final IAction getClientRoomsByCheckOutTimeAction;
    private final IAction getClientRoomsByNumbersAction;
    private final IAction getRoomInfoAction;
    private final IAction getRoomLastClientsAction;
    private final IAction getRoomPriceAction;
    private final IAction getRoomsByCapacityAction;
    private final IAction getRoomsByPriceAction;
    private final IAction getRoomsByStarsAction;
    private final IAction importReservationDataAction;
    private final IAction importRoomsDataAction;
    private final IAction addServiceAction;
    private final IAction exportProvidedServicesDataAction;
    private final IAction exportServicesDataAction;
    private final IAction getClientServicesByPriceAction;
    private final IAction getClientServicesByTimeAction;
    private final IAction getServicePriceAction;
    private final IAction getServicesAction;
    private final IAction importProvidedServicesDataAction;
    private final IAction importServiceDataAction;
    private final IAction provideServiceAction;
    private final IAction addClientAction;
    private final IAction countClientsAction;
    private final IAction exportClientsDataAction;
    private final IAction getClientsAction;
    private final IAction importClientsDataAction;

    public Builder(@Qualifier("getAvailableRoomsByPriceAction") IAction getAvailableRoomsByPriceAction,
                   @Qualifier("addRoomAction") IAction addRoomAction,
                   @Qualifier("addStarsAction") IAction addStarsAction,
                   @Qualifier("checkInAction") IAction checkInAction,
                   @Qualifier("countAvailableRoomsAction") IAction countAvailableRoomsAction,
                   @Qualifier("importReservationDataAction") IAction importReservationDataAction,
                   @Qualifier("getClientServicesByTimeAction") IAction getClientServicesByTimeAction,
                   @Qualifier("getRoomsByCapacityAction") IAction getRoomsByCapacityAction,
                   @Qualifier("evictAction") IAction evictAction,
                   @Qualifier("exportReservationsDataAction") IAction exportReservationsDataAction,
                   @Qualifier("exportServicesDataAction") IAction exportServicesDataAction,
                   @Qualifier("getClientServicesByPriceAction") IAction getClientServicesByPriceAction,
                   @Qualifier("getClientsAction") IAction getClientsAction,
                   @Qualifier("getRoomsByPriceAction") IAction getRoomsByPriceAction,
                   @Qualifier("importProvidedServicesDataAction") IAction importProvidedServicesDataAction,
                   @Qualifier("exportRoomsDataAction") IAction exportRoomsDataAction,
                   @Qualifier("getAvailableRoomsByCapacityAction") IAction getAvailableRoomsByCapacityAction,
                   @Qualifier("getServicePriceAction") IAction getServicePriceAction,
                   @Qualifier("countClientsAction") IAction countClientsAction,
                   @Qualifier("getClientRoomsByCheckOutTimeAction") IAction getClientRoomsByCheckOutTimeAction,
                   @Qualifier("getRoomLastClientsAction") IAction getRoomLastClientsAction,
                   @Qualifier("importRoomsDataAction") IAction importRoomsDataAction,
                   @Qualifier("getAvailableRoomsByStarsAction") IAction getAvailableRoomsByStarsAction,
                   @Qualifier("getRoomInfoAction") IAction getRoomInfoAction,
                   @Qualifier("getRoomsByStarsAction") IAction getRoomsByStarsAction,
                   @Qualifier("getServicesAction") IAction getServicesAction,
                   @Qualifier("getRoomPriceAction") IAction getRoomPriceAction,
                   @Qualifier("importClientsDataAction") IAction importClientsDataAction,
                   @Qualifier("provideServiceAction") IAction provideServiceAction,
                   @Qualifier("addServiceAction") IAction addServiceAction,
                   @Qualifier("exportProvidedServicesDataAction") IAction exportProvidedServicesDataAction,
                   @Qualifier("getAvailableRoomsByTimeAction") IAction getAvailableRoomsByTimeAction,
                   @Qualifier("addClientAction") IAction addClientAction,
                   @Qualifier("importServiceDataAction") IAction importServiceDataAction,
                   @Qualifier("exportClientsDataAction") IAction exportClientsDataAction,
                   @Qualifier("getClientRoomsByNumbersAction") IAction getClientRoomsByNumbersAction) {
        this.getAvailableRoomsByPriceAction = getAvailableRoomsByPriceAction;
        this.addRoomAction = addRoomAction;
        this.addStarsAction = addStarsAction;
        this.checkInAction = checkInAction;
        this.countAvailableRoomsAction = countAvailableRoomsAction;
        this.importReservationDataAction = importReservationDataAction;
        this.getClientServicesByTimeAction = getClientServicesByTimeAction;
        this.getRoomsByCapacityAction = getRoomsByCapacityAction;
        this.evictAction = evictAction;
        this.exportReservationsDataAction = exportReservationsDataAction;
        this.exportServicesDataAction = exportServicesDataAction;
        this.getClientServicesByPriceAction = getClientServicesByPriceAction;
        this.getClientsAction = getClientsAction;
        this.getRoomsByPriceAction = getRoomsByPriceAction;
        this.importProvidedServicesDataAction = importProvidedServicesDataAction;
        this.exportRoomsDataAction = exportRoomsDataAction;
        this.getAvailableRoomsByCapacityAction = getAvailableRoomsByCapacityAction;
        this.getServicePriceAction = getServicePriceAction;
        this.countClientsAction = countClientsAction;
        this.getClientRoomsByCheckOutTimeAction = getClientRoomsByCheckOutTimeAction;
        this.getRoomLastClientsAction = getRoomLastClientsAction;
        this.importRoomsDataAction = importRoomsDataAction;
        this.getAvailableRoomsByStarsAction = getAvailableRoomsByStarsAction;
        this.getRoomInfoAction = getRoomInfoAction;
        this.getRoomsByStarsAction = getRoomsByStarsAction;
        this.getServicesAction = getServicesAction;
        this.getRoomPriceAction = getRoomPriceAction;
        this.importClientsDataAction = importClientsDataAction;
        this.provideServiceAction = provideServiceAction;
        this.addServiceAction = addServiceAction;
        this.exportProvidedServicesDataAction = exportProvidedServicesDataAction;
        this.getAvailableRoomsByTimeAction = getAvailableRoomsByTimeAction;
        this.addClientAction = addClientAction;
        this.importServiceDataAction = importServiceDataAction;
        this.exportClientsDataAction = exportClientsDataAction;
        this.getClientRoomsByNumbersAction = getClientRoomsByNumbersAction;
    }

    /**
     * Метод проводит настройку главного меню.
     */
    @PostConstruct
    @Override
    public void init() {
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
