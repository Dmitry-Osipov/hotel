package ui.actions.service;

import essence.person.AbstractClient;
import essence.service.AbstractService;
import service.ServiceService;
import ui.actions.IAction;
import ui.utils.ErrorMessages;
import ui.utils.InputHandler;

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
        AbstractClient client = InputHandler.getClientByInput();
        AbstractService service = InputHandler.getServiceByInput();
        if (client == null) {
            System.out.println("\n" + ErrorMessages.NO_CLIENTS.getMessage());
        } else if (service == null) {
            System.out.println("\n" + ErrorMessages.NO_SERVICES.getMessage());
        } else {
            int id = InputHandler.getUserIntegerInput();
            String result = serviceService.provideService(id, client, service) ? "Удалось провести услугу" :
                    "Не удалось провести услугу";
            System.out.println("\n" + result);
        }
    }
}
