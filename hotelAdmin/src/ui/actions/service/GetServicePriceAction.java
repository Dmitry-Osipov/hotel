package ui.actions.service;

import essence.service.Service;
import repository.service.ProvidedServicesRepository;
import repository.service.ServiceRepository;
import service.ServiceService;
import ui.actions.IAction;
import ui.utils.ErrorMessages;
import ui.utils.InputHandler;


public class GetServicePriceAction implements IAction {
    /**
     * Метод выполняет действие по получению стоимости конкретной услуги. При выполнении действия пользователю
     * предлагается выбрать услугу, для которой требуется узнать стоимость, и затем выводится стоимость выбранной
     * услуги, если услуги есть в отеле, иначе выводится сообщение об ошибке.
     */
    @Override
    public void execute() {
        Service service = (Service) InputHandler.getServiceByInput();
        if (service == null) {
            System.out.println("\n" + ErrorMessages.NO_SERVICES.getMessage());
        } else {
            System.out.println("\nСтоимость услуги - " +
                    new ServiceService(ServiceRepository.getInstance(), ProvidedServicesRepository.getInstance())
                            .getFavorPrice(service));
        }
    }
}
