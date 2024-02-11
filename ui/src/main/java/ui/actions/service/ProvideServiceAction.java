package ui.actions.service;

import essence.person.AbstractClient;
import essence.service.AbstractService;
import service.ServiceService;
import ui.actions.IAction;
import utils.InputHandler;
import utils.exceptions.NoEntityException;

/**
 * Класс предоставляет логику выполнения действия по предоставлению услуги клиенту.
 */
public class ProvideServiceAction implements IAction {
    private final ServiceService serviceService;

    /**
     * Класс предоставляет логику выполнения действия по предоставлению услуги клиенту.
     * @param serviceService Класс обработки данных по услугам.
     */
    public ProvideServiceAction(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    /**
     * Метод выполняет действие по предоставлению услуги клиенту. При выполнении действия проверяется наличие клиента и
     * услуги, затем выполняется предоставление услуги клиенту с выводом результата.
     */
    @Override
    public void execute() {
        try {
            AbstractClient client = InputHandler.getClientByInput();
            AbstractService service = InputHandler.getServiceByInput();
            serviceService.provideService(client, service);
            System.out.println("\nУдалось провести услугу");
        } catch (NoEntityException e) {
            System.out.println("\n" + e.getMessage());
        }
    }
}
