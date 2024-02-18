package ui;

import essence.person.AbstractClient;
import essence.provided.ProvidedService;
import essence.reservation.RoomReservation;
import essence.room.AbstractRoom;
import essence.service.AbstractService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.client.ClientRepository;
import repository.room.RoomRepository;
import repository.room.RoomReservationRepository;
import repository.service.ProvidedServicesRepository;
import repository.service.ServiceRepository;
import service.ClientService;
import service.RoomService;
import service.ServiceService;
import utils.exceptions.ErrorMessages;
import utils.file.DataPath;
import utils.file.serialize.SerializationUtils;

import java.io.IOException;
import java.util.Scanner;

/**
 * Класс отвечает за работу UI.
 */
public class MenuController {
    private static final Logger logger = LoggerFactory.getLogger("AppProcess");
    private final Builder builder;
    private final Navigator navigator;

    /**
     * Класс отвечает за работу UI.
     */
    public MenuController() {
        builder = new Builder(
                new RoomService(RoomRepository.getInstance(), RoomReservationRepository.getInstance()),
                new ServiceService(ServiceRepository.getInstance(), ProvidedServicesRepository.getInstance()),
                new ClientService(ClientRepository.getInstance())
        );

        builder.buildMenu();
        navigator = new Navigator(builder.getRootMenu());
    }

    /**
     * Метод запускает приложение отеля.
     */
    public void run() {
        logger.info("Запуск приложения");
        deserializeAllRepo(builder.getClientService(), builder.getRoomService(), builder.getServiceService());
        while (true) {
            navigator.printMenu();
            int choice = getUserInput() - 1;
            if (choice == -2) {
                exit();
                break;
            }
            navigator.navigate(choice);
        }
    }

    /**
     * Служебный метод запрашивает у пользователя число, проверяет валидность поступивших данных от пользователя.
     * @return Число.
     */
    private int getUserInput() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nВыберите действие (для выхода введите -1): ");
        while (!scanner.hasNextInt()) {
            logger.info("Пользователь ввёл не целое число");
            System.out.println("\n" + ErrorMessages.INCORRECT_INPUT.getMessage());
            scanner.next();
        }

