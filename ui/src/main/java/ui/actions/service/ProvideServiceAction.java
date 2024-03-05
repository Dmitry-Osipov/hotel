package ui.actions.service;

import annotations.annotation.Autowired;
import annotations.annotation.Component;
import essence.person.AbstractClient;
import essence.service.AbstractService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import service.ClientService;
import service.ServiceService;
import ui.actions.IAction;
import utils.InputHandler;
import utils.exceptions.NoEntityException;

/**
 * Класс предоставляет логику выполнения действия по предоставлению услуги клиенту.
 */
@Component
@Getter
@Setter
@NoArgsConstructor
public class ProvideServiceAction implements IAction {
    @Autowired
    private ServiceService serviceService;
    @Autowired
    private ClientService clientService;

    /**
     * Метод выполняет действие по предоставлению услуги клиенту. При выполнении действия проверяется наличие клиента и
     * услуги, затем выполняется предоставление услуги клиенту с выводом результата.
     */
    @Override
    public void execute() {
        try {
            AbstractClient client = InputHandler.getClientByInput(clientService);
            AbstractService service = InputHandler.getServiceByInput(serviceService);
            serviceService.provideService(client, service);
            System.out.println("\nУдалось провести услугу");
        } catch (NoEntityException e) {
            System.out.println("\n" + e.getMessage());
        }
    }
}
