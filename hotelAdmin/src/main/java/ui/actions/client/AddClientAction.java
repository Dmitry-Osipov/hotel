package ui.actions.client;

import essence.Identifiable;
import essence.person.Client;
import service.ClientService;
import ui.actions.IAction;
import ui.utils.InputHandler;
import ui.utils.csv.FileAdditionResult;
import ui.utils.exceptions.ErrorMessages;
import ui.utils.id.IdFileManager;
import ui.utils.validators.PhoneNumberValidator;
import ui.utils.validators.UniqueIdValidator;

import java.io.IOException;

/**
 * Класс предоставляет логику выполнения действия по добавлению нового клиента.
 */
public class AddClientAction implements IAction {
    private final ClientService clientService;

    /**
     * Класс предоставляет логику выполнения действия по добавлению нового клиента.
     * @param clientService Класс обработки данных по клиентам.
     */
    public AddClientAction(ClientService clientService) {
        this.clientService = clientService;
    }

    /**
     * Метод выполняет действие по добавлению нового клиента. Пользователю предлагается ввести ФИО и номер телефона
     * клиента. В случае успешного добавления клиента в репозиторий выводится соответствующее сообщение, иначе выводится
     * сообщение об ошибке.
     */
    @Override
    public void execute() {
        String path = FileAdditionResult.getIdDirectory() + "client_id.txt";
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

        String result = clientService.addClient(new Client(id, name, phone)) ? "Удалось добавить клиента" :
                "Не удалось добавить клиента";
        System.out.println("\n" + result);
    }
}
