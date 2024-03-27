package ui.actions.room;

import annotations.annotation.Autowired;
import annotations.annotation.Component;
import service.RoomService;
import ui.actions.IAction;
import utils.InputHandler;
import utils.exceptions.ErrorMessages;
import utils.file.DataPath;
import utils.file.FileAdditionResult;
import utils.file.csv.ExportCSV;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Класс представляет собой действие по экспорту данных о резервациях.
 */
@Component
public class ExportReservationsDataAction implements IAction {
    @Autowired
    private RoomService roomService;

    /**
     * Метод execute выполняет действие по экспорту данных о резервациях. Пользователю предлагается ввести название
     * файла без расширения. Пользователю также предоставляется выбор: перезаписать существующий файл или нет.
     * В зависимости от выбора пользователя происходит экспорт данных или вывод сообщения об отмене операции.
     * Выводится результат операции на консоль.
     */
    @Override
    public void execute() {
        try {
            String path = DataPath.CSV_DIRECTORY.getPath() + InputHandler.getFileNameFromUser();
            String choice = InputHandler.getUserOverwriteChoice(path + ".csv");
            if (choice.equals("да")) {
                ExportCSV.exportReservationsData(path, roomService.getReservations());
                System.out.println("\n" + FileAdditionResult.SUCCESS.getMessage());
            } else {
                System.out.println("\n" + FileAdditionResult.FAILURE.getMessage());
            }
        } catch (IOException e) {
            System.out.println("\n" + ErrorMessages.FILE_ERROR.getMessage());
        } catch (SQLException e) {
            System.out.println("\n" + ErrorMessages.FATAL_ERROR.getMessage());
        }
    }
}
