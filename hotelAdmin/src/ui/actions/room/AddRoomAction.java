package ui.actions.room;

import essence.room.Room;
import service.RoomService;
import ui.actions.IAction;
import ui.utils.ErrorMessages;
import ui.utils.InputHandler;
import ui.utils.validators.ArrayDigitsValidator;
import ui.utils.validators.UniqueIdValidator;

/**
 * Класс предоставляет логику выполнения действияпо добавлению новой комнаты.
 */
public class AddRoomAction implements IAction {
    private final RoomService roomService;

    /**
     * Класс предоставляет логику выполнения действияпо добавлению новой комнаты.
     * @param roomService Класс обарботки данных по комнатам.
     */
    public AddRoomAction(RoomService roomService) {
        this.roomService = roomService;
    }

    /**
     * Метод выполняет действие по добавлению новой комнаты. При выполнении действия пользователю предлагается ввести
     * номер, вместимость и цену комнаты через пробел. Если данные корректны, создается новая комната и добавляется в
     * репозиторий. Пользователю выводится соответствующий результат операции.
     */
    @Override
    public void execute() {
        System.out.println("\nВведите id, номер, вместимость и цену комнаты через пробел: ");
        String[] userInput = InputHandler.getUserInput().split(" ");
        while (userInput.length != 4 || !ArrayDigitsValidator.isArrayOfDigits(userInput)
                || !UniqueIdValidator.validateUniqueId(roomService.roomsByStars(), Integer.parseInt(userInput[0]))) {
            System.out.println("\n" + ErrorMessages.INCORRECT_INPUT.getMessage());
            userInput = InputHandler.getUserInput().split(" ");
        }

        String result = roomService.addRoom(new Room(
                Integer.parseInt(userInput[0]),
                Integer.parseInt(userInput[1]),
                Integer.parseInt(userInput[2]),
                Integer.parseInt(userInput[3]))) ? "Удалось добавить комнату" : "Не удалось добавить комнату";
        System.out.println("\n" + result);
    }
}
