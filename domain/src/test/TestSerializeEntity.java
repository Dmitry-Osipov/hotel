import essence.person.Client;
import essence.provided.ProvidedService;
import essence.reservation.RoomReservation;
import essence.room.Room;
import essence.room.RoomStatusTypes;
import essence.service.Service;
import essence.service.ServiceNames;
import essence.service.ServiceStatusTypes;
import utils.file.DataPath;
import utils.file.serialize.SerializationUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class TestSerializeEntity {
    public static void main(String[] args) {
        try {
            Room room = new Room(1, 100, 2, 3000);
            room.setStatus(RoomStatusTypes.REPAIR);
            room.setStars(4);
            room.setCheckInTime(LocalDateTime.now());
            room.setCheckOutTime(LocalDateTime.now());

            Service service = new Service(1, ServiceNames.BREAKFAST, 5000);
            service.setStatus(ServiceStatusTypes.PAID);
            service.setServiceTime(LocalDateTime.now());

            Client client = new Client(1, "Osipov D.R.", "+7(902)902-98-11");
            client.setCheckInTime(LocalDateTime.now());
            client.setCheckOutTime(LocalDateTime.now());

            RoomReservation reservation =
                    new RoomReservation(1, room, LocalDateTime.now(), LocalDateTime.now(), List.of(client));

            ProvidedService providedService =
                    new ProvidedService(1, service, LocalDateTime.now(), client);

            SerializationUtils.serialize(room, DataPath.SERIALIZE_DIRECTORY.getPath() + "room.json");
            Room newRoom =
                    SerializationUtils.deserialize(Room.class,
                            DataPath.SERIALIZE_DIRECTORY.getPath() + "room.json");
            System.out.println(newRoom);

            SerializationUtils.serialize(service, DataPath.SERIALIZE_DIRECTORY.getPath() + "service.json");
            Service newService =
                    SerializationUtils.deserialize(Service.class,
                            DataPath.SERIALIZE_DIRECTORY.getPath() + "service.json");
            System.out.println(newService);

            SerializationUtils.serialize(client, DataPath.SERIALIZE_DIRECTORY.getPath() + "client.json");
            Client newClient =
                    SerializationUtils.deserialize(Client.class,
                            DataPath.SERIALIZE_DIRECTORY.getPath() + "client.json");
            System.out.println(newClient);

            SerializationUtils.serialize(reservation, DataPath.SERIALIZE_DIRECTORY.getPath() +
                    "reservation.json");
            RoomReservation newReservation = SerializationUtils.deserialize(RoomReservation.class,
                    DataPath.SERIALIZE_DIRECTORY.getPath() + "reservation.json");
            System.out.println(newReservation);

            SerializationUtils.serialize(providedService,
                    DataPath.SERIALIZE_DIRECTORY.getPath() + "providedService.json");
            ProvidedService newProvidedService = SerializationUtils.deserialize(ProvidedService.class,
                    DataPath.SERIALIZE_DIRECTORY.getPath() + "providedService.json");
            System.out.println(newProvidedService);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
