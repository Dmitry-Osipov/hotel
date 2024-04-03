package ui.actions.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import service.ServiceService;
import ui.actions.IAction;
import utils.exceptions.ErrorMessages;
import utils.exceptions.NoEntityException;
import utils.printers.ServicesPrinter;

import java.sql.SQLException;

/**
 * Класс предоставляет логику выполнения действия по получению списка всех услуг.
 */
@Component
public class GetServicesAction implements IAction {
    private final ServiceService serviceService;

    @Autowired
    public GetServicesAction(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    /**
     * Метод выполняет действие по получению списка всех услуг. При выполнении действия выводится список всех доступных
     * услуг, если они есть, иначе сообщение об их отсутствии.
     */
    @Override
    public void execute() {
        try {
            System.out.println("\nСписок всех услуг: ");
            ServicesPrinter.printServices(serviceService.getServices());
        } catch (NoEntityException e) {
            System.out.println("\n" + e.getMessage());
        } catch (SQLException e) {
            System.out.println("\n" + ErrorMessages.FATAL_ERROR.getMessage());
        }
    }
}
