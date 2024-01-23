package ui.actions.client;

import essence.person.AbstractClient;
import repository.client.ClientRepository;
import service.ClientService;
import ui.actions.IAction;
import ui.utils.ErrorMessages;

import java.util.List;

/**
 * Класс предоставляет логику выполнения действия по получению списка всех клиентов.
 */
public class GetClientsAction implements IAction {
    /**
     * Метод выполняет действие по получению списка всех клиентов. При выполнении действия выводится список всех
     * клиентов, если они присутствуют в репозитории, иначе сообщение об ошибке.
     */
    @Override
    public void execute() {
        List<AbstractClient> clients = new ClientService(ClientRepository.getInstance()).getClients();
        if (clients.isEmpty()) {
            System.out.println("\n" + ErrorMessages.NO_CLIENTS.getMessage());
        } else {
            System.out.println("\nСписок всех клиентов: ");
            for (int i = 0; i < clients.size(); i++) {
                System.out.println(i + 1 + ". " + clients.get(i));
            }
        }
    }
}
