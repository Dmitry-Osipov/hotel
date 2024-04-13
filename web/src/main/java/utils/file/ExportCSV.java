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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
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

/**
 * Утилитарный класс для экспорта дто-сущностей в формат CSV.
 */
@Component
public final class ExportCSV {
    private final RoomService roomService;
    private final ServiceService serviceService;
    private final ClientService clientService;

    @Autowired
    public ExportCSV(RoomService roomService, ServiceService serviceService, ClientService clientService) {
        this.roomService = roomService;
        this.serviceService = serviceService;
        this.clientService = clientService;
    }

    /**
     * Экспортирует список сущностей в массив байтов в формате CSV.
     * @param entities Список сущностей для экспорта.
     * @return Массив байтов, представляющий CSV-данные экспортированных сущностей.
     * @throws IOException Если происходит ошибка ввода-вывода при чтении или записи CSV-файла.
     * @throws SQLException Если происходит ошибка доступа к базе данных при экспорте сущностей.
     * @throws TechnicalException Если нет обработки для класса предоставленных сущностей.
     */
    public <T extends Identifiable> byte[] exportEntitiesDtoDataToBytes(List<T> entities)
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
