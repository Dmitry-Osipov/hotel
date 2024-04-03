package ui.actions.room;

import essence.room.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import service.RoomService;
import ui.actions.IAction;
import utils.InputHandler;
import utils.exceptions.EntityContainedException;
import utils.exceptions.ErrorMessages;
import utils.validators.ArrayDigitsValidator;

import java.sql.SQLException;

/**
 * Класс предоставляет логику выполнения действия по добавлению новой комнаты.
 */
@Component
public class AddRoomAction implements IAction {
    private final RoomService roomService;

    @Autowired
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
        try {
            System.out.println("\nВведите номер, вместимость и цену комнаты через пробел: ");
            String[] userInput = InputHandler.getUserInput().split(" ");
            while (userInput.length != 3 || !ArrayDigitsValidator.isArrayOfDigits(userInput)) {
                System.out.println("\n" + ErrorMessages.INCORRECT_INPUT.getMessage());
                userInput = InputHandler.getUserInput().split(" ");
            }

            Room room = new Room();
            room.setNumber(Integer.parseInt(userInput[0]));
            room.setCapacity(Integer.parseInt(userInput[1]));
            room.setPrice(Integer.parseInt(userInput[2]));
            roomService.addRoom(room);
            System.out.println("\nУдалось добавить комнату");
        } catch (EntityContainedException e) {
            System.out.println("\n" + e.getMessage());
        } catch (SQLException e) {
            System.out.println("\n" + ErrorMessages.FATAL_ERROR.getMessage());
        }
    }
}
