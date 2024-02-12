import com.opencsv.exceptions.CsvValidationException;
import essence.person.Client;
import essence.room.Room;
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
import utils.file.csv.ExportCSV;
import utils.file.csv.ImportCSV;

import java.io.IOException;

public class TestCSV {
    public static void main(String[] args) {
        String exportPathRoom = "hotelAdmin/src/ui/utils/csv/data/test_room";
        String exportPathService = "hotelAdmin/src/ui/utils/csv/data/test_service";
        String exportPathClient = "hotelAdmin/src/ui/utils/csv/data/test_client";
        String exportPathReservations = "hotelAdmin/src/ui/utils/csv/data/test_reservations";
        String exportPathProvidedServices = "hotelAdmin/src/ui/utils/csv/data/test_provided_services";

        RoomService roomService =
                new RoomService(RoomRepository.getInstance(), RoomReservationRepository.getInstance());
        Room room1 = new Room(1, 1, 3, 3000);
        Room room2 = new Room(2, 2, 5, 13000);
        Room room3 = new Room(3, 3, 6, 20000);
        Room room4 = new Room(4, 4, 7, 30000);
        Room room5 = new Room(5, 5, 2, 8500);
        roomService.addRoom(room1);
        roomService.addRoom(room2);
        roomService.addRoom(room3);
        roomService.addRoom(room4);
        roomService.addRoom(room5);

        ServiceService serviceService =
                new ServiceService(ServiceRepository.getInstance(), ProvidedServicesRepository.getInstance());
        Service service1 = new Service(1, ServiceNames.CONFERENCE, 2000);
        Service service2 = new Service(2, ServiceNames.BREAKFAST, 1500);
        Service service3 = new Service(3, ServiceNames.EXCURSION, 20000);
        Service service4 = new Service(4, ServiceNames.MINIBAR, 5000);
        Service service5 = new Service(5, ServiceNames.PARKING, 3000);
        serviceService.addService(service1);
        serviceService.addService(service2);
        serviceService.addService(service3);
        serviceService.addService(service4);
        serviceService.addService(service5);

        ClientService clientService = new ClientService(ClientRepository.getInstance());
        Client client1 = new Client(1, "Osipov D. R.", "+7(902)902-98-11");
        Client client2 = new Client(2, "Musofranova N. S.", "+7(961)150-09-97");
        Client client3 = new Client(3, "Belyakova I. S.", "+7(953)180-00-61");
        Client client4 = new Client(4, "Kondrashin E. V.", "+7(991)234-11-00");
        Client client5 = new Client(5, "Lebedev G. G.", "+7(953)542-28-99");
        clientService.addClient(client1);
        clientService.addClient(client2);
        clientService.addClient(client3);
        clientService.addClient(client4);
        clientService.addClient(client5);

        try {
            roomService.checkIn(room1, client1, client2);
            roomService.addStarsToRoom(room3, 5);
            roomService.addStarsToRoom(room5, 4);
            roomService.addStarsToRoom(room4, 3);
            roomService.addStarsToRoom(room1, 5);
            roomService.addStarsToRoom(room2, 4);
            roomService.checkIn(room2, client3);
        } catch (IOException e) {
            e.printStackTrace();
        }

        serviceService.provideService(client1, service1);
        serviceService.provideService(client3, service4);

        clientService.updateClient(new Client(5, "Lebedev G.I.", "+7(921)728-21-01"));

        try {
            System.out.println("Результаты по экспорту данных: ");
            ExportCSV.exportRoomsData(exportPathRoom, roomService.roomsByStars());
            ExportCSV.exportServicesData(exportPathService, serviceService.getServices());
            ExportCSV.exportClientsData(exportPathClient, clientService.getClients());
            ExportCSV.exportReservationsData(exportPathReservations, roomService.getReservations());
            ExportCSV.exportProvidedServicesData(exportPathProvidedServices, serviceService.getProvidedServices());

            System.out.println("\nРезультаты по импорту данных: ");
            System.out.println(ImportCSV.importRoomsData(exportPathRoom));
            System.out.println(ImportCSV.importServicesData(exportPathService));
            System.out.println(ImportCSV.importClientsData(exportPathClient));
            System.out.println(ImportCSV.importReservationsData(exportPathReservations));
            System.out.println(ImportCSV.importProvidedServicesData(exportPathProvidedServices));
        } catch (IOException | CsvValidationException e) {
            System.out.println(e.getMessage());
        }
    }
}
