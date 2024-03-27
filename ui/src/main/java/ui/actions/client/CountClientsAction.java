package ui.actions.client;

import annotations.annotation.Autowired;
import annotations.annotation.Component;
import service.ClientService;
import ui.actions.IAction;
import utils.exceptions.ErrorMessages;

import java.sql.SQLException;

/**
 * Класс предоставляет логику выполнения действия по подсчёту количества клиентов.
 */
@Component
public class CountClientsAction implements IAction {
    @Autowired
    private ClientService clientService;

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
