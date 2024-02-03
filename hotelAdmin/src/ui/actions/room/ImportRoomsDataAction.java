package ui.actions.room;

import com.opencsv.exceptions.CsvValidationException;
import essence.room.AbstractRoom;
import service.RoomService;
import ui.actions.IAction;
import ui.utils.exceptions.ErrorMessages;
import ui.utils.InputHandler;
import ui.utils.csv.FileAdditionResult;
import ui.utils.csv.ImportCSV;

import java.io.IOException;
import java.util.List;

/**
 * Класс представляет собой действие по импорту данных о комнатах.
 */
public class ImportRoomsDataAction implements IAction {
    private final RoomService roomService;

    /**
     * Класс представляет собой действие по импорту данных о комнатах.
     * @param roomService Сервис для работы с данными о комнатах.
     */
    public ImportRoomsDataAction(RoomService roomService) {
        this.roomService = roomService;
    }

    /**
     * Метод execute выполняет действие по импорту данных о комнатах. Пользователю предлагается ввести название файла
     * без расширения. Происходит импорт данных из CSV файла. Для каждой комнаты производится попытка обновления данных.
     * В случае неудачи производится попытка добавления. Выводится результат операции на консоль.
     */
    @Override
    public void execute() {
        try {
            String path = FileAdditionResult.getDataDirectory() + InputHandler.getFileNameFromUser();
            List<AbstractRoom> rooms = ImportCSV.importRoomsData(path);
            String result;
            for (AbstractRoom room : rooms) {
                boolean updated = roomService.updateRoom(room);
                int id = room.getId();
                if (!updated) {
                    boolean added = roomService.addRoom(room);
                    if (!added) {
                        result = ErrorMessages.ROOM_ADDITION_FAILURE.getMessage() + " с ID " + id;
                    } else {
                        result = "Комната с ID " + id + " успешно добавлена";
                    }
                } else {
                    result = "Данные по комнате с ID " + id + " успешно обновлены";
                }
                System.out.println("\n" + result);
            }
        } catch (IOException | CsvValidationException e) {
            System.out.println("\n" + ErrorMessages.FILE_ERROR.getMessage());
        }
    }
}
