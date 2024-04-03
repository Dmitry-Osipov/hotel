import dao.HiberDao;
import essence.person.Client;
import essence.provided.ProvidedService;
import essence.reservation.RoomReservation;
import essence.room.Room;
import essence.service.Service;
import essence.service.ServiceNames;

import java.time.LocalDateTime;
import java.util.List;

public class TestDao {
    public static void main(String[] args) {
        getOne();
        System.out.println("-----------------------------------------------------------------------------------------");
        getAll();
        System.out.println("-----------------------------------------------------------------------------------------");
        saveRoom();
        saveService();
        saveClient();
        saveRoomReservation();
        saveProvidedService();
        System.out.println("-----------------------------------------------------------------------------------------");
        updateRoom();
        updateService();
        updateClient();
        updateRoomReservation();
        updateProvidedService();
        System.out.println("-----------------------------------------------------------------------------------------");
        deleteProvidedService();
        deleteRoomReservation();
        deleteRoom();
        deleteClient();
        deleteService();
        System.out.println("-----------------------------------------------------------------------------------------");
    }

    public static void getOne() {
        HiberDao dao = new HiberDao();
        System.out.println(dao.getOne(1, Client.class));
        System.out.println();
        System.out.println(dao.getOne(1, Service.class));
        System.out.println();
        System.out.println(dao.getOne(1, Room.class));
        System.out.println();
        System.out.println(dao.getOne(1, RoomReservation.class));
        System.out.println();
        System.out.println(dao.getOne(1, ProvidedService.class));
        System.out.println();
    }

    public static void getAll() {
        HiberDao dao = new HiberDao();
        dao.getAll(Client.class).forEach(System.out::println);
        System.out.println();
        dao.getAll(Service.class).forEach(System.out::println);
        System.out.println();
        dao.getAll(Room.class).forEach(System.out::println);
        System.out.println();
        dao.getAll(RoomReservation.class).forEach(System.out::println);
        System.out.println();
        dao.getAll(ProvidedService.class).forEach(System.out::println);
        System.out.println();
    }

    public static void saveRoom() {
        HiberDao dao = new HiberDao();
        Room room = new Room();
        room.setNumber(8);
        room.setCapacity(8);
        room.setPrice(80000);
        dao.save(room);
    }

    public static void saveService() {
        HiberDao dao = new HiberDao();
        Service service = new Service();
        service.setName(ServiceNames.FITNESS);
        service.setPrice(60000);
        dao.save(service);
    }

    public static void saveClient() {
        HiberDao dao = new HiberDao();
        Client client = new Client();
        client.setFio("Fomina L. A.");
        client.setPhoneNumber("+7(777)777-77-77");
        dao.save(client);
    }

    public static void saveRoomReservation() {
        HiberDao dao = new HiberDao();
        RoomReservation roomReservation = new RoomReservation();
        roomReservation.setRoom(dao.getOne(10, Room.class));
        roomReservation.setCheckInTime(LocalDateTime.now());
        roomReservation.setCheckOutTime(LocalDateTime.now());
        roomReservation.setClients(
                List.of(dao.getOne(8, Client.class), dao.getOne(1, Client.class)));
        dao.save(roomReservation);
    }

    public static void saveProvidedService() {
        HiberDao dao = new HiberDao();
        ProvidedService providedService = new ProvidedService();
        providedService.setClient(dao.getOne(8, Client.class));
        providedService.setServiceTime(LocalDateTime.now());
        providedService.setService(dao.getOne(8, Service.class));
        dao.save(providedService);
    }

    public static void updateRoom() {
        HiberDao dao = new HiberDao();
        dao.update(new Room(10, 10, 10, 100000));
    }

    public static void updateService() {
        HiberDao dao = new HiberDao();
        dao.update(new Service(8, ServiceNames.LAUNDRY, 80000));
    }

    public static void updateClient() {
        HiberDao dao = new HiberDao();
        dao.update(new Client(8, "Talix O. G.", "+7(111)111-11-11"));
    }

    public static void updateRoomReservation() {
        HiberDao dao = new HiberDao();
        dao.update(new RoomReservation(7, dao.getOne(10, Room.class),
        LocalDateTime.now(), LocalDateTime.now(),
        List.of(dao.getOne(8, Client.class))));
    }

    public static void updateProvidedService() {
        HiberDao dao = new HiberDao();
        dao.update(new ProvidedService(11, dao.getOne(8, Service.class), LocalDateTime.now(),
                dao.getOne(8, Client.class)));
    }

    public static void deleteClient() {
        HiberDao dao = new HiberDao();
        dao.delete(new Client(8, "Talix O. G.", "+7(111)111-11-11"));
    }

    public static void deleteRoom() {
        HiberDao dao = new HiberDao();
        dao.delete(new Room(10, 10, 10, 100000));
    }

    public static void deleteService() {
        HiberDao dao = new HiberDao();
        dao.delete(new Service(8, ServiceNames.LAUNDRY, 80000));
    }

    public static void deleteRoomReservation() {
        HiberDao dao = new HiberDao();
        dao.delete(new RoomReservation(7, dao.getOne(10, Room.class), LocalDateTime.now(), LocalDateTime.now(),
                List.of(dao.getOne(8, Client.class))));
    }

    public static void deleteProvidedService() {
        HiberDao dao = new HiberDao();
        dao.delete(new ProvidedService(11, dao.getOne(8, Service.class), LocalDateTime.now(),
                dao.getOne(8, Client.class)));
    }
}
