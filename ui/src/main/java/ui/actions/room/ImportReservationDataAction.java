package ui.actions.room;

import com.opencsv.exceptions.CsvValidationException;
import essence.reservation.RoomReservation;
import service.RoomService;
import ui.actions.IAction;
import utils.InputHandler;
import utils.csv.FileAdditionResult;
import utils.csv.ImportCSV;
import utils.exceptions.ErrorMessages;

import java.io.IOException;
import java.util.List;

/**
 * Класс представляет собой действие по импорту данных о резервациях.
 */
public class ImportReservationDataAction implements IAction {
    private final RoomService roomService;

    /**
     * Класс представляет собой действие по импорту данных о резервациях.
     * @param roomService Сервис для работы с данными о комнатах и резервациях.
     */
    public ImportReservationDataAction(RoomService roomService) {
        this.roomService = roomService;
    }

    /**
     * Метод execute выполняет действие по импорту данных о резервациях. Пользователю предлагается ввести название файла
     * без расширения. Происходит импорт данных из CSV файла. Для каждой резервации производится попытка обновления
     * данных. В случае неудачи производится попытка добавления. Выводится результат операции на консоль.
     */
    @Override
    public void execute() {
        try {
            String path = FileAdditionResult.getDataDirectory() + InputHandler.getFileNameFromUser();
            List<RoomReservation> reservations = ImportCSV.importReservationsData(path);
            String result;
            for (RoomReservation reservation : reservations) {
                int id = reservation.getId();
                boolean updated = roomService.updateReservation(reservation);
                if (updated) {
                    result = "Данные по резервации с ID " + id + " успешно обновлены";
                } else {
                    roomService.addReservation(reservation);
                    result = "Резервация с ID " + id + " успешно добавлена";
                }
                System.out.println("\n" + result);
            }
        } catch (IOException | CsvValidationException e) {
            System.out.println("\n" + ErrorMessages.FILE_ERROR.getMessage());
        }
    }
}
