package ui.actions.service;

import essence.person.AbstractClient;
import essence.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import service.ClientService;
import service.ServiceService;
import ui.actions.IAction;
import utils.InputHandler;
import utils.exceptions.ErrorMessages;
import utils.exceptions.InvalidDataException;
import utils.exceptions.NoEntityException;
import utils.exceptions.TechnicalException;

import java.sql.SQLException;

/**
 * Класс предоставляет логику выполнения действия по предоставлению услуги клиенту.
 */
@Component
public class ProvideServiceAction implements IAction {
    private final ServiceService serviceService;
    private final ClientService clientService;

    @Autowired
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
        } catch (NoEntityException | TechnicalException e) {
            System.out.println("\n" + e.getMessage());
        } catch (SQLException e) {
            System.out.println("\n" + ErrorMessages.FATAL_ERROR.getMessage());
        } catch (InvalidDataException e) {
            System.out.println("\nНевозможно провести уже проведённую услугу");
        }
    }
}
