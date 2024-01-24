package ui.actions.client;

import essence.person.Client;
import service.ClientService;
import ui.actions.IAction;
import ui.utils.ErrorMessages;
import ui.utils.InputHandler;
import ui.utils.validators.PhoneNumberValidator;
import ui.utils.validators.UniqueIdValidator;

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
        System.out.println("\nВведите уникальный ID клиента: ");
        int id = InputHandler.getUserIntegerInput();
        while (!UniqueIdValidator.validateUniqueId(clientService.getClients(), id)) {
            System.out.println("\nТакой клиент уже есть. Введите уникальный ID: ");
            id = InputHandler.getUserIntegerInput();
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
