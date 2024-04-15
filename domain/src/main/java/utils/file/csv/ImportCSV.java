package utils.file.csv;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.exceptions.ErrorMessages;
import utils.validators.FileValidator;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс импорта данных из CSV файла.
 */
public final class ImportCSV {
    private static final Logger logger = LoggerFactory.getLogger(ImportCSV.class);

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
        logger.info("Вызов метода импорта данных по комнатам из CSV файла {}", fileName);
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
                LocalDateTime checkInTime = EntityFromStringParser.parseDateTime(nextLine[6]);
                LocalDateTime checkOutTime = EntityFromStringParser.parseDateTime(nextLine[7]);

                AbstractRoom room = new Room(id, number, capacity, price);
                room.setStatus(status);
                room.setStars(stars);
                room.setCheckInTime(checkInTime);
                room.setCheckOutTime(checkOutTime);

                rooms.add(room);
            }
        } catch (IOException e) {
            logger.error("Не удалось импортировать данные по комнатам из CSV файла {}. Ошибка IO", fileName);
            throw e;
        } catch (CsvValidationException e) {
            logger.error("Не удалось импортировать данные по комнатам из CSV файла {}. Ошибка валидации", fileName);
            throw e;
        }

        logger.info("Удалось импортировать данные по комнатам из CSV файла {}. Полученный список: {}", fileName, rooms);
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
        logger.info("Вызов метода импорта данных по услугам из CSV файла {}", fileName);
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
                LocalDateTime serviceTime = EntityFromStringParser.parseDateTime(nextLine[4]);

                AbstractService service = new Service(id, name, price);
                service.setStatus(status);
                service.setServiceTime(serviceTime);

                services.add(service);
            }
        } catch (IOException e) {
            logger.error("Не удалось импортировать данные по услугам из CSV файла {}. Ошибка IO", fileName);
            throw e;
        } catch (CsvValidationException e) {
            logger.error("Не удалось импортировать данные по услугам из CSV файла {}. Ошибка валидации", fileName);
            throw e;
        }

        logger.info("Удалось импортировать данные по услугам из CSV файла {}. Полученный список: {}",
                fileName, services);
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
        logger.info("Вызов метода импорта данных по клиентам из CSV файла {}", fileName);
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
                LocalDateTime checkInTime = EntityFromStringParser.parseDateTime(nextLine[3]);
                LocalDateTime checkOutTime = EntityFromStringParser.parseDateTime(nextLine[4]);

                AbstractClient client = new Client(id, fio, phoneNumber);
                client.setCheckInTime(checkInTime);
                client.setCheckOutTime(checkOutTime);

                clients.add(client);
            }
        } catch (IOException e) {
            logger.error("Не удалось импортировать данные по клиентам из CSV файла {}. Ошибка IO", fileName);
            throw e;
        } catch (CsvValidationException e) {
            logger.error("Не удалось импортировать данные по клиентам из CSV файла {}. Ошибка валидации", fileName);
            throw e;
        }

        logger.info("Удалось импортировать данные по клиентам из CSV файла {}. Полученный список: {}",
                fileName, clients);
        return clients;
    }

    /**
     * Метод импортирует данные по резервациям из CSV файла.
     * @param fileName Название файла без указания расширения.
     * @return Список резерваций.
     * @throws IOException Ошибка при обработке файла.
     * @throws CsvValidationException Ошибка валидации файла, связанная с его структурой или данными внутри.
     * @throws utils.exceptions.AccessDeniedException Ошибка запрета изменения статуса комнаты.
     */
    public static List<RoomReservation> importReservationsData(String fileName) throws IOException,
            CsvValidationException {
        logger.info("Вызов метода импорта данных по резервациям из CSV файла {}", fileName);
        List<RoomReservation> reservations = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(fileName + ".csv"))) {
            String[] header = reader.readNext();
            if (!FileValidator.isValidCsvHeader(header, CsvHeaderLength.RESERVATIONS, CsvHeaderArrays.RESERVATIONS)) {
                throw new IOException("\n" + ErrorMessages.INCORRECT_FILE_DATA_TYPE.getMessage());
            }

            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                int id = Integer.parseInt(nextLine[0]);
                AbstractRoom room = EntityFromStringParser.parseRoom(nextLine[1]);
                LocalDateTime checkInTime = EntityFromStringParser.parseDateTime(nextLine[2]);
                LocalDateTime checkOutTime = EntityFromStringParser.parseDateTime(nextLine[3]);
                List<AbstractClient> clients = EntityFromStringParser.parseClients(nextLine[4]);

                reservations.add(new RoomReservation(id, room, checkInTime, checkOutTime, clients));
            }
        } catch (IOException e) {
            logger.error("Не удалось импортировать данные по резервациям из CSV файла {}. Ошибка IO", fileName);
            throw e;
        } catch (CsvValidationException e) {
            logger.error("Не удалось импортировать данные по резервация из CSV файла {}. Ошибка валидации", fileName);
            throw e;
        }

        logger.info("Удалось импортировать данные по резервациям из CSV файла {}. Полученный список: {}",
                fileName, reservations);
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
        logger.info("Вызов метода импорта данных по оказанным услугам из CSV файла {}", fileName);
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
                List<AbstractClient> clients = EntityFromStringParser.parseClients(nextLine[1]);
                AbstractService service = EntityFromStringParser.parseService(nextLine[2]);
                LocalDateTime serviceTime = EntityFromStringParser.parseDateTime(nextLine[3]);

                ProvidedService providedService = new ProvidedService(id, service, serviceTime, clients.get(0));
                for (int i = 1; i < clients.size(); i++) {
                    providedService.setClient(clients.get(i));
                }

                providedServices.add(providedService);
            }
        } catch (IOException e) {
            logger.error("Не удалось импортировать данные по оказанным услугам из CSV файла {}. Ошибка IO", fileName);
            throw e;
        } catch (CsvValidationException e) {
            logger.error("Не удалось импортировать данные по оказанным услугам из CSV файла {}. Ошибка валидации",
                    fileName);
            throw e;
        }

        logger.info("Удалось импортировать данные по оказанным услугам из CSV файла {}. Полученный список: {}",
                fileName, providedServices);
        return providedServices;
    }
}
