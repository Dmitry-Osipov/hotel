package ui.actions.client;

import repository.client.ClientRepository;
import service.ClientService;
import ui.actions.IAction;
import utils.InputHandler;
import utils.exceptions.ErrorMessages;
import utils.file.FileAdditionResult;
import utils.file.serialize.SerializationUtils;

import java.io.IOException;

public class SerializeClientRepoAction implements IAction {
    private final ClientService clientService;

    public SerializeClientRepoAction(ClientService clientService) {
        this.clientService = clientService;
    }

    @Override
    public void execute() {
        try {
            String path = FileAdditionResult.getSerializeDirectory() + InputHandler.getFileNameFromUser();
            String choice = InputHandler.getUserOverwriteChoice(path + ".json");
            if (choice.equals("да")) {
                ClientRepository repo = clientService.getClientRepository();
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
