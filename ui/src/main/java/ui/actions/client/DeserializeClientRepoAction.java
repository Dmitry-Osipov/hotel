package ui.actions.client;

import essence.person.AbstractClient;
import repository.client.ClientRepository;
import service.ClientService;
import ui.actions.IAction;
import utils.InputHandler;
import utils.exceptions.ErrorMessages;
import utils.file.FileAdditionResult;
import utils.file.serialize.SerializationUtils;

import java.io.IOException;

public class DeserializeClientRepoAction implements IAction {
    private final ClientService clientService;

    public DeserializeClientRepoAction(ClientService clientService) {
        this.clientService = clientService;
    }

    @Override
    public void execute() {
        try {
            String path = FileAdditionResult.getSerializeDirectory() + InputHandler.getFileNameFromUser();
            ClientRepository repo = SerializationUtils.deserialize(ClientRepository.class, path);
            for (AbstractClient client : repo.getClients()) {
                if (!clientService.updateClient(client)) {
                    clientService.addClient(client);
                    System.out.println("\nКлиент успешно добавлен");
                } else {
                    System.out.println("\nДанные по клиенту успешно обновлены");
                }
            }
            System.out.println("\nДанные десериализованы успешно");
        } catch (IOException e) {
            System.out.println("\n" + ErrorMessages.FILE_ERROR.getMessage());
        }
    }
}
