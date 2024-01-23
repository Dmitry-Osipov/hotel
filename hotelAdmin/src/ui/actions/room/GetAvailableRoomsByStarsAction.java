package ui.actions.room;

import repository.room.RoomRepository;
import repository.room.RoomReservationRepository;
import service.RoomService;
import ui.actions.IAction;
import ui.utils.printers.RoomsPrinter;

/**
 * Класс предоставляет логику выполнения действия по выводу списка свободных комнат в порядке убывания звёзд.
 */
public class GetAvailableRoomsByStarsAction implements IAction {
    /**
     * Метод выполняет действие по выводу списка свободных комнат в порядке убывания звёзд. При выполнении
     * действия выводится список свободных комнат, отсортированный по убыванию звёзд. Если свободных комнат
     * нет, пользователю выводится соответствующее сообщение.
     */
    @Override
    public void execute() {
        System.out.println("\nСписок свободных комнат по убыванию звёзд: ");
        RoomsPrinter.printRooms(new RoomService(RoomRepository.getInstance(), RoomReservationRepository.getInstance())
                .availableRoomsByStars());
    }
}
