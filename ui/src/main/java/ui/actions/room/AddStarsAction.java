package ui.actions.room;

import annotations.annotation.Autowired;
import annotations.annotation.Component;
import essence.room.AbstractRoom;
import service.RoomService;
import ui.actions.IAction;
import utils.InputHandler;
import utils.exceptions.ErrorMessages;
import utils.exceptions.InvalidDataException;
import utils.exceptions.NoEntityException;

import java.sql.SQLException;

/**
 * Класс предоставляет логику выполнения действия по добавлению звёзд комнате.
 */
@Component
public class AddStarsAction implements IAction {
    @Autowired
    private RoomService roomService;

    /**
     * Метод выполняет действие по добавлению звёзд комнате. При выполнении действия пользователю предлагается выбрать
     * комнату, затем ввести количество звёзд. После валидации данных происходит добавление соответствующего количества
     * звёзд к выбранной комнате. Пользователю выводится соответствующий результат операции.
     */
    @Override
    public void execute() {
        try {
            AbstractRoom room = InputHandler.getRoomByInput(roomService);
            System.out.println("\nВведите количество звёзд (от 1 до 5): ");
            int stars = InputHandler.getUserIntegerInput();
            roomService.addStarsToRoom(room, stars);
            System.out.println("\nДобавление звёзд прошло успешно");
        } catch (NoEntityException | InvalidDataException e) {
            System.out.println("\n" + e.getMessage());
        } catch (SQLException e) {
            System.out.println("\n" + ErrorMessages.FATAL_ERROR.getMessage());
        }
    }
}
