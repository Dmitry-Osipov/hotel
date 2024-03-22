import dao.JdbcDao;
import essence.person.Client;
import essence.provided.ProvidedService;
import essence.reservation.RoomReservation;
import essence.room.Room;
import essence.room.RoomStatusTypes;
import essence.service.Service;
import essence.service.ServiceNames;
import essence.service.ServiceStatusTypes;
import lombok.SneakyThrows;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class TestDAO {
    @SneakyThrows
    public static void main(String[] args) {
        testGetOneRoom();
        testGetOneService();
        testGetOneClient();
        testGetOneRoomReservation();
        testGetOneProvidedService();
        testGetAllRooms();
        testGetAllServices();
        testGetAllClients();
        testGetAllRoomReservations();
        testGetAllProvidedServices();
        testSaveRoom();
        testSaveService();
        testSaveClient();
        testSaveRoomReservation();
        testSaveProvidedService();
        testUpdateRoom();
        testUpdateService();
        testUpdateClient();
        testUpdateRoomReservation();
        testUpdateProvidedService();
        testDeleteRoomReservation();
        testDeleteProvidedService();
        testDeleteRoom();
        testDeleteService();
        testDeleteClient();
    }

    public static void testGetOneRoom() throws SQLException, NoSuchFieldException, InvocationTargetException,
            NoSuchMethodException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        JdbcDao jdbcDao = new JdbcDao();
        jdbcDao.setDbUrl("jdbc:mysql://localhost:3306/hotel");
        jdbcDao.setDbUser("hotel_user");
        jdbcDao.setDbPassword("R7hB2fK9sL6e");
        System.out.println(jdbcDao.getOne(1, Room.class));
        System.out.println("--------------------------------------");
    }

    public static void testGetOneService() throws SQLException, NoSuchFieldException, InvocationTargetException,
            NoSuchMethodException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        JdbcDao jdbcDao = new JdbcDao();
        jdbcDao.setDbUrl("jdbc:mysql://localhost:3306/hotel");
        jdbcDao.setDbUser("hotel_user");
        jdbcDao.setDbPassword("R7hB2fK9sL6e");
        System.out.println(jdbcDao.getOne(1, Service.class));
        System.out.println("--------------------------------------");
    }

    public static void testGetOneClient() throws SQLException, NoSuchFieldException, InvocationTargetException,
            NoSuchMethodException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        JdbcDao jdbcDao = new JdbcDao();
        jdbcDao.setDbUrl("jdbc:mysql://localhost:3306/hotel");
        jdbcDao.setDbUser("hotel_user");
        jdbcDao.setDbPassword("R7hB2fK9sL6e");
        System.out.println(jdbcDao.getOne(1, Client.class));
        System.out.println("--------------------------------------");
    }

    public static void testGetOneRoomReservation() throws SQLException, NoSuchFieldException, InvocationTargetException,
            NoSuchMethodException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        JdbcDao jdbcDao = new JdbcDao();
        jdbcDao.setDbUrl("jdbc:mysql://localhost:3306/hotel");
        jdbcDao.setDbUser("hotel_user");
        jdbcDao.setDbPassword("R7hB2fK9sL6e");
        System.out.println(jdbcDao.getOne(1, RoomReservation.class));
        System.out.println("--------------------------------------");
    }

    public static void testGetOneProvidedService() throws SQLException, NoSuchFieldException, InvocationTargetException,
            NoSuchMethodException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        JdbcDao jdbcDao = new JdbcDao();
        jdbcDao.setDbUrl("jdbc:mysql://localhost:3306/hotel");
        jdbcDao.setDbUser("hotel_user");
        jdbcDao.setDbPassword("R7hB2fK9sL6e");
        System.out.println(jdbcDao.getOne(1, ProvidedService.class));
        System.out.println("--------------------------------------");
    }

    public static void testGetAllRooms() throws SQLException, NoSuchFieldException, InvocationTargetException,
            NoSuchMethodException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        JdbcDao jdbcDao = new JdbcDao();
        jdbcDao.setDbUrl("jdbc:mysql://localhost:3306/hotel");
        jdbcDao.setDbUser("hotel_user");
        jdbcDao.setDbPassword("R7hB2fK9sL6e");
        List<Room> rooms = jdbcDao.getAll(Room.class);
        for (Room room : rooms) {
            System.out.println(room);
        }
        System.out.println("--------------------------------------");
    }

    public static void testGetAllServices() throws SQLException, NoSuchFieldException, InvocationTargetException,
            NoSuchMethodException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        JdbcDao jdbcDao = new JdbcDao();
        jdbcDao.setDbUrl("jdbc:mysql://localhost:3306/hotel");
        jdbcDao.setDbUser("hotel_user");
        jdbcDao.setDbPassword("R7hB2fK9sL6e");
        List<Service> services = jdbcDao.getAll(Service.class);
        for (Service service : services) {
            System.out.println(service);
        }
        System.out.println("--------------------------------------");
    }

    public static void testGetAllClients() throws SQLException, NoSuchFieldException, InvocationTargetException,
            NoSuchMethodException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        JdbcDao jdbcDao = new JdbcDao();
        jdbcDao.setDbUrl("jdbc:mysql://localhost:3306/hotel");
        jdbcDao.setDbUser("hotel_user");
        jdbcDao.setDbPassword("R7hB2fK9sL6e");
        List<Client> clients = jdbcDao.getAll(Client.class);
        for (Client client : clients) {
            System.out.println(client);
        }
        System.out.println("--------------------------------------");
    }

    public static void testGetAllRoomReservations() throws SQLException, NoSuchFieldException,
            InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException,
            ClassNotFoundException {
        JdbcDao jdbcDao = new JdbcDao();
        jdbcDao.setDbUrl("jdbc:mysql://localhost:3306/hotel");
        jdbcDao.setDbUser("hotel_user");
        jdbcDao.setDbPassword("R7hB2fK9sL6e");
        List<RoomReservation> reservations = jdbcDao.getAll(RoomReservation.class);
        for (RoomReservation reservation : reservations) {
            System.out.println(reservation);
        }
        System.out.println("--------------------------------------");
    }

    public static void testGetAllProvidedServices() throws SQLException, NoSuchFieldException,
            InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException,
            ClassNotFoundException {
        JdbcDao jdbcDao = new JdbcDao();
        jdbcDao.setDbUrl("jdbc:mysql://localhost:3306/hotel");
        jdbcDao.setDbUser("hotel_user");
        jdbcDao.setDbPassword("R7hB2fK9sL6e");
        List<ProvidedService> providedServices = jdbcDao.getAll(ProvidedService.class);
        for (ProvidedService providedService : providedServices) {
            System.out.println(providedService);
        }
        System.out.println("--------------------------------------");
    }

    public static void testSaveRoom() throws SQLException, IllegalAccessException {
        JdbcDao jdbcDao = new JdbcDao();
        jdbcDao.setDbUrl("jdbc:mysql://localhost:3306/hotel");
        jdbcDao.setDbUser("hotel_user");
        jdbcDao.setDbPassword("R7hB2fK9sL6e");
        Room room = new Room(16, 7, 3, 5000);
        room.setStars(3);
        room.setStatus(RoomStatusTypes.REPAIR);
        room.setCheckInTime(LocalDateTime.now());
        room.setCheckOutTime(LocalDateTime.now());
        jdbcDao.save(room);
        System.out.println("Сохранить сущность " + room + " удалось");
        System.out.println("--------------------------------------");
    }

    public static void testSaveService() throws SQLException, IllegalAccessException {
        JdbcDao jdbcDao = new JdbcDao();
        jdbcDao.setDbUrl("jdbc:mysql://localhost:3306/hotel");
        jdbcDao.setDbUser("hotel_user");
        jdbcDao.setDbPassword("R7hB2fK9sL6e");
        Service service = new Service(6, ServiceNames.CONFERENCE, 20000);
        service.setStatus(ServiceStatusTypes.PAID);
        service.setServiceTime(LocalDateTime.now());
        jdbcDao.save(service);
        System.out.println("Сохранить сущность " + service + " удалось");
        System.out.println("--------------------------------------");
    }

    public static void testSaveClient() throws SQLException, IllegalAccessException {
        JdbcDao jdbcDao = new JdbcDao();
        jdbcDao.setDbUrl("jdbc:mysql://localhost:3306/hotel");
        jdbcDao.setDbUser("hotel_user");
        jdbcDao.setDbPassword("R7hB2fK9sL6e");
        Client client = new Client(6, "Antadze U. M.", "+7(953)170-82-19");
        client.setCheckInTime(LocalDateTime.now());
        client.setCheckOutTime(LocalDateTime.now());
        jdbcDao.save(client);
        System.out.println("Сохранить сущность " + client + " удалось");
        System.out.println("--------------------------------------");
    }

    public static void testSaveRoomReservation() throws SQLException, IllegalAccessException, NoSuchFieldException,
            ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException {
        JdbcDao jdbcDao = new JdbcDao();
        jdbcDao.setDbUrl("jdbc:mysql://localhost:3306/hotel");
        jdbcDao.setDbUser("hotel_user");
        jdbcDao.setDbPassword("R7hB2fK9sL6e");
        RoomReservation reservation = new RoomReservation(3, jdbcDao.getOne(16, Room.class),
                LocalDateTime.now(), LocalDateTime.now(),
                List.of(jdbcDao.getOne(6, Client.class)));
        jdbcDao.save(reservation);
        System.out.println("Сохранить сущность " + reservation + " удалось");
        System.out.println("--------------------------------------");
    }

    public static void testSaveProvidedService() throws SQLException, NoSuchFieldException, ClassNotFoundException,
            InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        JdbcDao jdbcDao = new JdbcDao();
        jdbcDao.setDbUrl("jdbc:mysql://localhost:3306/hotel");
        jdbcDao.setDbUser("hotel_user");
        jdbcDao.setDbPassword("R7hB2fK9sL6e");
        ProvidedService providedService = new ProvidedService(88, jdbcDao.getOne(6, Service.class),
                LocalDateTime.now(), jdbcDao.getOne(6, Client.class));
        jdbcDao.save(providedService);
        System.out.println("Сохранить сущность " + providedService + " удалось");
        System.out.println("--------------------------------------");
    }

    public static void testDeleteRoom() throws SQLException {
        JdbcDao jdbcDao = new JdbcDao();
        jdbcDao.setDbUrl("jdbc:mysql://localhost:3306/hotel");
        jdbcDao.setDbUser("hotel_user");
        jdbcDao.setDbPassword("R7hB2fK9sL6e");
        Room room = new Room(16, 7, 3, 5000);
        jdbcDao.delete(room);
        System.out.println("Удалить сущность " + room + " удалось");
        System.out.println("--------------------------------------");
    }

    public static void testDeleteService() throws SQLException {
        JdbcDao jdbcDao = new JdbcDao();
        jdbcDao.setDbUrl("jdbc:mysql://localhost:3306/hotel");
        jdbcDao.setDbUser("hotel_user");
        jdbcDao.setDbPassword("R7hB2fK9sL6e");
        Service service = new Service(6, ServiceNames.CONFERENCE, 20000);
        jdbcDao.delete(service);
        System.out.println("Удалить сущность " + service + " удалось");
        System.out.println("--------------------------------------");
    }

    public static void testDeleteClient() throws SQLException {
        JdbcDao jdbcDao = new JdbcDao();
        jdbcDao.setDbUrl("jdbc:mysql://localhost:3306/hotel");
        jdbcDao.setDbUser("hotel_user");
        jdbcDao.setDbPassword("R7hB2fK9sL6e");
        Client client = new Client(6, "Antadze U. M.", "+7(953)170-82-19");
        jdbcDao.delete(client);
        System.out.println("Удалить сущность " + client + " удалось");
        System.out.println("--------------------------------------");
    }

    public static void testDeleteRoomReservation() throws SQLException, NoSuchFieldException, ClassNotFoundException,
            InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        JdbcDao jdbcDao = new JdbcDao();
        jdbcDao.setDbUrl("jdbc:mysql://localhost:3306/hotel");
        jdbcDao.setDbUser("hotel_user");
        jdbcDao.setDbPassword("R7hB2fK9sL6e");
        RoomReservation reservation = jdbcDao.getOne(3, RoomReservation.class);
        jdbcDao.delete(reservation);
        System.out.println("Удалить сущность " + reservation + " удалось");
        System.out.println("--------------------------------------");
    }

    public static void testDeleteProvidedService() throws SQLException, NoSuchFieldException, ClassNotFoundException,
            InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        JdbcDao jdbcDao = new JdbcDao();
        jdbcDao.setDbUrl("jdbc:mysql://localhost:3306/hotel");
        jdbcDao.setDbUser("hotel_user");
        jdbcDao.setDbPassword("R7hB2fK9sL6e");
        ProvidedService providedService = jdbcDao.getOne(88, ProvidedService.class);
        jdbcDao.delete(providedService);
        System.out.println("Удалить сущность " + providedService + " удалось");
        System.out.println("--------------------------------------");
    }

    public static void testUpdateRoom() throws SQLException, IllegalAccessException, NoSuchFieldException,
            InvocationTargetException, NoSuchMethodException, InstantiationException, ClassNotFoundException {
        JdbcDao jdbcDao = new JdbcDao();
        jdbcDao.setDbUrl("jdbc:mysql://localhost:3306/hotel");
        jdbcDao.setDbUser("hotel_user");
        jdbcDao.setDbPassword("R7hB2fK9sL6e");
        Room room = new Room(16, 500, 30, 100000000);
        jdbcDao.update(room);
        System.out.println("Удалось обновить сущность. Новые данные по сущности: "
                + jdbcDao.getOne(room.getId(), Room.class));
        System.out.println("--------------------------------------");
    }

    public static void testUpdateService() throws SQLException, IllegalAccessException, NoSuchFieldException,
            InvocationTargetException, NoSuchMethodException, InstantiationException, ClassNotFoundException {
        JdbcDao jdbcDao = new JdbcDao();
        jdbcDao.setDbUrl("jdbc:mysql://localhost:3306/hotel");
        jdbcDao.setDbUser("hotel_user");
        jdbcDao.setDbPassword("R7hB2fK9sL6e");
        Service service = new Service(6, ServiceNames.CLEANING, 1000000000);
        jdbcDao.update(service);
        System.out.println("Удалось обновить сущность. Новые данные по сущности: "
                + jdbcDao.getOne(service.getId(), Service.class));
        System.out.println("--------------------------------------");
    }

    public static void testUpdateClient() throws SQLException, IllegalAccessException, NoSuchFieldException,
            InvocationTargetException, NoSuchMethodException, InstantiationException, ClassNotFoundException {
        JdbcDao jdbcDao = new JdbcDao();
        jdbcDao.setDbUrl("jdbc:mysql://localhost:3306/hotel");
        jdbcDao.setDbUser("hotel_user");
        jdbcDao.setDbPassword("R7hB2fK9sL6e");
        Client client = new Client(6, "Antadze U. M.", "+7(999)888-77-66");
        jdbcDao.update(client);
        System.out.println("Удалось обновить сущность. Новые данные по сущности: "
                + jdbcDao.getOne(client.getId(), Client.class));
        System.out.println("--------------------------------------");
    }

    public static void testUpdateRoomReservation() throws SQLException, NoSuchFieldException, ClassNotFoundException,
            InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        JdbcDao jdbcDao = new JdbcDao();
        jdbcDao.setDbUrl("jdbc:mysql://localhost:3306/hotel");
        jdbcDao.setDbUser("hotel_user");
        jdbcDao.setDbPassword("R7hB2fK9sL6e");
        RoomReservation reservation = new RoomReservation(3, jdbcDao.getOne(16, Room.class),
                LocalDateTime.of(2024, 1, 1, 12, 0, 0),
                LocalDateTime.of(2024, 1, 2, 10, 0, 0),
                List.of(jdbcDao.getOne(1, Client.class), jdbcDao.getOne(6, Client.class)));
        jdbcDao.update(reservation);
        System.out.println("Удалось обновить сущность. Новые данные по сущности: "
                + jdbcDao.getOne(reservation.getId(), RoomReservation.class));
        System.out.println("--------------------------------------");
    }

    public static void testUpdateProvidedService() throws SQLException, NoSuchFieldException, ClassNotFoundException,
            InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        JdbcDao jdbcDao = new JdbcDao();
        jdbcDao.setDbUrl("jdbc:mysql://localhost:3306/hotel");
        jdbcDao.setDbUser("hotel_user");
        jdbcDao.setDbPassword("R7hB2fK9sL6e");
        ProvidedService providedService = jdbcDao.getOne(88, ProvidedService.class);
        providedService.setBeneficiaries(List.of(jdbcDao.getOne(1, Client.class)));
        providedService.setService(jdbcDao.getOne(1, Service.class));
        jdbcDao.update(providedService);
        System.out.println("Удалось обновить сущность. Новые данные по сущности: "
                + jdbcDao.getOne(providedService.getId(), ProvidedService.class));
        System.out.println("--------------------------------------");
    }
}
