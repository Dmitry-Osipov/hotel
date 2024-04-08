package utils.file;

import dto.ClientDto;
import dto.ProvidedServiceDto;
import dto.RoomDto;
import dto.RoomReservationDto;
import dto.ServiceDto;
import essence.Identifiable;
import essence.person.AbstractClient;
import essence.provided.ProvidedService;
import essence.reservation.RoomReservation;
import essence.room.AbstractRoom;
import essence.service.AbstractService;
import service.ClientService;
import service.RoomService;
import service.ServiceService;
import utils.DtoConverter;
import utils.exceptions.TechnicalException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class ExportCSV {
    private ExportCSV() {
    }

    public static <T extends Identifiable> byte[] exportEntitiesDtoDataToBytes(List<T> entities,
                                                                               RoomService roomService,
                                                                               ClientService clientService,
                                                                               ServiceService serviceService)
            throws IOException, SQLException {
        String filePath;
        Path path;
        String className = entities.get(0).getClass().getSimpleName();
        switch (className) {
            case "ClientDto":
                filePath = "client.csv";
                List<AbstractClient> clients = new ArrayList<>();
                for (T dto : entities) {
                    AbstractClient client = DtoConverter.convertDtoToClient((ClientDto) dto);
                    clients.add(client);
                }
                utils.file.csv.ExportCSV.exportClientsData(filePath.substring(0, filePath.length() - 4), clients);
                break;
            case "ServiceDto":
                filePath = "service.csv";
                List<AbstractService> services = new ArrayList<>();
                for (T dto : entities) {
                    AbstractService service = DtoConverter.convertDtoToService((ServiceDto) dto);
                    services.add(service);
                }
                utils.file.csv.ExportCSV.exportServicesData(filePath.substring(0, filePath.length() - 4), services);
                break;
            case "RoomDto":
                filePath = "room.csv";
                List<AbstractRoom> rooms = new ArrayList<>();
                for (T dto : entities) {
                    AbstractRoom room = DtoConverter.convertDtoToRoom((RoomDto) dto);
                    rooms.add(room);
                }
                utils.file.csv.ExportCSV.exportRoomsData(filePath.substring(0, filePath.length() - 4), rooms);
                break;
            case "RoomReservationDto":
                filePath = "reservation.csv";
                List<RoomReservation> reservations = new ArrayList<>();
                for (T dto : entities) {
                    RoomReservation reservation = DtoConverter.convertDtoToRoomReservation((RoomReservationDto) dto,
                            roomService, clientService);
                    reservations.add(reservation);
                }
                utils.file.csv.ExportCSV.exportReservationsData(filePath.substring(0, filePath.length() - 4),
                        reservations);
                break;
            case "ProvidedServiceDto":
                filePath = "provided.csv";
                List<ProvidedService> providedServices = new ArrayList<>();
                for (T dto : entities) {
                    ProvidedService providedService = DtoConverter.convertDtoToProvidedService((ProvidedServiceDto) dto,
                            serviceService, clientService);
                    providedServices.add(providedService);
                }
                utils.file.csv.ExportCSV.exportProvidedServicesData(filePath.substring(0, filePath.length() - 4),
                        providedServices);
                break;
            default:
                throw new TechnicalException("Отсутствует обработка для класса: " + className);
        }

        path = Paths.get(filePath);
        return Files.readAllBytes(path);
    }
}
