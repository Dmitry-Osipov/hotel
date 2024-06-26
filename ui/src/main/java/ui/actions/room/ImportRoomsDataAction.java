package ui.actions.room;

import com.opencsv.exceptions.CsvValidationException;
import essence.room.AbstractRoom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import service.RoomService;
import ui.actions.IAction;
import utils.InputHandler;
import utils.exceptions.AccessDeniedException;
import utils.exceptions.EntityContainedException;
import utils.exceptions.ErrorMessages;
import utils.exceptions.TechnicalException;
import utils.file.DataPath;
import utils.file.csv.ImportCSV;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Класс представляет собой действие по импорту данных о комнатах.
 */
@Component
public class ImportRoomsDataAction implements IAction {
    private final RoomService roomService;

    @Autowired
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
            String path = DataPath.CSV_DIRECTORY.getPath() + InputHandler.getFileNameFromUser();
            List<AbstractRoom> rooms = ImportCSV.importRoomsData(path);
            String result;
            for (AbstractRoom room : rooms) {
                int id = room.getId();
                try {
                    roomService.updateRoom(room);
                    result = "Данные по комнате с ID " + id + " успешно обновлены";
                } catch (TechnicalException e) {
                    roomService.addRoom(room);
                    result = "Комната с ID " + id + " успешно добавлена";
                }
                System.out.println("\n" + result);
            }
        } catch (IOException | CsvValidationException e) {
            System.out.println("\n" + ErrorMessages.FILE_ERROR.getMessage());
        } catch (EntityContainedException | AccessDeniedException e) {
            System.out.println("\n" + e.getMessage());
        } catch (SQLException e) {
            System.out.println("\n" + ErrorMessages.FATAL_ERROR.getMessage());
        }
    }
}
