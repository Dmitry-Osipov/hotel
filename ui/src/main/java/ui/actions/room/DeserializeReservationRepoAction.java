package ui.actions.room;

import essence.reservation.RoomReservation;
import repository.room.RoomReservationRepository;
import service.RoomService;
import ui.actions.IAction;
import utils.InputHandler;
import utils.exceptions.ErrorMessages;
import utils.file.FileAdditionResult;
import utils.file.serialize.SerializationUtils;

import java.io.IOException;

public class DeserializeReservationRepoAction implements IAction {
    private final RoomService roomService;

    public DeserializeReservationRepoAction(RoomService roomService) {
        this.roomService = roomService;
    }

    @Override
    public void execute() {
        try {
            String path = FileAdditionResult.getSerializeDirectory() + InputHandler.getFileNameFromUser();
            RoomReservationRepository repo = SerializationUtils.deserialize(RoomReservationRepository.class, path);
            for (RoomReservation reservation : repo.getReservations()) {
                if (!roomService.updateReservation(reservation)) {
                    roomService.addReservation(reservation);
                    System.out.println("\nРезервация успешно добавлена");
                } else {
                    System.out.println("\nДанные по резервации успешно обновлены");
                }
            }
            System.out.println("\nДанные десериализованы успешно");
        } catch (IOException e) {
            System.out.println("\n" + ErrorMessages.FILE_ERROR.getMessage());
        }
    }
}
