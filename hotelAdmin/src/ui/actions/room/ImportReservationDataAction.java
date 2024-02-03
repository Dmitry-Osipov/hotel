package ui.actions.room;

import com.opencsv.exceptions.CsvValidationException;
import essence.reservation.RoomReservation;
import service.RoomService;
import ui.actions.IAction;
import ui.utils.exceptions.ErrorMessages;
import ui.utils.InputHandler;
import ui.utils.csv.FileAdditionResult;
import ui.utils.csv.ImportCSV;

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
                boolean updated = roomService.updateReservation(reservation);
                int id = reservation.getId();
                if (!updated) {
                    boolean added = roomService.addReservation(reservation);
                    if (!added) {
                        result = ErrorMessages.RESERVATION_ADDITION_FAILURE.getMessage() + " с ID " + id;
                    } else {
                        result = "Резервация с ID " + id + " успешно добавлена";
                    }
                } else {
                    result = "Данные по резервации с ID " + id + " успешно обновлены";
                }
                System.out.println("\n" + result);
            }
        } catch (IOException | CsvValidationException e) {
            System.out.println("\n" + ErrorMessages.FILE_ERROR.getMessage());
        }
    }
}
