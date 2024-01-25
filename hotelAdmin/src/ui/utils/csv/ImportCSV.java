package ui.utils.csv;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import essence.person.AbstractClient;
import essence.person.Client;
import essence.room.AbstractRoom;
import essence.room.Room;
import essence.room.RoomStatusTypes;
import essence.service.AbstractService;
import essence.service.Service;
import essence.service.ServiceNames;
import essence.service.ServiceStatusTypes;
import lombok.Getter;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс импорта данных из CSV файла.
 */
@Getter
public class ImportCSV {
    private String fileName;

    /**
     * Класс импорта данных из CSV файла.
     * @param fileName Путь + имя файла без указания расширения.
     */
    public ImportCSV(String fileName) {
        this.fileName = fileName + ".csv";
    }

    /**
     * Метод устанавливает новый файл.
     * @param fileName Путь + имя файла без указания расширения.
     */
    public void setFileName(String fileName) {
        this.fileName = fileName + ".csv";
    }

    /**
     * Метод импортирует данные по комнатам из CSV файла.
     * @return Список комнат.
     * @throws IOException Ошибка при обработке файла.
     * @throws CsvValidationException Ошибка валидации файла, связанная с его структурой или данными внутри.
     */
    public List<AbstractRoom> importRoomsData() throws IOException, CsvValidationException {
        List<AbstractRoom> rooms = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(fileName))) {
            reader.readNext();

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
     * @return Список услуг.
     * @throws IOException Ошибка при обработке файла.
     * @throws CsvValidationException Ошибка валидации файла, связанная с его структурой или данными внутри.
     */
    public List<AbstractService> importServicesData() throws IOException, CsvValidationException {
        List<AbstractService> services = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(fileName))) {
            reader.readNext();

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
     * @return Список клиентов.
     * @throws IOException Ошибка при обработке файла.
     * @throws CsvValidationException Ошибка валидации файла, связанная с его структурой или данными внутри.
     */
    public List<AbstractClient> importClientsData() throws IOException, CsvValidationException {
        List<AbstractClient> clients = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(fileName))) {
            reader.readNext();

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
     * Служебный метод парсит строковое время и дату в экземпляр класса LocalDateTime.
     * @param dateTime Строка времени и даты.
     * @return Время, если строка или значение строки не равно null, иначе возвращается null.
     */
    private LocalDateTime parseDateTime(String dateTime) {
        return dateTime != null && !dateTime.equals("null") ? LocalDateTime.parse(dateTime) : null;
    }
}
