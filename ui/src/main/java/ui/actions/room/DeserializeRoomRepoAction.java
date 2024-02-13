package ui.actions.room;

import essence.room.AbstractRoom;
import repository.room.RoomRepository;
import service.RoomService;
import ui.actions.IAction;
import utils.InputHandler;
import utils.exceptions.ErrorMessages;
import utils.file.FileAdditionResult;
import utils.file.serialize.SerializationUtils;

import java.io.IOException;

public class DeserializeRoomRepoAction implements IAction {
    private final RoomService roomService;

    public DeserializeRoomRepoAction(RoomService roomService) {
        this.roomService = roomService;
    }

    @Override
    public void execute() {
        try {
            String path = FileAdditionResult.getSerializeDirectory() + InputHandler.getFileNameFromUser();
            RoomRepository repo = SerializationUtils.deserialize(RoomRepository.class, path);
            for (AbstractRoom room : repo.getRooms()) {
                if (!roomService.updateRoom(room)) {
                    roomService.addRoom(room);
                    System.out.println("\nКомната успешно добавлена");
                } else {
                    System.out.println("\nДанные по комнате успешно обновлены");
                }
            }
            System.out.println("\nДанные десериализованы успешно");
        } catch (IOException e) {
            System.out.println("\n" + ErrorMessages.FILE_ERROR.getMessage());
        }
    }
}
