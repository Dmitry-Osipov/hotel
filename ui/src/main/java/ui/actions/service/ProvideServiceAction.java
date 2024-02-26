package ui.actions.service;

import essence.person.AbstractClient;
import essence.service.AbstractService;
import service.ClientService;
import service.ServiceService;
import ui.actions.IAction;
import utils.InputHandler;
import utils.exceptions.NoEntityException;

/**
 * Класс предоставляет логику выполнения действия по предоставлению услуги клиенту.
 */
public class ProvideServiceAction implements IAction {
    private final ServiceService serviceService;
    private final ClientService clientService;

    /**
     * Класс предоставляет логику выполнения действия по предоставлению услуги клиенту.
     * @param serviceService Класс обработки данных по услугам.
     */
    public ProvideServiceAction(ServiceService serviceService, ClientService clientService) {
        this.serviceService = serviceService;
        this.clientService = clientService;
    }

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
