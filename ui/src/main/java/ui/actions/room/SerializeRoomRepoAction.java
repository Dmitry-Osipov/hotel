package ui.actions.room;

import repository.room.RoomRepository;
import service.RoomService;
import ui.actions.IAction;
import utils.InputHandler;
import utils.exceptions.ErrorMessages;
import utils.file.FileAdditionResult;
import utils.file.serialize.SerializationUtils;

import java.io.IOException;

public class SerializeRoomRepoAction implements IAction {
    private final RoomService roomService;

    public SerializeRoomRepoAction(RoomService roomService) {
        this.roomService = roomService;
    }

    @Override
    public void execute() {
        try {
            String path = FileAdditionResult.getSerializeDirectory() + InputHandler.getFileNameFromUser();
            String choice = InputHandler.getUserOverwriteChoice(path + ".json");
            if (choice.equals("да")) {
                RoomRepository repo = roomService.getRoomRepository();
                SerializationUtils.serialize(repo, path);
                System.out.println("\n" + FileAdditionResult.SUCCESS.getMessage());
            } else {
                System.out.println("\n" + FileAdditionResult.FAILURE.getMessage());
            }
        } catch (IOException e) {
            System.out.println("\n" + ErrorMessages.FILE_ERROR.getMessage());
        }
    }
}
