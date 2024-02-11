package ui.actions.room;

import service.RoomService;
import ui.actions.IAction;
import utils.InputHandler;
import utils.csv.ExportCSV;
import utils.csv.FileAdditionResult;
import utils.exceptions.ErrorMessages;

import java.io.IOException;

/**
 * Класс представляет собой действие по экспорту данных о резервациях.
 */
public class ExportReservationsDataAction implements IAction {
    private final RoomService roomService;

    /**
     * Класс представляет собой действие по экспорту данных о резервациях.
     * @param roomService Сервис для работы с данными о комнатах и резервациях.
     */
    public ExportReservationsDataAction(RoomService roomService) {
        this.roomService = roomService;
    }

    /**
     * Метод execute выполняет действие по экспорту данных о резервациях. Пользователю предлагается ввести название
     * файла без расширения. Пользователю также предоставляется выбор: перезаписать существующий файл или нет.
     * В зависимости от выбора пользователя происходит экспорт данных или вывод сообщения об отмене операции.
     * Выводится результат операции на консоль.
     */
    @Override
    public void execute() {
        try {
            String path = FileAdditionResult.getDataDirectory() + InputHandler.getFileNameFromUser();
            String choice = InputHandler.getUserOverwriteChoice(path);
            if (choice.equals("да")) {
                ExportCSV.exportReservationsData(path, roomService.getReservations());
                System.out.println("\n" + FileAdditionResult.SUCCESS.getMessage());
            } else {
                System.out.println("\n" + FileAdditionResult.FAILURE.getMessage());
            }
        } catch (IOException e) {
            System.out.println("\n" + ErrorMessages.FILE_ERROR.getMessage());
        }
    }
}
