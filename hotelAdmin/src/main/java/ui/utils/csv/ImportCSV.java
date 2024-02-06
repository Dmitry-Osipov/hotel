package ui.utils.csv;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import essence.person.AbstractClient;
import essence.person.Client;
import essence.provided.ProvidedService;
import essence.reservation.RoomReservation;
import essence.room.AbstractRoom;
import essence.room.Room;
import essence.room.RoomStatusTypes;
import essence.service.AbstractService;
import essence.service.Service;
import essence.service.ServiceNames;
import essence.service.ServiceStatusTypes;
import lombok.Getter;
import ui.utils.exceptions.ErrorMessages;
import ui.utils.validators.FileValidator;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Класс импорта данных из CSV файла.
 */
@Getter
public class ImportCSV {
    private ImportCSV() {
    }


    /**
     * Метод импортирует данные по комнатам из CSV файла.
     * @param fileName Название файла без указания расширения.
     * @return Список комнат.
     * @throws IOException Ошибка при обработке файла.
     * @throws CsvValidationException Ошибка валидации файла, связанная с его структурой или данными внутри.
     */
    public static List<AbstractRoom> importRoomsData(String fileName) throws IOException, CsvValidationException {
        List<AbstractRoom> rooms = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(fileName + ".csv"))) {
            String[] header = reader.readNext();
            if (!FileValidator.isValidCsvHeader(header, CsvHeaderLength.ROOMS, CsvHeaderArrays.ROOMS)) {
                throw new IOException("\n" + ErrorMessages.INCORRECT_FILE_DATA_TYPE.getMessage());
            }

            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                int id = Integer.parseInt(nextLine[0]);
                int number = Integer.parseInt(nextLine[1]);
                int capacity = Integer.parseInt(nextLine[2]);
                int price = Integer.parseInt(nextLine[3]);
                RoomStatusTypes status = RoomStatusTypes.valueOf(nextLine[4]);
                int stars = Integer.parseInt(nextLine[5]);
                LocalDateTime checkInTime = parseDateTime(nextLine[6]);
                LocalDateTime checkOutTime = parseDateTime(nextLine[7]);

                AbstractRoom room = new Room(id, number, capacity, price);
                room.setStatus(status);
                room.setStars(stars);
                room.setCheckInTime(checkInTime);
                room.setCheckOutTime(checkOutTime);

                rooms.add(room);
            }
        }

        return rooms;
    }

    /**
     * Метод импортирует данные по услугам из CSV файла.
     * @param fileName Название файла без указания расширения.
     * @return Список услуг.
     * @throws IOException Ошибка при обработке файла.
     * @throws CsvValidationException Ошибка валидации файла, связанная с его структурой или данными внутри.
     */
    public static List<AbstractService> importServicesData(String fileName) throws IOException, CsvValidationException {
        List<AbstractService> services = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(fileName + ".csv"))) {
            String[] header = reader.readNext();
            if (!FileValidator.isValidCsvHeader(header, CsvHeaderLength.SERVICES, CsvHeaderArrays.SERVICES)) {
                throw new IOException("\n" + ErrorMessages.INCORRECT_FILE_DATA_TYPE.getMessage());
            }

            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                int id = Integer.parseInt(nextLine[0]);
                ServiceNames name = ServiceNames.valueOf(nextLine[1]);
                int price = Integer.parseInt(nextLine[2]);
                ServiceStatusTypes status = ServiceStatusTypes.valueOf(nextLine[3]);
                LocalDateTime serviceTime = parseDateTime(nextLine[4]);

                AbstractService service = new Service(id, name, price);
                service.setStatus(status);
                service.setServiceTime(serviceTime);

                services.add(service);
            }
        }

        return services;
    }

    /**
     * Метод импортирует данные по клиентам из CSV файла.
     * @param fileName Название файла без указания расширения.
     * @return Список клиентов.
     * @throws IOException Ошибка при обработке файла.
     * @throws CsvValidationException Ошибка валидации файла, связанная с его структурой или данными внутри.
     */
    public static List<AbstractClient> importClientsData(String fileName) throws IOException, CsvValidationException {
        List<AbstractClient> clients = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(fileName + ".csv"))) {
            String[] header = reader.readNext();
            if (!FileValidator.isValidCsvHeader(header, CsvHeaderLength.CLIENTS, CsvHeaderArrays.CLIENTS)) {
                throw new IOException("\n" + ErrorMessages.INCORRECT_FILE_DATA_TYPE.getMessage());
            }

            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                int id = Integer.parseInt(nextLine[0]);
                String fio = nextLine[1];
                String phoneNumber = nextLine[2];
                LocalDateTime checkInTime = parseDateTime(nextLine[3]);
                LocalDateTime checkOutTime = parseDateTime(nextLine[4]);

                AbstractClient client = new Client(id, fio, phoneNumber);
                client.setCheckInTime(checkInTime);
                client.setCheckOutTime(checkOutTime);

                clients.add(client);
            }
        }

        return clients;
    }

    /**
     * Метод импортирует данные по резервациям из CSV файла.
     * @param fileName Название файла без указания расширения.
     * @return Список резерваций.
     * @throws IOException Ошибка при обработке файла.
     * @throws CsvValidationException Ошибка валидации файла, связанная с его структурой или данными внутри.
     */
    public static List<RoomReservation> importReservationsData(String fileName) throws IOException,
            CsvValidationException {
        List<RoomReservation> reservations = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(fileName + ".csv"))) {
            String[] header = reader.readNext();
            if (!FileValidator.isValidCsvHeader(header, CsvHeaderLength.RESERVATIONS, CsvHeaderArrays.RESERVATIONS)) {
                throw new IOException("\n" + ErrorMessages.INCORRECT_FILE_DATA_TYPE.getMessage());
            }

            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                int id = Integer.parseInt(nextLine[0]);
                AbstractRoom room = parseRoom(nextLine[1]);
                LocalDateTime checkInTime = parseDateTime(nextLine[2]);
                LocalDateTime checkOutTime = parseDateTime(nextLine[3]);
                List<AbstractClient> clients = parseClients(nextLine[4]);

                reservations.add(new RoomReservation(id, room, checkInTime, checkOutTime, clients));
            }
        }

        return reservations;
    }

    /**
     * Метод импортирует данные по проведённым услугам из CSV файла.
     * @param fileName Название файла без указания расширения.
     * @return Список проведённых услуг.
     * @throws IOException Ошибка при обработке файла.
     * @throws CsvValidationException Ошибка валидации файла, связанная с его структурой или данными внутри.
     */
    public static List<ProvidedService> importProvidedServicesData(String fileName) throws IOException,
            CsvValidationException {
        List<ProvidedService> providedServices = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(fileName + ".csv"))) {
            String[] header = reader.readNext();
            if (!FileValidator.isValidCsvHeader(header, CsvHeaderLength.PROVIDED_SERVICES,
                    CsvHeaderArrays.PROVIDED_SERVICES)) {
                throw new IOException("\n" + ErrorMessages.INCORRECT_FILE_DATA_TYPE.getMessage());
            }

            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                int id = Integer.parseInt(nextLine[0]);
                List<AbstractClient> clients = parseClients(nextLine[1]);
                AbstractService service = parseService(nextLine[2]);
                LocalDateTime serviceTime = parseDateTime(nextLine[3]);

                ProvidedService providedService = new ProvidedService(id, service, serviceTime, clients.getFirst());
                for (int i = 1; i < clients.size(); i++) {
                    providedService.getBeneficiaries().add(clients.get(i));
                }

                providedServices.add(providedService);
            }
        }

        return providedServices;
    }

    /**
     * Служебный метод парсит строковое время и дату в экземпляр класса LocalDateTime.
     * @param dateTime Строка времени и даты.
     * @return Время, если строка или значение строки не равно null, иначе возвращается null.
     */
    private static LocalDateTime parseDateTime(String dateTime) {
        return dateTime != null && !dateTime.equals("null") ? LocalDateTime.parse(dateTime) : null;
    }

    /**
     * Служебный метод парсит строковое представление комнаты в объект комнаты.
     * @param room Строка комнаты.
     * @return Комната.
     */
    private static AbstractRoom parseRoom(String room) {
        room = room.substring(5, room.length() - 1);
        Map<String, String> roomMap = new HashMap<>();
        updateMapByArray(roomMap, room.split("; "));

        int id = Integer.parseInt(roomMap.get("id"));
        int number = Integer.parseInt(roomMap.get("number"));
        int capacity = Integer.parseInt(roomMap.get("capacity"));
        int price = Integer.parseInt(roomMap.get("price"));
        RoomStatusTypes status = RoomStatusTypes.valueOf(roomMap.get("status"));
        int stars = Integer.parseInt(roomMap.get("stars"));
        LocalDateTime checkInTime = parseDateTime(roomMap.get("check-in time"));
        LocalDateTime checkOutTime = parseDateTime(roomMap.get("check-out time"));

        AbstractRoom newRoom = new Room(id, number, capacity, price);
        newRoom.setStatus(status);
        newRoom.setStars(stars);
        newRoom.setCheckInTime(checkInTime);
        newRoom.setCheckOutTime(checkOutTime);

        return newRoom;
    }

    /**
     * Служебный метод парсит строковый список клиентов в обычный список клиентов.
     * @param client Список клиентов в виде строки.
     * @return Список клиентов.
     */
    private static List<AbstractClient> parseClients(String client) {
        List<AbstractClient> guests = new ArrayList<>();
        client = client.substring(1, client.length()-1);
        Map<String, String> clientMap = new HashMap<>();

        for (String clientString : client.split(", ")) {
            updateMapByArray(clientMap, clientString.substring(7, clientString.length() - 1).split("; "));

            int id = Integer.parseInt(clientMap.get("id"));
            String fio = clientMap.get("fio");
            String phoneNumber = clientMap.get("phoneNumber");
            LocalDateTime checkInTime = parseDateTime(clientMap.get("checkInTime"));
            LocalDateTime checkOutTime = parseDateTime(clientMap.get("checkOutTime"));

            AbstractClient guest = new Client(id, fio, phoneNumber);
            guest.setCheckInTime(checkInTime);
            guest.setCheckOutTime(checkOutTime);

            guests.add(guest);
        }

        return guests;
    }

    /**
     * Служебный метод обновляет переданную мапу ключом и значением переданного массива. Ключ - первый элемент массива,
     * значение - второй элемент массива.
     * @param map Мапа, которую необходимо обновить.
     * @param array Массив, откуда будут браться данные для обновления.
     */
    private static void updateMapByArray(Map<String, String> map, String[] array) {
        for (String data : array) {
            String[] keyValue = data.split("=");
            String key = keyValue[0].trim();
            String value = keyValue[1].trim();
            map.put(key, value);
        }
    }

    /**
     * Служебный метод парсит строковое представление услуги в экземпляр класса услуги.
     * @param service Строка услуги.
     * @return Услуга.
     */
    private static AbstractService parseService(String service) {
        service = service.substring(8, service.length() - 1);
        Map<String, String> serviceMap = new HashMap<>();
        updateMapByArray(serviceMap, service.split("; "));

        int id = Integer.parseInt(serviceMap.get("id"));
        ServiceNames name = ServiceNames.valueOf(serviceMap.get("name"));
        int price = Integer.parseInt(serviceMap.get("price"));
        ServiceStatusTypes status = ServiceStatusTypes.valueOf(serviceMap.get("status"));
        LocalDateTime serviceTime = parseDateTime(serviceMap.get("service time"));

        AbstractService newService = new Service(id, name, price);
        newService.setStatus(status);
        newService.setServiceTime(serviceTime);

        return newService;
    }
}
