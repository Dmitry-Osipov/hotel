package ui.actions.client;

import annotations.annotation.Autowired;
import annotations.annotation.Component;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import service.ClientService;
import ui.actions.IAction;

/**
 * Класс предоставляет логику выполнения действия по подсчёту количества клиентов.
 */
@Component
@Getter
@Setter
@NoArgsConstructor
public class CountClientsAction implements IAction {
    @Autowired
    private ClientService clientService;

    /**
     * Метод выполняет действие по подсчёту количества клиентов. Пользователю выводится соответствующее сообщение.
     */
    @Override
    public void execute() {
        System.out.println("\nКоличество клиентов - " + clientService.countClients());
    }
}
