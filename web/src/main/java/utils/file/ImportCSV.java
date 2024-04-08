package utils.file;

import dto.ClientDto;
import dto.ProvidedServiceDto;
import dto.RoomDto;
import dto.RoomReservationDto;
import dto.ServiceDto;
import essence.Identifiable;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.multipart.MultipartFile;
import utils.exceptions.TechnicalException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public final class ImportCSV {
    private ImportCSV() {
    }

    public static <T extends Identifiable> List<T> parseEntityDtoFromCsv(MultipartFile file, Class<T> clazz)
            throws IOException {
        try (
                InputStream inputStream = file.getInputStream();
                InputStreamReader reader = new InputStreamReader(inputStream);
                CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT)
        ){
            List<T> entities = new ArrayList<>();
            boolean isFirstLine = true;
            for (CSVRecord csvRecord : parser) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                T entity = parseEntityDto(csvRecord, clazz);
                entities.add(entity);
            }

            return entities;
        }
    }

    private static <T extends Identifiable> T parseEntityDto(CSVRecord csvRecord, Class<T> clazz) {
        String className = clazz.getSimpleName();
        switch (clazz.getSimpleName()) {
            case "ClientDto":
                return (T) parseClient(csvRecord);
            case "ServiceDto":
                return (T) parseService(csvRecord);
            case "RoomDto":
                return (T) parseRoom(csvRecord);
            case "RoomReservationDto":
                return (T) parseRoomReservation(csvRecord);
            case "ProvidedServiceDto":
                return (T) parseProvidedService(csvRecord);
            default:
                throw new TechnicalException("Отсутствует обработка для класса: " + className);
        }
    }

    private static ClientDto parseClient(CSVRecord csvRecord) {
        int id = Integer.parseInt(csvRecord.get(0));
        String fio = csvRecord.get(1);
        String phoneNumber = csvRecord.get(2);
        LocalDateTime checkInTime = csvRecord.get(3).equals("null") ? null : LocalDateTime.parse(csvRecord.get(3));
        LocalDateTime checkOutTime = csvRecord.get(4).equals("null") ? null : LocalDateTime.parse(csvRecord.get(4));

        ClientDto client = new ClientDto();
        client.setId(id);
        client.setFio(fio);
        client.setPhoneNumber(phoneNumber);
        client.setCheckInTime(checkInTime);
        client.setCheckOutTime(checkOutTime);
        return client;
    }

    private static ServiceDto parseService(CSVRecord csvRecord) {
        int id = Integer.parseInt(csvRecord.get(0));
        String name = csvRecord.get(1);
        int price = Integer.parseInt(csvRecord.get(2));
        String status = csvRecord.get(3);
        LocalDateTime serviceTime = csvRecord.get(4).equals("null") ? null : LocalDateTime.parse(csvRecord.get(4));

        ServiceDto service = new ServiceDto();
        service.setId(id);
        service.setName(name);
        service.setPrice(price);
        service.setStatus(status);
        service.setServiceTime(serviceTime);
        return service;
    }

    private static RoomDto parseRoom(CSVRecord csvRecord) {
        int id = Integer.parseInt(csvRecord.get(0));
        int number = Integer.parseInt(csvRecord.get(1));
        int capacity = Integer.parseInt(csvRecord.get(2));
        int price = Integer.parseInt(csvRecord.get(3));
        String status = csvRecord.get(4);
        int stars = Integer.parseInt(csvRecord.get(5));
        LocalDateTime checkInTime = csvRecord.get(6).equals("null") ? null : LocalDateTime.parse(csvRecord.get(6));
        LocalDateTime checkOutTime = csvRecord.get(7).equals("null") ? null : LocalDateTime.parse(csvRecord.get(7));

        RoomDto room = new RoomDto();
        room.setId(id);
        room.setNumber(number);
        room.setCapacity(capacity);
        room.setPrice(price);
        room.setStatus(status);
        room.setStars(stars);
        room.setCheckInTime(checkInTime);
        room.setCheckOutTime(checkOutTime);
        return room;
    }

    private static RoomReservationDto parseRoomReservation(CSVRecord csvRecord) {
        int id = Integer.parseInt(csvRecord.get(0));
        int roomId = Integer.parseInt(csvRecord.get(1));
        LocalDateTime checkInTime = csvRecord.get(2).equals("null") ? null : LocalDateTime.parse(csvRecord.get(2));
        LocalDateTime checkOutTime = csvRecord.get(3).equals("null") ? null : LocalDateTime.parse(csvRecord.get(3));
        String ids = csvRecord.get(4);
        List<Integer> clientIds = new ArrayList<>();
        for (String clientId : ids.split(", ")) {
            clientIds.add(Integer.parseInt(clientId));
        }

        RoomReservationDto reservation = new RoomReservationDto();
        reservation.setId(id);
        reservation.setRoomId(roomId);
        reservation.setCheckInTime(checkInTime);
        reservation.setCheckOutTime(checkOutTime);
        reservation.setClientIds(clientIds);
        return reservation;
    }

    private static ProvidedServiceDto parseProvidedService(CSVRecord csvRecord) {
        int id = Integer.parseInt(csvRecord.get(0));
        int clientId = Integer.parseInt(csvRecord.get(1));
        int serviceId = Integer.parseInt(csvRecord.get(2));
        LocalDateTime serviceTime = csvRecord.get(3).equals("null") ? null : LocalDateTime.parse(csvRecord.get(3));

        ProvidedServiceDto providedService = new ProvidedServiceDto();
        providedService.setId(id);
        providedService.setClientId(clientId);
        providedService.setServiceId(serviceId);
        providedService.setServiceTime(serviceTime);
        return providedService;
    }
}
