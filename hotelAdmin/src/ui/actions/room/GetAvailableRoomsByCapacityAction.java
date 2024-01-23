package ui.actions.room;

import repository.room.RoomRepository;
import repository.room.RoomReservationRepository;
import service.RoomService;
import ui.actions.IAction;
import ui.utils.printers.RoomsPrinter;

/**
 * Класс предоставляет логику выполнения действия по выводу списка свободных комнат в порядке возрастания вместимости.
 */
public class GetAvailableRoomsByCapacityAction implements IAction {
    /**
     * Метод выполняет действие по выводу списка свободных комнат в порядке возрастания вместимости. При выполнении
     * действия выводится список свободных комнат, отсортированный по возрастанию вместимости. Если свободных комнат
     * нет, пользователю выводится соответствующее сообщение.
     */
    @Override
    public void execute() {
        System.out.println("\nСписок свободных комнат по возрастанию вместимости: ");
        RoomsPrinter.printRooms(new RoomService(RoomRepository.getInstance(), RoomReservationRepository.getInstance())
                .availableRoomsByCapacity());
    }
}