        int choice = scanner.nextInt();
        logger.info("Пользователь ввёл число {}", choice);
        return choice;
    }

    /**
     * Служебный метод производит user-friendly выход из программы.
     */
    private void exit() {
        logger.info("Выход из приложения");
        System.out.println("\nВыход из программы...");
        serializeAllRepo(builder.getClientService(), builder.getRoomService(), builder.getServiceService());
    }

    /**
     * Служебный метод производит десериализацию всех данных.
     * @param clientService Сервис управления данных по клиентам.
     * @param roomService Сервис управления данных по комнатам.
     * @param serviceService Сервис управления данных по услугам.
     */
    private void deserializeAllRepo(ClientService clientService, RoomService roomService,
                                    ServiceService serviceService) {
        logger.info("Происходит десериализация всех данных");
        deserializeClientRepo(clientService);
        deserializeRoomRepo(roomService);
        deserializeServiceRepo(serviceService);
        deserializeReservationRepo(roomService);
        deserializeProvidedServiceRepo(serviceService);
        logger.info("Десериализация всех данных завершена");
    }

    /**
     * Служебный метод производит десериализацию данных по клиентам.
     * @param clientService Сервис управления данных по клиентам.
     */
    private void deserializeClientRepo(ClientService clientService) {
        logger.info("Происходит десериализация данных по клиентам");
        try {
            String path = DataPath.SERIALIZE_DIRECTORY.getPath() + "clients";
            ClientRepository repo = SerializationUtils.deserialize(ClientRepository.class, path);
            for (AbstractClient client : repo.getClients()) {
                if (!clientService.updateClient(client)) {
                    clientService.addClient(client);
                }
            }
        } catch (IOException e) {
            logger.error("Десериализация данных по клиентам не произошла");
        }
        logger.info("Десериализация данных по клиентам завершена");
    }

    /**
     * Служебный метод производит десериализацию данных по комнатам.
     * @param roomService Сервис управления данных по комнатам.
     */
    private void deserializeRoomRepo(RoomService roomService) {
        logger.info("Происходит десериализация данных по комнатам");
        try {
            String path = DataPath.SERIALIZE_DIRECTORY.getPath() + "rooms";
            RoomRepository repo = SerializationUtils.deserialize(RoomRepository.class, path);
            for (AbstractRoom room : repo.getRooms()) {
                if (!roomService.updateRoom(room)) {
                    roomService.addRoom(room);
                }
            }
        } catch (IOException e) {
            logger.error("Десериализация данных по комнатам не произошла");
        }
        logger.info("Десериализация данных по комнатам завершена");
    }

    /**
     * Служебный метод производит десериализацию данных по резервациям.
     * @param roomService Сервис управления данных по комнатам.
     */
    private void deserializeReservationRepo(RoomService roomService) {
        logger.info("Происходит десериализация данных по резервациям");
        try {
            String path = DataPath.SERIALIZE_DIRECTORY.getPath() + "reservations";
            RoomReservationRepository repo = SerializationUtils.deserialize(RoomReservationRepository.class, path);
            for (RoomReservation reservation : repo.getReservations()) {
                if (!roomService.updateReservation(reservation)) {
                    roomService.addReservation(reservation);
                }
            }
        } catch (IOException e) {
            logger.error("Десериализация данных по резервациям не произошла");
        }
        logger.info("Десериализация данных по резервациям завершена");
    }

    /**
     * Служебный метод производит десериализацию данных по услугам.
     * @param serviceService Сервис управления данных по услугам.
     */
    private void deserializeServiceRepo(ServiceService serviceService) {
        logger.info("Происходит десериализация данных по услугам");
        try {
            String path = DataPath.SERIALIZE_DIRECTORY.getPath() + "services";
            ServiceRepository repo = SerializationUtils.deserialize(ServiceRepository.class, path);
            for (AbstractService service : repo.getServices()) {
                if (!serviceService.updateService(service)) {
                    serviceService.addService(service);
                }
            }
        } catch (IOException e) {
            logger.error("Десериализация данных по услугам не произошла");
        }
        logger.info("Десериализация данных по услугам завершена");
    }

    /**
     * Служебный метод производит десериализацию данных по проведённым услугам.
     * @param serviceService Сервис управления данных по услугам.
     */
    private void deserializeProvidedServiceRepo(ServiceService serviceService) {
        logger.info("Происходит десериализация данных по проведённым услугам");
        try {
            String path = DataPath.SERIALIZE_DIRECTORY.getPath() + "provided_services";
            ProvidedServicesRepository repo = SerializationUtils.deserialize(ProvidedServicesRepository.class, path);
            for (ProvidedService providedService : repo.getProvidedServices()) {
                if (!serviceService.updateProvidedService(providedService)) {
                    serviceService.addProvidedService(providedService);
                }
            }
        } catch (IOException e) {
            logger.error("Десериализация данных по проведённым услугам не произошла");
        }
        logger.info("Десериализация данных по проведённым услугам завершена");
    }

    /**
     * Служебный метод производит сериализацию всех данных.
     * @param clientService Сервис управления данных по клиентам.
     * @param roomService Сервис управления данных по комнатам.
     * @param serviceService Сервис управления данных по услугам.
     */
    private void serializeAllRepo(ClientService clientService, RoomService roomService, ServiceService serviceService) {
        logger.info("Происходит сериализация всех данных");
        serializeClientRepo(clientService);
        serializeRoomRepo(roomService);
        serializeServiceRepo(serviceService);
        serializeReservationRepo(roomService);
        serializeProvidedServiceRepo(serviceService);
        logger.info("Сериализация всех данных завершена");
    }

    /**
     * Служебный метод производит сериализацию данных по клиентам.
     * @param clientService Сервис управления данных по клиентам.
     */
    private void serializeClientRepo(ClientService clientService) {
        logger.info("Происходит сериализация данных по клиентам");
        try {
            String path = DataPath.SERIALIZE_DIRECTORY.getPath() + "clients";
            ClientRepository repo = clientService.getClientRepository();
            SerializationUtils.serialize(repo, path);
        } catch (IOException e) {
            logger.error("Сериализация данных по клиентам не произошла");
        }
        logger.info("Сериализация данных по клиентам завершена");
    }

    /**
     * Служебный метод производит сериализацию данных по комнатам.
     * @param roomService Сервис управления данных по комнатам.
     */
    private void serializeRoomRepo(RoomService roomService) {
        logger.info("Происходит сериализация данных по комнатам");
        try {
            String path = DataPath.SERIALIZE_DIRECTORY.getPath() + "rooms";
            RoomRepository repo = roomService.getRoomRepository();
            SerializationUtils.serialize(repo, path);
        } catch (IOException e) {
            logger.error("Сериализация данных по комнатам не произошла");
        }
        logger.info("Сериализация данных по комнатам завершена");
    }

    /**
     * Служебный метод производит сериализацию данных по резервациям.
     * @param roomService Сервис управления данных по комнатам.
     */
    private void serializeReservationRepo(RoomService roomService) {
        logger.info("Происходит сериализация данных по резервациям");
        try {
            String path = DataPath.SERIALIZE_DIRECTORY.getPath() + "reservations";
            RoomReservationRepository repo = roomService.getReservationRepository();
            SerializationUtils.serialize(repo, path);
        } catch (IOException e) {
            logger.error("Сериализация данных по резервациям не произошла");
        }
        logger.info("Сериализация данных по резервациям завершена");
    }

    /**
     * Служебный метод производит сериализацию данных по услугам.
     * @param serviceService Сервис управления данных по услугам.
     */
    private void serializeServiceRepo(ServiceService serviceService) {
        logger.info("Происходит сериализация данных по услугам");
        try {
            String path = DataPath.SERIALIZE_DIRECTORY.getPath() + "services";
            ServiceRepository repo = serviceService.getServiceRepository();
            SerializationUtils.serialize(repo, path);
        } catch (IOException e) {
            logger.error("Сериализация данных по услугам не произошла");
        }
        logger.info("Сериализация данных по услугам завершена");
    }

    /**
     * Служебный метод производит сериализацию данных по проведённым услугам.
     * @param serviceService Сервис управления данных по услугам.
     */
    private void serializeProvidedServiceRepo(ServiceService serviceService) {
        logger.info("Происходит сериализация данных по проведённым услугам");
        try {
            String path = DataPath.SERIALIZE_DIRECTORY.getPath() + "provided_services";
            ProvidedServicesRepository repo = serviceService.getProvidedServicesRepository();
            SerializationUtils.serialize(repo, path);
        } catch (IOException e) {
            logger.error("Сериализация данных по проведённым услугам не произошла");
        }
        logger.info("Сериализация данных по проведённым услугам завершена");
    }
}
