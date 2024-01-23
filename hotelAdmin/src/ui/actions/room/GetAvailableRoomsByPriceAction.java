package ui.actions.room;

import repository.room.RoomRepository;
import repository.room.RoomReservationRepository;
import service.RoomService;
import ui.actions.IAction;
import ui.utils.printers.RoomsPrinter;

/**
 * Класс предоставляет логику выполнения действия по выводу списка свободных комнат в порядке возрастания цены.
 */
public class GetAvailableRoomsByPriceAction implements IAction {
    /**
     * Метод выполняет действие по выводу списка свободных комнат в порядке возрастания цены. При выполнении
     * действия выводится список свободных комнат, отсортированный по возрастанию цены. Если свободных комнат
     * нет, пользователю выводится соответствующее сообщение.
     */
    @Override
    public void execute() {
        System.out.println("\nСписок свободных комнат по возрастанию цены: ");
        RoomsPrinter.printRooms(new RoomService(RoomRepository.getInstance(), RoomReservationRepository.getInstance())
                .availableRoomsByPrice());
    }
}
