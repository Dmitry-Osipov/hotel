package ui.actions.room;

import essence.room.AbstractRoom;
import repository.room.RoomRepository;
import repository.room.RoomReservationRepository;
import service.RoomService;
import ui.actions.IAction;
import ui.utils.ErrorMessages;
import ui.utils.InputHandler;

/**
 * Класс предоставляет логику выполнения действия по добавлению звёзд комнате.
 */
public class AddStarsAction implements IAction {
    /**
     * Метод выполняет действие по добавлению звёзд комнате. При выполнении действия пользователю предлагается выбрать
     * комнату, затем ввести количество звёзд. После валидации данных происходит добавление соответствующего количества
     * звёзд к выбранной комнате. Пользователю выводится соответствующий результат операции.
     */
    @Override
    public void execute() {
        AbstractRoom room = InputHandler.getRoomByInput();

        String result;
        if (room != null) {
            System.out.println("\nВведите количество звёзд (от 1 до 5): ");
            int stars = InputHandler.getUserIntegerInput();

            result = new RoomService(RoomRepository.getInstance(), RoomReservationRepository.getInstance())
                    .addStarsToRoom(room, stars) ? "Добавление звёзд прошло успешно" :
                    "Введено недопустимое количество звёзд";
        } else {
            result = ErrorMessages.NO_ROOMS.getMessage();
        }

        System.out.println("\n" + result);
    }
}
