import dao.DAO;
import essence.person.Client;
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
//        try {
//            testGetOneRoom();
//            testGetOneService();
//            testGetOneClient();
//            testGetAllRooms();
//            testGetAllServices();
//            testGetAllClients();
//            testSaveRoom();
//            testSaveService();
//            testSaveClient();
//            testUpdateRoom();
//            testUpdateService();
//            testUpdateClient();
//        } finally {
//            testDeleteRoom();
//            testDeleteService();
//            testDeleteClient();
//        }
        testGetOneRoomReservation();
    }

    public static void testGetOneRoom() throws SQLException, NoSuchFieldException, InvocationTargetException,
            NoSuchMethodException, InstantiationException, IllegalAccessException {
        DAO dao = new DAO();
        dao.setDbUrl("jdbc:mysql://localhost:3306/hotel");
        dao.setDbUser("hotel_user");
        dao.setDbPassword("R7hB2fK9sL6e");
        System.out.println(dao.getOne(1, Room.class));
        System.out.println("--------------------------------------");
    }

    public static void testGetOneService() throws SQLException, NoSuchFieldException, InvocationTargetException,
            NoSuchMethodException, InstantiationException, IllegalAccessException {
        DAO dao = new DAO();
        dao.setDbUrl("jdbc:mysql://localhost:3306/hotel");
        dao.setDbUser("hotel_user");
        dao.setDbPassword("R7hB2fK9sL6e");
        System.out.println(dao.getOne(1, Service.class));
        System.out.println("--------------------------------------");
    }

    public static void testGetOneClient() throws SQLException, NoSuchFieldException, InvocationTargetException,
            NoSuchMethodException, InstantiationException, IllegalAccessException {
        DAO dao = new DAO();
        dao.setDbUrl("jdbc:mysql://localhost:3306/hotel");
        dao.setDbUser("hotel_user");
        dao.setDbPassword("R7hB2fK9sL6e");
        System.out.println(dao.getOne(1, Client.class));
        System.out.println("--------------------------------------");
    }

    public static void testGetAllRooms() throws SQLException, NoSuchFieldException, InvocationTargetException,
            NoSuchMethodException, InstantiationException, IllegalAccessException {
        DAO dao = new DAO();
        dao.setDbUrl("jdbc:mysql://localhost:3306/hotel");
        dao.setDbUser("hotel_user");
        dao.setDbPassword("R7hB2fK9sL6e");
        List<Room> rooms = dao.getAll(Room.class);
        for (Room room : rooms) {
            System.out.println(room);
        }
        System.out.println("--------------------------------------");
    }

    public static void testGetAllServices() throws SQLException, NoSuchFieldException, InvocationTargetException,
            NoSuchMethodException, InstantiationException, IllegalAccessException {
        DAO dao = new DAO();
        dao.setDbUrl("jdbc:mysql://localhost:3306/hotel");
        dao.setDbUser("hotel_user");
        dao.setDbPassword("R7hB2fK9sL6e");
        List<Service> services = dao.getAll(Service.class);
        for (Service service : services) {
            System.out.println(service);
        }
        System.out.println("--------------------------------------");
    }

    public static void testGetAllClients() throws SQLException, NoSuchFieldException, InvocationTargetException,
            NoSuchMethodException, InstantiationException, IllegalAccessException {
        DAO dao = new DAO();
        dao.setDbUrl("jdbc:mysql://localhost:3306/hotel");
        dao.setDbUser("hotel_user");
        dao.setDbPassword("R7hB2fK9sL6e");
        List<Client> clients = dao.getAll(Client.class);
        for (Client client : clients) {
            System.out.println(client);
        }
        System.out.println("--------------------------------------");
    }

    public static void testSaveRoom() throws SQLException, IllegalAccessException {
        DAO dao = new DAO();
        dao.setDbUrl("jdbc:mysql://localhost:3306/hotel");
        dao.setDbUser("hotel_user");
        dao.setDbPassword("R7hB2fK9sL6e");
        Room room = new Room(16, 7, 3, 5000);
        room.setStars(3);
        room.setStatus(RoomStatusTypes.REPAIR);
        room.setCheckInTime(LocalDateTime.now());
        room.setCheckOutTime(LocalDateTime.now());
        dao.save(room);
        System.out.println("Сохранить сущность " + room + " удалось");
        System.out.println("--------------------------------------");
    }

    public static void testSaveService() throws SQLException, IllegalAccessException {
        DAO dao = new DAO();
        dao.setDbUrl("jdbc:mysql://localhost:3306/hotel");
        dao.setDbUser("hotel_user");
        dao.setDbPassword("R7hB2fK9sL6e");
        Service service = new Service(6, ServiceNames.CONFERENCE, 20000);
        service.setStatus(ServiceStatusTypes.PAID);
        service.setServiceTime(LocalDateTime.now());
        dao.save(service);
        System.out.println("Сохранить сущность " + service + " удалось");
        System.out.println("--------------------------------------");
    }

    public static void testSaveClient() throws SQLException, IllegalAccessException {
        DAO dao = new DAO();
        dao.setDbUrl("jdbc:mysql://localhost:3306/hotel");
        dao.setDbUser("hotel_user");
        dao.setDbPassword("R7hB2fK9sL6e");
        Client client = new Client(6, "Antadze U. M.", "+7(953)170-82-19");
        client.setCheckInTime(LocalDateTime.now());
        client.setCheckOutTime(LocalDateTime.now());
        dao.save(client);
        System.out.println("Сохранить сущность " + client + " удалось");
        System.out.println("--------------------------------------");
    }

    public static void testDeleteRoom() throws SQLException {
        DAO dao = new DAO();
        dao.setDbUrl("jdbc:mysql://localhost:3306/hotel");
        dao.setDbUser("hotel_user");
        dao.setDbPassword("R7hB2fK9sL6e");
        Room room = new Room(16, 7, 3, 5000);
        dao.delete(room);
        System.out.println("Удалить сущность " + room + " удалось");
        System.out.println("--------------------------------------");
    }

    public static void testDeleteService() throws SQLException {
        DAO dao = new DAO();
        dao.setDbUrl("jdbc:mysql://localhost:3306/hotel");
        dao.setDbUser("hotel_user");
        dao.setDbPassword("R7hB2fK9sL6e");
        Service service = new Service(6, ServiceNames.CONFERENCE, 20000);
        dao.delete(service);
        System.out.println("Удалить сущность " + service + " удалось");
        System.out.println("--------------------------------------");
    }

    public static void testDeleteClient() throws SQLException {
        DAO dao = new DAO();
        dao.setDbUrl("jdbc:mysql://localhost:3306/hotel");
        dao.setDbUser("hotel_user");
        dao.setDbPassword("R7hB2fK9sL6e");
        Client client = new Client(6, "Antadze U. M.", "+7(953)170-82-19");
        dao.delete(client);
        System.out.println("Удалить сущность " + client + " удалось");
        System.out.println("--------------------------------------");
    }

    public static void testUpdateRoom() throws SQLException, IllegalAccessException, NoSuchFieldException,
            InvocationTargetException, NoSuchMethodException, InstantiationException {
        DAO dao = new DAO();
        dao.setDbUrl("jdbc:mysql://localhost:3306/hotel");
        dao.setDbUser("hotel_user");
        dao.setDbPassword("R7hB2fK9sL6e");
        Room room = new Room(16, 500, 30, 100000000);
        dao.update(room);
        System.out.println("Удалось обновить сущность. Новые данные по сущности: "
                + dao.getOne(room.getId(), Room.class));
        System.out.println("--------------------------------------");
    }

    public static void testUpdateService() throws SQLException, IllegalAccessException, NoSuchFieldException,
            InvocationTargetException, NoSuchMethodException, InstantiationException {
        DAO dao = new DAO();
        dao.setDbUrl("jdbc:mysql://localhost:3306/hotel");
        dao.setDbUser("hotel_user");
        dao.setDbPassword("R7hB2fK9sL6e");
        Service service = new Service(6, ServiceNames.CLEANING, 1000000000);
        dao.update(service);
        System.out.println("Удалось обновить сущность. Новые данные по сущности: "
                + dao.getOne(service.getId(), Service.class));
        System.out.println("--------------------------------------");
    }

    public static void testUpdateClient() throws SQLException, IllegalAccessException, NoSuchFieldException,
            InvocationTargetException, NoSuchMethodException, InstantiationException {
        DAO dao = new DAO();
        dao.setDbUrl("jdbc:mysql://localhost:3306/hotel");
        dao.setDbUser("hotel_user");
        dao.setDbPassword("R7hB2fK9sL6e");
        Client client = new Client(6, "Antadze U. M.", "+7(999)888-77-66");
        dao.update(client);
        System.out.println("Удалось обновить сущность. Новые данные по сущности: "
                + dao.getOne(client.getId(), Client.class));
        System.out.println("--------------------------------------");
    }

    public static void testGetOneRoomReservation() throws SQLException, NoSuchFieldException, InvocationTargetException,
            NoSuchMethodException, InstantiationException, IllegalAccessException {
        DAO dao = new DAO();
        dao.setDbUrl("jdbc:mysql://localhost:3306/hotel");
        dao.setDbUser("hotel_user");
        dao.setDbPassword("R7hB2fK9sL6e");
        System.out.println(dao.getOne(1, RoomReservation.class));
        System.out.println("--------------------------------------");
    }
}
