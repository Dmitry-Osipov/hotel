package ui.actions.room;

import repository.room.RoomRepository;
import repository.room.RoomReservationRepository;
import service.RoomService;
import ui.actions.IAction;
import ui.utils.printers.RoomsPrinter;

/**
 * Класс предоставляет логику выполнения действия по выводу списка всех комнат по убыванию звёзд.
 */
public class GetRoomsByStarsAction implements IAction {
    /**
     * Метод выполняет действие по выводу списка всех комнат по убыванию звёзд. При выполнении действия
     * выводится список всех комнат отеля, упорядоченный по убыванию звёзд. Если в отеле отсутствуют комнаты,
     * выводится соответствующее сообщение.
     */
    @Override
    public void execute() {
        System.out.println("\nСписок всех комнат по убыванию звёзд: ");
        RoomsPrinter.printRooms(new RoomService(RoomRepository.getInstance(), RoomReservationRepository.getInstance())
                .roomsByStars());
    }
}
