package ui.actions.room;

import com.opencsv.exceptions.CsvValidationException;
import essence.reservation.RoomReservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import service.RoomService;
import ui.actions.IAction;
import utils.InputHandler;
import utils.exceptions.AccessDeniedException;
import utils.exceptions.ErrorMessages;
import utils.exceptions.TechnicalException;
import utils.file.DataPath;
import utils.file.csv.ImportCSV;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Класс представляет собой действие по импорту данных о резервациях.
 */
@Component
public class ImportReservationDataAction implements IAction {
    private final RoomService roomService;

    @Autowired
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
            String path = DataPath.CSV_DIRECTORY.getPath() + InputHandler.getFileNameFromUser();
            List<RoomReservation> reservations = ImportCSV.importReservationsData(path);
            String result;
            for (RoomReservation reservation : reservations) {
                int id = reservation.getId();
                try{
                    roomService.updateReservation(reservation);
                    result = "Данные по резервации с ID " + id + " успешно обновлены";
                } catch (TechnicalException e) {
                    roomService.addReservation(reservation);
                    result = "Резервация с ID " + id + " успешно добавлена";
                }
                System.out.println("\n" + result);
            }
        } catch (IOException | CsvValidationException e) {
            System.out.println("\n" + ErrorMessages.FILE_ERROR.getMessage());
        } catch (AccessDeniedException e) {
            System.out.println("\n" + e.getMessage());
        } catch (SQLException e) {
            System.out.println("\n" + ErrorMessages.FATAL_ERROR.getMessage());
        }
    }
}
