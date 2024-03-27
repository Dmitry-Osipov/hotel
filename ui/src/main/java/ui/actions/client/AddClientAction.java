package ui.actions.client;

import annotations.annotation.Autowired;
import annotations.annotation.Component;
import essence.Identifiable;
import essence.person.Client;
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
import java.sql.SQLException;

/**
 * Класс предоставляет логику выполнения действия по добавлению нового клиента.
 */
@Component
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
        try {
            String path = DataPath.ID_DIRECTORY.getPath() + "client_id.txt";
            int id = IdFileManager.readMaxId(path);
            if (!UniqueIdValidator.validateUniqueId(clientService.getClients(), id)) {
                id = clientService.getClients().stream().mapToInt(Identifiable::getId).max().orElse(0) + 1;
            }
            IdFileManager.writeMaxId(path, id + 1);

            System.out.println("\nВведите ФИО клиента: ");
            String name = InputHandler.getUserInput();

            System.out.println("\nВведите номер телефона клиента в формате +7(xxx)xxx-xx-xx: ");
            String phone = InputHandler.getUserInput();
            while (!PhoneNumberValidator.validatePhoneNumber(phone)) {
                System.out.println("\n" + ErrorMessages.INCORRECT_INPUT.getMessage());
                phone = InputHandler.getUserInput();
            }

            clientService.addClient(new Client(id, name, phone));
            System.out.println("\nУдалось добавить клиента");
        } catch (EntityContainedException e) {
            System.out.println("\n" + e.getMessage());
        } catch (SQLException e) {
            System.out.println("\n" + ErrorMessages.FATAL_ERROR.getMessage());
        } catch (IOException e) {
            System.out.println("\n" + FileAdditionResult.FAILURE.getMessage());
        }
    }
}
