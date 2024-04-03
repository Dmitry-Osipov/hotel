package ui.actions.client;

import annotations.annotation.Autowired;
import annotations.annotation.Component;
import essence.person.Client;
import service.ClientService;
import ui.actions.IAction;
import utils.InputHandler;
import utils.exceptions.EntityContainedException;
import utils.exceptions.ErrorMessages;
import utils.validators.PhoneNumberValidator;

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
            System.out.println("\nВведите ФИО клиента: ");
            String name = InputHandler.getUserInput();

            System.out.println("\nВведите номер телефона клиента в формате +7(xxx)xxx-xx-xx: ");
            String phone = InputHandler.getUserInput();
            while (!PhoneNumberValidator.validatePhoneNumber(phone)) {
                System.out.println("\n" + ErrorMessages.INCORRECT_INPUT.getMessage());
                phone = InputHandler.getUserInput();
            }

            Client client = new Client();
            client.setFio(name);
            client.setPhoneNumber(phone);
            clientService.addClient(client);
            System.out.println("\nУдалось добавить клиента");
        } catch (EntityContainedException e) {
            System.out.println("\n" + e.getMessage());
        } catch (SQLException e) {
            System.out.println("\n" + ErrorMessages.FATAL_ERROR.getMessage());
        }
    }
}
