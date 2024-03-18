package ui.actions.service;

import annotations.annotation.Autowired;
import annotations.annotation.Component;
import essence.person.AbstractClient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import service.ClientService;
import service.ServiceService;
import ui.actions.IAction;
import utils.InputHandler;
import utils.exceptions.NoEntityException;
import utils.printers.ServicesPrinter;

/**
 * Класс предоставляет логику выполнения действия по получению списка услуг, оказанных клиенту, по возрастанию цены.
 */
@Component
@Getter
@Setter
@NoArgsConstructor
public class GetClientServicesByPriceAction implements IAction {
    @Autowired
    private ServiceService serviceService;
    @Autowired
    private ClientService clientService;

    /**
     * Метод выполняет действие по получению списка услуг, оказанных клиенту, по возрастанию цены. При выполнении
     * действия пользователю предлагается выбрать клиента, для которого требуется вывести услуги, и затем выводится
     * список услуг по возрастанию цены, если клиент есть в отеле, иначе выводится сообщение об ошибке.
     */
    @Override
    public void execute() {
        try {
            AbstractClient client = InputHandler.getClientByInput(clientService);
            System.out.println("\nСписок услуг, оказанных клиенту, по возрастанию цены: ");
            ServicesPrinter.printServices(serviceService.getClientServicesByPrice(client));
        } catch (NoEntityException e) {
            System.out.println("\n" + e.getMessage());
        }
    }
}