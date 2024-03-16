import dao.DAO;
import essence.person.Client;
import essence.room.Room;
import lombok.SneakyThrows;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

public class TestDAO {
    @SneakyThrows
    public static void main(String[] args) {
        testGetOne();
        testGetAll();
//        testSave();
    }

    public static void testGetOne() throws SQLException, NoSuchFieldException, InvocationTargetException,
            NoSuchMethodException, InstantiationException, IllegalAccessException {
        DAO dao = new DAO();
        dao.setDbUrl("jdbc:mysql://localhost:3306/hotel");
        dao.setDbUser("hotel_user");
        dao.setDbPassword("R7hB2fK9sL6e");
        System.out.println(dao.getOne(1, Client.class));
        System.out.println("--------------------------------------");
    }

    public static void testGetAll() throws SQLException, NoSuchFieldException, InvocationTargetException,
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

    public static void testSave() throws SQLException, IllegalAccessException {
        DAO dao = new DAO();
        dao.setDbUrl("jdbc:mysql://localhost:3306/hotel");
        dao.setDbUser("hotel_user");
        dao.setDbPassword("R7hB2fK9sL6e");
        Room room = new Room(16, 7, 3, 5000);
        dao.save(room);
        System.out.println("Сохранить сущность " + room + " удалось");
        System.out.println("--------------------------------------");
    }
}
