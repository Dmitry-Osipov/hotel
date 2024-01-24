package ui.actions.room;

import service.RoomService;
import ui.actions.IAction;
import ui.utils.ErrorMessages;
import ui.utils.InputHandler;
import ui.utils.printers.RoomsPrinter;
import ui.utils.validators.ArrayDigitsValidator;

import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * Класс предоставляет логику выполнения действия по выводу списка свободных комнат с конкретного времени.
 */
public class GetAvailableRoomsByTimeAction implements IAction {
    private final RoomService roomService;

    /**
     * Класс предоставляет логику выполнения действия по выводу списка свободных комнат с конкретного времени.
     * @param roomService Класс обработки данных по комнатам.
     */
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
        RoomsPrinter.printRooms(roomService.getAvailableRoomsByTime(LocalDateTime.of(
                Integer.parseInt(dateTime[0]),
                Integer.parseInt(dateTime[1]),
                Integer.parseInt(dateTime[2]),
                Integer.parseInt(dateTime[3]),
                Integer.parseInt(dateTime[4])
        )));
    }
}
