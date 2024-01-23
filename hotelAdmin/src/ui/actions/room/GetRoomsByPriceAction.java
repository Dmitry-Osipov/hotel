package ui.actions.room;

import repository.room.RoomRepository;
import repository.room.RoomReservationRepository;
import service.RoomService;
import ui.actions.IAction;
import ui.utils.printers.RoomsPrinter;

/**
 * Класс предоставляет логику выполнения действия по выводу списка всех комнат по возрастанию цены.
 */
public class GetRoomsByPriceAction implements IAction {
    /**
     * Метод выполняет действие по выводу списка всех комнат по возрастанию цены. При выполнении действия
     * выводится список всех комнат отеля, упорядоченный по возрастанию цены. Если в отеле отсутствуют комнаты,
     * выводится соответствующее сообщение.
     */
    @Override
    public void execute() {
        System.out.println("\nСписок всех комнат по возрастанию цены: ");
        RoomsPrinter.printRooms(new RoomService(RoomRepository.getInstance(), RoomReservationRepository.getInstance())
                .roomsByPrice());
    }
}
