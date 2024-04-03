package ui.actions.service;

import essence.person.AbstractClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import service.ClientService;
import service.ServiceService;
import ui.actions.IAction;
import utils.InputHandler;
import utils.exceptions.ErrorMessages;
import utils.exceptions.NoEntityException;
import utils.printers.ServicesPrinter;

import java.sql.SQLException;

/**
 * Класс предоставляет логику выполнения действия по получению списка услуг, оказанных клиенту, по убыванию времени
 * оказания.
 */
@Component
public class GetClientServicesByTimeAction implements IAction {
    private final ServiceService serviceService;
    private final ClientService clientService;

    @Autowired
    public GetClientServicesByTimeAction(ServiceService serviceService, ClientService clientService) {
        this.serviceService = serviceService;
        this.clientService = clientService;
    }

    /**
     * Метод выполняет действие по получению списка услуг, оказанных клиенту, по убыванию времени оказания. При
     * выполнении действия пользователю предлагается выбрать клиента, для которого требуется вывести услуги, и затем
     * выводится список услуг по убыванию времени оказания, если клиенты есть в отеле, иначе выводится сообщение об
     * ошибке.
     */
    @Override
    public void execute() {
        try {
            AbstractClient client = InputHandler.getClientByInput(clientService);
            System.out.println("Список услуг, оказанных клиенту, по убыванию времени оказания: ");
            ServicesPrinter.printServices(serviceService.getClientServicesByTime(client));
        } catch (NoEntityException e) {
            System.out.println("\n" + e.getMessage());
        } catch (SQLException e) {
            System.out.println("\n" + ErrorMessages.FATAL_ERROR.getMessage());
        }
    }
}
