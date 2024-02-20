import essence.person.AbstractClient;
import essence.person.Client;
import essence.room.AbstractRoom;
import essence.room.Room;
import essence.service.AbstractService;
import essence.service.Service;
import essence.service.ServiceNames;
import repository.client.ClientRepository;
import repository.room.RoomRepository;
import repository.room.RoomReservationRepository;
import repository.service.ProvidedServicesRepository;
import repository.service.ServiceRepository;
import service.ClientService;
import service.RoomService;
import service.ServiceService;
import utils.file.DataPath;
import utils.file.serialize.SerializationUtils;

import java.io.IOException;
import java.util.List;

public class TestSerializeRepo {
    public static void main(String[] args) {
        try {
            AbstractRoom room1 = new Room(1, 100, 2, 3000);
            AbstractRoom room2 = new Room(2, 101, 3, 5000);
            AbstractRoom room3 = new Room(3, 102, 5, 10000);

            AbstractService service1 = new Service(1, ServiceNames.CONFERENCE, 15000);
            AbstractService service2 = new Service(2, ServiceNames.FITNESS, 3000);
            AbstractService service3 = new Service(3, ServiceNames.EXCURSION, 50000);

            AbstractClient client1 = new Client(1, "Osipov D.R.", "+7(902)902-98-11");
            AbstractClient client2 = new Client(2, "Musofranova N.S.", "+7(953)180-00-61");
            AbstractClient client3 = new Client(3, "Soshin V.S.", "+7(901)901-91-11");

            RoomService rs = new RoomService(RoomRepository.getInstance(), RoomReservationRepository.getInstance());
            ServiceService ss = new ServiceService(ServiceRepository.getInstance(), ProvidedServicesRepository.getInstance());
            ClientService cs = new ClientService(ClientRepository.getInstance());

            rs.addRoom(room1);
            rs.addRoom(room2);
            rs.addRoom(room3);
            ss.addService(service1);
            ss.addService(service2);
            ss.addService(service3);
            cs.addClient(client1);
            cs.addClient(client2);
            cs.addClient(client3);

            rs.checkIn(room1, client1, client2);
            rs.evict(room1, client1, client2);
            rs.addStarsToRoom(room1, 5);
            ss.provideService(client1, service1);
            ss.provideService(client3, service3);
            rs.checkIn(room2, client3);
            rs.checkIn(room1, client1);
            ss.provideService(client2, service2);

            SerializationUtils.serialize(rs.roomsByStars(),
                    DataPath.SERIALIZE_DIRECTORY.getPath() + "roomTest.json");
            List roomTest = SerializationUtils.deserialize(List.class,
                    DataPath.SERIALIZE_DIRECTORY.getPath() + "roomTest.json");
            System.out.println(roomTest);

            SerializationUtils.serialize(rs.getReservations(),
                    DataPath.SERIALIZE_DIRECTORY.getPath() + "reservationTest.json");
            List reservationTest = SerializationUtils.deserialize(List.class,
                    DataPath.SERIALIZE_DIRECTORY.getPath() + "reservationTest.json");
            System.out.println(reservationTest);

            SerializationUtils.serialize(ss.getServices(),
                    DataPath.SERIALIZE_DIRECTORY.getPath() + "serviceTest.json");
            List serviceTest = SerializationUtils.deserialize(List.class,
                    DataPath.SERIALIZE_DIRECTORY.getPath() + "serviceTest.json");
            System.out.println(serviceTest);

            SerializationUtils.serialize(ss.getProvidedServices(),
                    DataPath.SERIALIZE_DIRECTORY.getPath() + "providedServiceTest.json");
            List providedServicesTest = SerializationUtils.deserialize(List.class,
                    DataPath.SERIALIZE_DIRECTORY.getPath() + "providedServiceTest.json");
            System.out.println(providedServicesTest);

            SerializationUtils.serialize(cs.getClients(),
                    DataPath.SERIALIZE_DIRECTORY.getPath() + "clientTest.json");
            List clientTest = SerializationUtils.deserialize(List.class,
                    DataPath.SERIALIZE_DIRECTORY.getPath() + "clientTest.json");
            System.out.println(clientTest);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
