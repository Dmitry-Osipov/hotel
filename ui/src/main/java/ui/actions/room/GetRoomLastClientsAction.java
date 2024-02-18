package ui.actions.room;

import essence.person.AbstractClient;
import essence.room.AbstractRoom;
import service.RoomService;
import ui.actions.IAction;
import utils.InputHandler;
import utils.exceptions.ErrorMessages;
import utils.exceptions.NoEntityException;
import utils.file.DataPath;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;


/**
 * Класс предоставляет логику выполнения действия по выводу последних клиентов комнаты.
 */
public class GetRoomLastClientsAction implements IAction {
    private final RoomService roomService;

    /**
     * Класс предоставляет логику выполнения действия по выводу последних клиентов комнаты.
     * @param roomService Класс обработки данных по комнатам.
     */
    public GetRoomLastClientsAction(RoomService roomService) {
        this.roomService = roomService;
    }

    /**
     * Метод выполняет действие по выводу последних клиентов комнаты. При выполнении действия выводится указанное
     * количество последних клиентов комнаты. Если комнат в отеле нет, выводится соответствующее сообщение.
     */
    @Override
    public void execute() {
        try {
            AbstractRoom room = InputHandler.getRoomByInput();
            int count = getNumClientsFromPropertyFile();
            List<AbstractClient> clients = roomService.getRoomLastClients(room, count);
            System.out.println("\nПоследние клиенты комнаты: ");
            for (int i = 0; i < clients.size(); i++) {
                System.out.println(i+1 + ". " + clients.get(i));
            }
        } catch (NoEntityException e) {
            System.out.println("\n" + e.getMessage());
        } catch (IOException | NumberFormatException e) {
            System.out.println("\n" + ErrorMessages.FILE_ERROR.getMessage());
        }
    }

    /**
     * Метод получает данные по количеству клиентов, которых нужно хранить в истории резервации.
     * @return Количество клиентов, которых нужно хранить в истории. По умолчанию 10.
     * @throws NumberFormatException Ошибка парсинга числа из файла.
     * @throws IOException Ошибка ввода/вывода.
     */
    private int getNumClientsFromPropertyFile() throws IOException {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream(DataPath.PROPERTY_FILE.getPath())) {
            properties.load(fis);
            return Integer.parseInt(properties.getProperty("number_last_clients"));
        }
    }
}
