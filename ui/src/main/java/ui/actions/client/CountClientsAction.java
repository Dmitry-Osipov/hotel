package ui.actions.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import service.ClientService;
import ui.actions.IAction;
import utils.exceptions.ErrorMessages;

import java.sql.SQLException;

/**
 * Класс предоставляет логику выполнения действия по подсчёту количества клиентов.
 */
@Component
public class CountClientsAction implements IAction {
    private final ClientService clientService;

    @Autowired
    public CountClientsAction(ClientService clientService) {
        this.clientService = clientService;
    }

    /**
     * Метод выполняет действие по подсчёту количества клиентов. Пользователю выводится соответствующее сообщение.
     */
    @Override
    public void execute() {
        try {
            System.out.println("\nКоличество клиентов - " + clientService.countClients());
        } catch (SQLException e) {
            System.out.println("\n" + ErrorMessages.FATAL_ERROR.getMessage());
        }
    }
}
