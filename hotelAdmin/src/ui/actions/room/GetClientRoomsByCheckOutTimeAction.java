package ui.actions.room;

import essence.person.AbstractClient;
import repository.room.RoomRepository;
import repository.room.RoomReservationRepository;
import service.RoomService;
import ui.actions.IAction;
import ui.utils.ErrorMessages;
import ui.utils.InputHandler;
import ui.utils.printers.RoomsPrinter;

/**
 * Класс предоставляет логику выполнения действия по выводу списка комнат клиента по убыванию времени выезда.
 */
public class GetClientRoomsByCheckOutTimeAction implements IAction {
    /**
     * Метод выполняет действие по выводу списка комнат клиента по убыванию времени выезда. При выполнении действия
     * выводится список комнат клиента, отсортированный по убыванию времени выезда. Если клиентов в отеле нет, выводится
     * соответствующее сообщение.
     */
    @Override
    public void execute() {
        AbstractClient client = InputHandler.getClientByInput();
        if (client == null) {
            System.out.println("\n" + ErrorMessages.NO_CLIENTS.getMessage());
        } else {
            System.out.println("\nСписок всех комнат клиента по убыванию времени выезда: ");
            RoomsPrinter.printRooms(
                    new RoomService(RoomRepository.getInstance(), RoomReservationRepository.getInstance())
                            .getClientRoomsByCheckOutTime(client));
        }
    }
}
