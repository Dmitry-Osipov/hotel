package ui.actions.service;

import annotations.annotation.Autowired;
import annotations.annotation.Component;
import essence.person.AbstractClient;
import essence.service.AbstractService;
import service.ClientService;
import service.ServiceService;
import ui.actions.IAction;
import utils.InputHandler;
import utils.exceptions.ErrorMessages;
import utils.exceptions.InvalidDataException;
import utils.exceptions.NoEntityException;

import java.sql.SQLException;

/**
 * Класс предоставляет логику выполнения действия по предоставлению услуги клиенту.
 */
@Component
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
        } catch (SQLException e) {
            System.out.println("\n" + ErrorMessages.FATAL_ERROR.getMessage());
        } catch (InvalidDataException e) {
            System.out.println("\nНевозможно провести уже проведённую услугу");
        }
    }
}
