package utils.file.csv;

import com.opencsv.CSVWriter;
import essence.person.AbstractClient;
import essence.provided.ProvidedService;
import essence.reservation.RoomReservation;
import essence.room.AbstractRoom;
import essence.service.AbstractService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Класс экспорта данных в CSV файл.
 */
public final class ExportCSV {
    private static final Logger logger = LoggerFactory.getLogger(ExportCSV.class);

    private ExportCSV() {
    }

    /**
     * Метод экспортирует данные из списка комнат в CSV файл.
     * @param fileName Название файла без указания расширения.
     * @param rooms Список комнат.
     * @throws IOException Ошибка при обработке файла.
     */
    public static void exportRoomsData(String fileName, List<AbstractRoom> rooms) throws IOException {
        logger.info("Вызов метода экспорта данных в CSV файл {} списка комнат {}", fileName, rooms);
        try (CSVWriter writer = new CSVWriter(new FileWriter(fileName + ".csv"))) {
            writer.writeNext(new String[]{"ID", "Номер", "Вместительность", "Цена", "Статус", "Количество звёзд",
                    "Время въезда", "Время выезда"});

            for (AbstractRoom room : rooms) {
                String[] data = {
                        String.valueOf(room.getId()),
                        String.valueOf(room.getNumber()),
                        String.valueOf(room.getCapacity()),
                        String.valueOf(room.getPrice()),
                        String.valueOf(room.getStatus()),
                        String.valueOf(room.getStars()),
                        String.valueOf(room.getCheckInTime()),
                        String.valueOf(room.getCheckOutTime())
                };
                writer.writeNext(data);
            }
        } catch (IOException e) {
            logger.error("Не удалось экспортировать в CSV файл {} список комнат {}", fileName, rooms);
            throw new IOException(e.getMessage());
        }

        logger.info("Удалось экспортировать в CSV файл {} список комнат {}", fileName, rooms);
    }

    /**
     * Метод экспортирует данные из списка услуг в CSV файл.
     * @param fileName Название файла без указания расширения.
     * @param services Список услуг.
     * @throws IOException Ошибка при обработке файла.
     */
    public static void exportServicesData(String fileName, List<AbstractService> services) throws IOException {
        logger.info("Вызов метода экспорта данных в CSV файл {} списка услуг {}", fileName, services);
        try (CSVWriter writer = new CSVWriter(new FileWriter(fileName + ".csv"))) {
            writer.writeNext(new String[]{"ID", "Название", "Цена", "Статус", "Время оказания"});
            for (AbstractService service : services) {
                String[] data = {
                        String.valueOf(service.getId()),
                        String.valueOf(service.getName()),
                        String.valueOf(service.getPrice()),
                        String.valueOf(service.getStatus()),
                        String.valueOf(service.getServiceTime())
                };
                writer.writeNext(data);
            }
        } catch (IOException e) {
            logger.error("Не удалось экспортировать в CSV файл {} список услуг {}", fileName, services);
            throw new IOException(e.getMessage());
        }

        logger.info("Удалось экспортировать в CSV файл {} список услуг {}", fileName, services);
    }

    /**
     * Метод экспортирует данные из списка клиентов в CSV файл.
     * @param fileName Название файла без указания расширения.
     * @param clients Список клиентов.
     * @throws IOException Ошибка при обработке файла.
     */
    public static void exportClientsData(String fileName, List<AbstractClient> clients) throws IOException {
        logger.info("Вызов метода экспорта данных в CSV файл {} списка клиентов {}", fileName, clients);
        try (CSVWriter writer = new CSVWriter(new FileWriter(fileName + ".csv"))) {
            writer.writeNext(new String[]{"ID", "ФИО", "Номер телефона", "Время заезда", "Время выезда"});
            for (AbstractClient client : clients) {
                String[] data = {
                        String.valueOf(client.getId()),
                        client.getFio(),
                        client.getPhoneNumber(),
                        String.valueOf(client.getCheckInTime()),
                        String.valueOf(client.getCheckOutTime())
                };
                writer.writeNext(data);
            }
        } catch (IOException e) {
            logger.error("Не удалось экспортировать в CSV файл {} список клиентов {}", fileName, clients);
            throw new IOException(e.getMessage());
        }

        logger.info("Удалось экспортировать в CSV файл {} список клиентов {}", fileName, clients);
    }

    /**
     * Метод экспортирует данные из списка резерваций в CSV файл.
     * @param fileName Название файла без указания расширения.
     * @param reservations Список резерваций.
     * @throws IOException Ошибка при обработке файла.
     */
    public static void exportReservationsData(String fileName, List<RoomReservation> reservations) throws IOException {
        logger.info("Вызов метода экспорта данных в CSV файл {} списка резерваций {}", fileName, reservations);
        try (CSVWriter writer = new CSVWriter(new FileWriter(fileName + ".csv"))) {
            writer.writeNext(new String[] {"ID", "Комната", "Время заезда", "Время выезда", "Список клиентов"});
            for (RoomReservation reservation : reservations) {
                String[] data = {
                        String.valueOf(reservation.getId()),
                        reservation.getRoom().toString(),
                        String.valueOf(reservation.getCheckInTime()),
                        String.valueOf(reservation.getCheckOutTime()),
                        reservation.getClients().toString()
                };
                writer.writeNext(data);
            }
        } catch (IOException e) {
            logger.error("Не удалось экспортировать в CSV файл {} список резерваций {}", fileName, reservations);
            throw new IOException(e.getMessage());
        }

        logger.info("Удалось экспортировать в CSV файл {} список резерваций {}", fileName, reservations);
    }

    /**
     * Метод экспортирует данные из списка проведённых услуг в CSV файл.
     * @param fileName Название файла без указания расширения.
     * @param providedServices Список проведённых услуг.
     * @throws IOException Ошибка при обработке файла.
     */
    public static void exportProvidedServicesData(String fileName, List<ProvidedService> providedServices)
            throws IOException {
        logger.info("Вызов метода экспорта данных в CSV файл {} списка оказанных услуг {}", fileName, providedServices);
        try (CSVWriter writer = new CSVWriter(new FileWriter(fileName + ".csv"))) {
            writer.writeNext(new String[] {"ID", "Список клиентов", "Услуга", "Время оказания"});
            for (ProvidedService providedService : providedServices) {
                String[] data = {
                        String.valueOf(providedService.getId()),
                        providedService.getBeneficiaries().toString(),
                        providedService.getService().toString(),
                        String.valueOf(providedService.getServiceTime())
                };
                writer.writeNext(data);
            }
        } catch (IOException e) {
            logger.error("Не удалось экспортировать в CSV файл {} список оказанных услуг {}",
                    fileName, providedServices);
            throw new IOException(e.getMessage());
        }

        logger.info("Удалось экспортировать в CSV файл {} список оказанных услуг {}", fileName, providedServices);
    }
}
