package ui.actions.room;

import annotations.annotation.Autowired;
import annotations.annotation.Component;
import essence.Identifiable;
import essence.room.Room;
import service.RoomService;
import ui.actions.IAction;
import utils.InputHandler;
import utils.exceptions.EntityContainedException;
import utils.exceptions.ErrorMessages;
import utils.file.DataPath;
import utils.file.FileAdditionResult;
import utils.file.id.IdFileManager;
import utils.validators.ArrayDigitsValidator;
import utils.validators.UniqueIdValidator;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Класс предоставляет логику выполнения действия по добавлению новой комнаты.
 */
@Component
public class AddRoomAction implements IAction {
    @Autowired
    private RoomService roomService;

    /**
     * Метод выполняет действие по добавлению новой комнаты. При выполнении действия пользователю предлагается ввести
     * номер, вместимость и цену комнаты через пробел. Если данные корректны, создается новая комната и добавляется в
     * репозиторий. Пользователю выводится соответствующий результат операции.
     */
    @Override
    public void execute() {
        try {
            String path = DataPath.ID_DIRECTORY.getPath() + "room_id.txt";
            int id = IdFileManager.readMaxId(path);
            if (!UniqueIdValidator.validateUniqueId(roomService.roomsByStars(), id)) {
                id = roomService.roomsByStars().stream().mapToInt(Identifiable::getId).max().orElse(0) + 1;
            }
            IdFileManager.writeMaxId(path, id + 1);

            System.out.println("\nВведите номер, вместимость и цену комнаты через пробел: ");
            String[] userInput = InputHandler.getUserInput().split(" ");
            while (userInput.length != 3 || !ArrayDigitsValidator.isArrayOfDigits(userInput)) {
                System.out.println("\n" + ErrorMessages.INCORRECT_INPUT.getMessage());
                userInput = InputHandler.getUserInput().split(" ");
            }

            roomService.addRoom(new Room(id, Integer.parseInt(userInput[0]), Integer.parseInt(userInput[1]),
                    Integer.parseInt(userInput[2])));
            System.out.println("\nУдалось добавить комнату");
        } catch (EntityContainedException e) {
            System.out.println("\n" + e.getMessage());
        } catch (SQLException e) {
            System.out.println("\n" + ErrorMessages.FATAL_ERROR.getMessage());
        } catch (IOException e) {
            System.out.println("\n" + FileAdditionResult.FAILURE.getMessage());
        }
    }
}
