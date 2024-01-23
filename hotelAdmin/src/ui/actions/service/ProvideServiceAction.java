package ui.actions.service;

import essence.person.AbstractClient;
import essence.service.AbstractService;
import repository.service.ProvidedServicesRepository;
import repository.service.ServiceRepository;
import service.ServiceService;
import ui.actions.IAction;
import ui.utils.ErrorMessages;
import ui.utils.InputHandler;

/**
 * Класс предоставляет логику выполнения действия по предоставлению услуги клиенту.
 */
public class ProvideServiceAction implements IAction {
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
            String result =
                    new ServiceService(ServiceRepository.getInstance(), ProvidedServicesRepository.getInstance())
                            .provideService(client, service) ? "Удалось провести услугу" : "Не удалось провести услугу";
            System.out.println("\n" + result);
        }
    }
}
