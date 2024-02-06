package ui.actions.room;

import essence.room.AbstractRoom;
import service.RoomService;
import ui.actions.IAction;
import ui.utils.InputHandler;
import ui.utils.exceptions.NoEntityException;

/**
 * Класс предоставляет логику выполнения действия по добавлению звёзд комнате.
 */
public class AddStarsAction implements IAction {
    private final RoomService roomService;

    /**
     * Класс предоставляет логику выполнения действия по добавлению звёзд комнате.
     * @param roomService Класс обработки данных по комнатам.
     */
    public AddStarsAction(RoomService roomService) {
        this.roomService = roomService;
    }

    /**
     * Метод выполняет действие по добавлению звёзд комнате. При выполнении действия пользователю предлагается выбрать
     * комнату, затем ввести количество звёзд. После валидации данных происходит добавление соответствующего количества
     * звёзд к выбранной комнате. Пользователю выводится соответствующий результат операции.
     */
    @Override
    public void execute() {
        try {
            AbstractRoom room = InputHandler.getRoomByInput();
            System.out.println("\nВведите количество звёзд (от 1 до 5): ");
            int stars = InputHandler.getUserIntegerInput();
            String result = roomService.addStarsToRoom(room, stars) ? "Добавление звёзд прошло успешно" :
                    "Введено недопустимое количество звёзд";
            System.out.println("\n" + result);
        } catch (NoEntityException e) {
            System.out.println("\n" + e.getMessage());
        }
    }
}
