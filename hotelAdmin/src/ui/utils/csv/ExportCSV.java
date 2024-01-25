package ui.utils.csv;

import com.opencsv.CSVWriter;
import essence.person.AbstractClient;
import essence.room.AbstractRoom;
import essence.service.AbstractService;
import lombok.Getter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Класс экспорта данных в CSV файл.
 */
@Getter
public class ExportCSV {
    private String fileName;
    private final List<AbstractRoom> rooms;
    private final List<AbstractService> services;
    private final List<AbstractClient> clients;

    /**
     * Класс экспорта данных в CSV файл.
     * @param fileName Путь + имя файла без указания расширения.
     * @param rooms Список комнат.
     * @param services Список услуг.
     * @param clients Список клиентов.
     */
    public ExportCSV(String fileName, List<AbstractRoom> rooms, List<AbstractService> services,
                     List<AbstractClient> clients) {
        this.fileName = fileName + ".csv";
        this.rooms = rooms;
        this.services = services;
        this.clients = clients;
    }

    /**
     * Метод устанавливает новый файл.
     * @param fileName Путь + имя файла без указания расширения.
     */
    public void setFileName(String fileName) {
        this.fileName = fileName + ".csv";
    }

    /**
     * Экспорт данных из списка комнат в CSV файл.
     * @return true, если удалось записать данные, иначе false.
     * @throws IOException Ошибка при обработке файла.
     */
    public boolean exportRoomsData() throws IOException {
        try (CSVWriter writer = new CSVWriter(new FileWriter(fileName))) {
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
        }

        return true;
    }

    /**
     * Экспорт данных из списка услуг в CSV файл.
     * @return true, если удалось записать данные, иначе false.
     * @throws IOException Ошибка при обработке файла.
     */
    public boolean exportServicesData() throws IOException {
        try (CSVWriter writer = new CSVWriter(new FileWriter(fileName))) {
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
        }

        return true;
    }

    /**
     * Экспорт данных из списка клиентов в CSV файл.
     * @return true, если удалось записать данные, иначе false.
     * @throws IOException ошибка при обработке файла.
     */
    public boolean exportClientsData() throws IOException {
        try (CSVWriter writer = new CSVWriter(new FileWriter(fileName))) {
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
        }

        return true;
    }
}
