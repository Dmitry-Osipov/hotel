package ui.actions.client;

import annotations.annotation.Autowired;
import annotations.annotation.Component;
import essence.person.AbstractClient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import service.ClientService;
import ui.actions.IAction;
import utils.exceptions.ErrorMessages;

import java.util.List;

/**
 * Класс предоставляет логику выполнения действия по получению списка всех клиентов.
 */
@Component
@Getter
@Setter
@NoArgsConstructor
public class GetClientsAction implements IAction {
    @Autowired
    private ClientService clientService;

    /**
     * Класс предоставляет логику выполнения действия по получению списка всех клиентов.
     * @param clientService Класс обработки данных по клиентам.
     */
    public GetClientsAction(ClientService clientService) {
        this.clientService = clientService;
    }

    /**
     * Метод выполняет действие по получению списка всех клиентов. При выполнении действия выводится список всех
     * клиентов, если они присутствуют в репозитории, иначе сообщение об ошибке.
     */
    @Override
    public void execute() {
        List<AbstractClient> clients = clientService.getClients();
        if (clients.isEmpty()) {
            System.out.println("\n" + ErrorMessages.NO_CLIENTS.getMessage());
        } else {
            System.out.println("\nСписок всех клиентов: ");
            for (int i = 0; i < clients.size(); i++) {
                System.out.println(i+1 + ". " + clients.get(i));
            }
        }
    }
}
