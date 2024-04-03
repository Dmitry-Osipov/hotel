package ui.actions.room;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import service.RoomService;
import ui.actions.IAction;
import utils.InputHandler;
import utils.exceptions.ErrorMessages;
import utils.exceptions.NoEntityException;
import utils.printers.RoomsPrinter;
import utils.validators.ArrayDigitsValidator;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * Класс предоставляет логику выполнения действия по выводу списка свободных комнат с конкретного времени.
 */
@Component
public class GetAvailableRoomsByTimeAction implements IAction {
    private final RoomService roomService;

    @Autowired
    public GetAvailableRoomsByTimeAction(RoomService roomService) {
        this.roomService = roomService;
    }

    /**
     * Метод выполняет действие по выводу списка свободных комнат с конкретного времени. При выполнении
     * действия выводится список свободных комнат. Если свободных комнат нет, пользователю выводится соответствующее
     * сообщение.
     */
    @Override
    public void execute() {
        System.out.println("\nВведите год, месяц, день, час и минуты через пробел: ");
        String[] dateTime = InputHandler.getUserInput().split(" ");
        while (dateTime.length != 5 || !ArrayDigitsValidator.isArrayOfDigits(dateTime)) {
            System.out.println("\n" + ErrorMessages.INCORRECT_INPUT.getMessage());
            dateTime = InputHandler.getUserInput().split(" ");
        }

        System.out.println("\nСвободные комнаты с " + Arrays.toString(dateTime) + ": ");
        try {
            RoomsPrinter.printRooms(roomService.getAvailableRoomsByTime(LocalDateTime.of(
                    Integer.parseInt(dateTime[0]),
                    Integer.parseInt(dateTime[1]),
                    Integer.parseInt(dateTime[2]),
                    Integer.parseInt(dateTime[3]),
                    Integer.parseInt(dateTime[4])
            )));
        } catch (NoEntityException e) {
            System.out.println("\n" + e.getMessage());
        } catch (SQLException e) {
            System.out.println("\n" + ErrorMessages.FATAL_ERROR.getMessage());
        }
    }
}
