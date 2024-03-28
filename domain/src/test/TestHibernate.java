import essence.person.AbstractClient;
import essence.person.Client;
import essence.provided.ProvidedService;
import essence.reservation.RoomReservation;
import essence.room.AbstractRoom;
import essence.room.Room;
import essence.service.AbstractService;
import essence.service.Service;
import essence.service.ServiceNames;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.time.LocalDateTime;
import java.util.List;

public class TestHibernate {
    public static void main(String[] args) {
        testGetOne();
        System.out.println("-----------------------------------------------------------------------------------------");
        testGetAll();
        System.out.println("-----------------------------------------------------------------------------------------");
        testSaveRoom();
        testSaveClient();
        testSaveService();
        testSaveRoomReservation();
        testSaveProvidedService();
        System.out.println("-----------------------------------------------------------------------------------------");
        testUpdateRoom();
        System.out.println();
        testUpdateService();
        System.out.println();
        testUpdateClient();
        System.out.println();
        testUpdateProvidedService();
        System.out.println();
        testUpdateRoomReservation();
        System.out.println();
        System.out.println("-----------------------------------------------------------------------------------------");
        testDeleteRoomReservation();
        System.out.println();
        testDeleteProvidedService();
        System.out.println();
        testDeleteRoom();
        System.out.println();
        testDeleteService();
        System.out.println();
        testDeleteClient();
    }

    public static void testGetOne() {
        try (SessionFactory factory = new Configuration()
                .configure()
                .addAnnotatedClass(Room.class)
                .addAnnotatedClass(Service.class)
                .addAnnotatedClass(Client.class)
                .addAnnotatedClass(ProvidedService.class)
                .addAnnotatedClass(RoomReservation.class)
                .buildSessionFactory();
             Session session = factory.getCurrentSession()) {
            session.beginTransaction();
            Room room = session.get(Room.class, 5);
            System.out.println(room);

            Service service = session.get(Service.class, 5);
            System.out.println(service);

            Client client = session.get(Client.class, 2);
            System.out.println(client);

            ProvidedService providedService = session.get(ProvidedService.class, 7);
            System.out.println(providedService);

            RoomReservation roomReservation = session.get(RoomReservation.class, 1);
            System.out.println(roomReservation);

            session.getTransaction().commit();
            System.out.println("Done!");
        }
    }

    public static void testGetAll() {
        try (SessionFactory factory = new Configuration()
                .configure()
                .addAnnotatedClass(Room.class)
                .addAnnotatedClass(Service.class)
                .addAnnotatedClass(Client.class)
                .addAnnotatedClass(ProvidedService.class)
                .addAnnotatedClass(RoomReservation.class)
                .buildSessionFactory();
             Session session = factory.getCurrentSession()) {
            session.beginTransaction();

            List<AbstractRoom> rooms = session.createQuery("from Room", AbstractRoom.class).getResultList();
            rooms.forEach(System.out::println);
            System.out.println();

            List<AbstractClient> clients = session.createQuery("from Client", AbstractClient.class).getResultList();
            clients.forEach(System.out::println);
            System.out.println();

            List<AbstractService> services = session.createQuery("from Service", AbstractService.class)
                    .getResultList();
            services.forEach(System.out::println);
            System.out.println();

            List<ProvidedService> providedServices = session.createQuery("from ProvidedService",
                    ProvidedService.class).getResultList();
            providedServices.forEach(System.out::println);
            System.out.println();

            List<RoomReservation> roomReservations = session.createQuery("from RoomReservation",
                    RoomReservation.class).getResultList();
            roomReservations.forEach(System.out::println);
            System.out.println();

            session.getTransaction().commit();
            System.out.println("Done!");
        }
    }

    public static void testSaveRoom() {
        try (SessionFactory factory = new Configuration()
                .configure()
                .addAnnotatedClass(Room.class)
                .addAnnotatedClass(Service.class)
                .addAnnotatedClass(Client.class)
                .addAnnotatedClass(ProvidedService.class)
                .addAnnotatedClass(RoomReservation.class)
                .buildSessionFactory();
             Session session = factory.getCurrentSession()) {
            session.beginTransaction();

            // Требуется не устанавливать id для сущности для избежания ошибки:
            Room room = new Room();
            room.setNumber(8);
            room.setCapacity(8);
            room.setPrice(80000);
            session.persist(room);

            session.getTransaction().commit();
            System.out.println("Done!");
        }
    }

