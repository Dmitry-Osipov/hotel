package ui.actions.client;

import annotations.annotation.Autowired;
import annotations.annotation.Component;
import essence.person.AbstractClient;
import service.ClientService;
import ui.actions.IAction;
import utils.exceptions.ErrorMessages;

import java.sql.SQLException;
import java.util.List;

/**
 * Класс предоставляет логику выполнения действия по получению списка всех клиентов.
 */
@Component
public class GetClientsAction implements IAction {
    @Autowired
    private ClientService clientService;

    /**
     * Метод выполняет действие по получению списка всех клиентов. При выполнении действия выводится список всех
     * клиентов, если они присутствуют в репозитории, иначе сообщение об ошибке.
     */
    @Override
    public void execute() {
        try {
            List<AbstractClient> clients = clientService.getClients();
            if (clients.isEmpty()) {
                System.out.println("\n" + ErrorMessages.NO_CLIENTS.getMessage());
            } else {
                System.out.println("\nСписок всех клиентов: ");
                for (int i = 0; i < clients.size(); i++) {
                    System.out.println(i + 1 + ". " + clients.get(i));
                }
            }
        } catch (SQLException e) {
            System.out.println("\n" + ErrorMessages.FATAL_ERROR.getMessage());
        }
    }
}
