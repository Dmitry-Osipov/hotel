package ui.actions.service;

import annotations.annotation.Autowired;
import annotations.annotation.Component;
import essence.service.Service;
import service.ServiceService;
import ui.actions.IAction;
import utils.InputHandler;
import utils.exceptions.ErrorMessages;
import utils.exceptions.NoEntityException;

import java.sql.SQLException;

/**
 * Класс предоставляет логику получения стоимости услуги.
 */
@Component
public class GetServicePriceAction implements IAction {
    @Autowired
    private ServiceService serviceService;

    /**
     * Метод выполняет действие по получению стоимости конкретной услуги. При выполнении действия пользователю
     * предлагается выбрать услугу, для которой требуется узнать стоимость, и затем выводится стоимость выбранной
     * услуги, если услуги есть в отеле, иначе выводится сообщение об ошибке.
     */
    @Override
    public void execute() {
        try {
            Service service = (Service) InputHandler.getServiceByInput(serviceService);
            System.out.println("\nСтоимость услуги - " + serviceService.getFavorPrice(service));
        } catch (NoEntityException e) {
            System.out.println("\n" + e.getMessage());
        } catch (SQLException e) {
            System.out.println("\n" + ErrorMessages.FATAL_ERROR.getMessage());
        }
    }
}