    public static void testSaveService() {
        try (SessionFactory factory = new Configuration()
                .configure()
                .addAnnotatedClass(Room.class)
                .addAnnotatedClass(Service.class)
                .addAnnotatedClass(Client.class)
                .addAnnotatedClass(ProvidedService.class)
                .addAnnotatedClass(RoomReservation.class)
                .buildSessionFactory();
             Session session = factory.getCurrentSession()) {
            session.beginTransaction();

            Service service = new Service();
            service.setName(ServiceNames.FITNESS);
            service.setPrice(60000);
            session.persist(service);

            session.getTransaction().commit();
            System.out.println("Done!");
        }
    }

    public static void testSaveClient() {
        try (SessionFactory factory = new Configuration()
                .configure()
                .addAnnotatedClass(Room.class)
                .addAnnotatedClass(Service.class)
                .addAnnotatedClass(Client.class)
                .addAnnotatedClass(ProvidedService.class)
                .addAnnotatedClass(RoomReservation.class)
                .buildSessionFactory();
             Session session = factory.getCurrentSession()) {
            session.beginTransaction();

            Client client = new Client();
            client.setFio("Fomina L. A.");
            client.setPhoneNumber("+7(777)777-77-77");
            session.persist(client);

            session.getTransaction().commit();
            System.out.println("Done!");
        }
    }

    public static void testSaveRoomReservation() {
        try (SessionFactory factory = new Configuration()
                .configure()
                .addAnnotatedClass(Room.class)
                .addAnnotatedClass(Service.class)
                .addAnnotatedClass(Client.class)
                .addAnnotatedClass(ProvidedService.class)
                .addAnnotatedClass(RoomReservation.class)
                .buildSessionFactory();
             Session session = factory.getCurrentSession()) {
            session.beginTransaction();

            RoomReservation roomReservation = new RoomReservation();
            roomReservation.setRoom(session.get(Room.class, 8));
            roomReservation.setCheckInTime(LocalDateTime.now());
            roomReservation.setCheckOutTime(LocalDateTime.now());
            roomReservation.setClients(List.of(session.get(Client.class, 6), session.get(Client.class, 1)));
            session.persist(roomReservation);

            session.getTransaction().commit();
            System.out.println("Done!");
        }
    }

    public static void testSaveProvidedService() {
        try (SessionFactory factory = new Configuration()
                .configure()
                .addAnnotatedClass(Room.class)
                .addAnnotatedClass(Service.class)
                .addAnnotatedClass(Client.class)
                .addAnnotatedClass(ProvidedService.class)
                .addAnnotatedClass(RoomReservation.class)
                .buildSessionFactory();
             Session session = factory.getCurrentSession()) {
            session.beginTransaction();

            ProvidedService providedService = new ProvidedService();
            providedService.setClient(session.get(Client.class, 6));
            providedService.setServiceTime(LocalDateTime.now());
            providedService.setService(session.get(Service.class, 6));
            session.persist(providedService);

            session.getTransaction().commit();
            System.out.println("Done!");
        }
    }

    public static void testUpdateRoom() {
        try (SessionFactory factory = new Configuration()
                .configure()
                .addAnnotatedClass(Room.class)
                .addAnnotatedClass(Service.class)
                .addAnnotatedClass(Client.class)
                .addAnnotatedClass(ProvidedService.class)
                .addAnnotatedClass(RoomReservation.class)
                .buildSessionFactory();
             Session session = factory.getCurrentSession()) {
            session.beginTransaction();

            // Есть 2 варианта обновлеия: через установку нового значения поля полученного объекта или через HQL:
            AbstractRoom room = session.get(Room.class, 8);
            room.setCheckInTime(LocalDateTime.now());
            // session.createQuery("update Room set stars=3 where id=8").executeUpdate();  // Обновили с помощью HQL.

            session.getTransaction().commit();
            System.out.println("Done!");
        }
    }

    public static void testUpdateService() {
        try (SessionFactory factory = new Configuration()
                .configure()
                .addAnnotatedClass(Room.class)
                .addAnnotatedClass(Service.class)
                .addAnnotatedClass(Client.class)
                .addAnnotatedClass(ProvidedService.class)
                .addAnnotatedClass(RoomReservation.class)
                .buildSessionFactory();
             Session session = factory.getCurrentSession()) {
            session.beginTransaction();

            Service service = session.get(Service.class, 6);
            service.setServiceTime(LocalDateTime.now());

            session.getTransaction().commit();
            System.out.println("Done!");
        }
    }

    public static void testUpdateClient() {
        try (SessionFactory factory = new Configuration()
                .configure()
                .addAnnotatedClass(Room.class)
                .addAnnotatedClass(Service.class)
                .addAnnotatedClass(Client.class)
                .addAnnotatedClass(ProvidedService.class)
                .addAnnotatedClass(RoomReservation.class)
                .buildSessionFactory();
             Session session = factory.getCurrentSession()) {
            session.beginTransaction();

            Client service = session.get(Client.class, 6);
            service.setCheckInTime(LocalDateTime.now());

            session.getTransaction().commit();
            System.out.println("Done!");
        }
    }

