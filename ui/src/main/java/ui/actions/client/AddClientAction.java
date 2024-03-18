package ui.actions.client;

import annotations.annotation.Autowired;
import annotations.annotation.Component;
import essence.Identifiable;
import essence.person.Client;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import service.ClientService;
import ui.actions.IAction;
import utils.InputHandler;
import utils.exceptions.EntityContainedException;
import utils.exceptions.ErrorMessages;
import utils.file.DataPath;
import utils.file.FileAdditionResult;
import utils.file.id.IdFileManager;
import utils.validators.PhoneNumberValidator;
import utils.validators.UniqueIdValidator;

import java.io.IOException;

/**
 * Класс предоставляет логику выполнения действия по добавлению нового клиента.
 */
@Component
@Getter
@Setter
@NoArgsConstructor
public class AddClientAction implements IAction {
    @Autowired
    private ClientService clientService;

    /**
     * Метод выполняет действие по добавлению нового клиента. Пользователю предлагается ввести ФИО и номер телефона
     * клиента. В случае успешного добавления клиента в репозиторий выводится соответствующее сообщение, иначе выводится
     * сообщение об ошибке.
     */
    @Override
    public void execute() {
        String path = DataPath.ID_DIRECTORY.getPath() + "client_id.txt";
        int id = IdFileManager.readMaxId(path);
        if (!UniqueIdValidator.validateUniqueId(clientService.getClients(), id)) {
            id = clientService.getClients().stream().mapToInt(Identifiable::getId).max().orElse(0) + 1;
        }
        try {
            IdFileManager.writeMaxId(path, id + 1);
        } catch (IOException e) {
            System.out.println("\n" + FileAdditionResult.FAILURE.getMessage());
        }

        System.out.println("\nВведите ФИО клиента: ");
        String name = InputHandler.getUserInput();

        System.out.println("\nВведите номер телефона клиента в формате +7(xxx)xxx-xx-xx: ");
        String phone = InputHandler.getUserInput();
        while (!PhoneNumberValidator.validatePhoneNumber(phone)) {
            System.out.println("\n" + ErrorMessages.INCORRECT_INPUT.getMessage());
            phone = InputHandler.getUserInput();
        }

        try {
            clientService.addClient(new Client(id, name, phone));
            System.out.println("\nУдалось добавить клиента");
        } catch (EntityContainedException e) {
            System.out.println("\n" + e.getMessage());
        }
    }
}