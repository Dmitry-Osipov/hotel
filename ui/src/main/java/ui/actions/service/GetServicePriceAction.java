package ui.actions.service;

import essence.service.Service;
import service.ServiceService;
import ui.actions.IAction;
import utils.InputHandler;
import utils.exceptions.NoEntityException;

/**
 * Класс предоставляет логику получения стоимости услуги.
 */
public class GetServicePriceAction implements IAction {
    private final ServiceService serviceService;

    /**
     * Класс предоставляет логику получения стоимости услуги.
     * @param serviceService Класс обработки данных по услугам.
     */
    public GetServicePriceAction(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    /**
     * Метод выполняет действие по получению стоимости конкретной услуги. При выполнении действия пользователю
     * предлагается выбрать услугу, для которой требуется узнать стоимость, и затем выводится стоимость выбранной
     * услуги, если услуги есть в отеле, иначе выводится сообщение об ошибке.
     */
    @Override
    public void execute() {
        try {
            Service service = (Service) InputHandler.getServiceByInput();
            System.out.println("\nСтоимость услуги - " + serviceService.getFavorPrice(service));
        } catch (NoEntityException e) {
            System.out.println("\n" + e.getMessage());
        }
    }
}