    public static void testUpdateProvidedService() {
        try (SessionFactory factory = new Configuration()
                .configure()
                .addAnnotatedClass(Room.class)
                .addAnnotatedClass(Service.class)
                .addAnnotatedClass(Client.class)
                .addAnnotatedClass(ProvidedService.class)
                .addAnnotatedClass(RoomReservation.class)
                .buildSessionFactory();
             Session session = factory.getCurrentSession()) {
            session.beginTransaction();

            ProvidedService service = session.get(ProvidedService.class, 9);
            service.setServiceTime(LocalDateTime.now());

            session.getTransaction().commit();
            System.out.println("Done!");
        }
    }

    public static void testUpdateRoomReservation() {
        try (SessionFactory factory = new Configuration()
                .configure()
                .addAnnotatedClass(Room.class)
                .addAnnotatedClass(Service.class)
                .addAnnotatedClass(Client.class)
                .addAnnotatedClass(ProvidedService.class)
                .addAnnotatedClass(RoomReservation.class)
                .buildSessionFactory();
             Session session = factory.getCurrentSession()) {
            session.beginTransaction();

            RoomReservation reservation = session.get(RoomReservation.class, 5);
            reservation.setClients(List.of(session.get(Client.class, 6)));

            session.getTransaction().commit();
            System.out.println("Done!");
        }
    }

    public static void testDeleteRoom() {
        try (SessionFactory factory = new Configuration()
                .configure()
                .addAnnotatedClass(Room.class)
                .addAnnotatedClass(Service.class)
                .addAnnotatedClass(Client.class)
                .addAnnotatedClass(ProvidedService.class)
                .addAnnotatedClass(RoomReservation.class)
                .buildSessionFactory();
             Session session = factory.getCurrentSession()) {
            session.beginTransaction();

            // Есть 2 варианта удаления: через специальный метод или через HQL:
            session.remove(session.get(Room.class, 8));
            // session.createQuery("delete Room where id=8").executeUpdate();  // Или с
            // помощью HQL

            session.getTransaction().commit();
            System.out.println("Done!");
        }
    }

    public static void testDeleteService() {
        try (SessionFactory factory = new Configuration()
                .configure()
                .addAnnotatedClass(Room.class)
                .addAnnotatedClass(Service.class)
                .addAnnotatedClass(Client.class)
                .addAnnotatedClass(ProvidedService.class)
                .addAnnotatedClass(RoomReservation.class)
                .buildSessionFactory();
             Session session = factory.getCurrentSession()) {
            session.beginTransaction();

            session.remove(session.get(Service.class, 6));

            session.getTransaction().commit();
            System.out.println("Done!");
        }
    }

    public static void testDeleteClient() {
        try (SessionFactory factory = new Configuration()
                .configure()
                .addAnnotatedClass(Room.class)
                .addAnnotatedClass(Service.class)
                .addAnnotatedClass(Client.class)
                .addAnnotatedClass(ProvidedService.class)
                .addAnnotatedClass(RoomReservation.class)
                .buildSessionFactory();
             Session session = factory.getCurrentSession()) {
            session.beginTransaction();

            session.remove(session.get(Client.class, 6));

            session.getTransaction().commit();
            System.out.println("Done!");
        }
    }

    public static void testDeleteProvidedService() {
        try (SessionFactory factory = new Configuration()
                .configure()
                .addAnnotatedClass(Room.class)
                .addAnnotatedClass(Service.class)
                .addAnnotatedClass(Client.class)
                .addAnnotatedClass(ProvidedService.class)
                .addAnnotatedClass(RoomReservation.class)
                .buildSessionFactory();
             Session session = factory.getCurrentSession()) {
            session.beginTransaction();

            session.remove(session.get(ProvidedService.class, 9));

            session.getTransaction().commit();
            System.out.println("Done!");
        }
    }

    public static void testDeleteRoomReservation() {
        try (SessionFactory factory = new Configuration()
                .configure()
                .addAnnotatedClass(Room.class)
                .addAnnotatedClass(Service.class)
                .addAnnotatedClass(Client.class)
                .addAnnotatedClass(ProvidedService.class)
                .addAnnotatedClass(RoomReservation.class)
                .buildSessionFactory();
             Session session = factory.getCurrentSession()) {
            session.beginTransaction();

            session.remove(session.get(RoomReservation.class, 5));

            session.getTransaction().commit();
            System.out.println("Done!");
        }
    }
}
