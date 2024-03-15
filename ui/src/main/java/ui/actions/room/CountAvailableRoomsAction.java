package ui.actions.room;

import annotations.annotation.Autowired;
import annotations.annotation.Component;
import service.RoomService;
import ui.actions.IAction;

/**
 * Класс представляет логику выполнения действия подсчёта количества свободных комнат.
 */
@Component
public class CountAvailableRoomsAction implements IAction {
    @Autowired
    private RoomService roomService;

    /**
     * Метод выполняет действие по подсчёту количества свободных комнат.
     * Пользователю выводится соответствующий результат операции.
     */
    @Override
    public void execute() {
        System.out.println("\nКоличество свободных комнат - " + roomService.countAvailableRooms());
    }
}
