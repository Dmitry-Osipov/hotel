package ui.actions.service;

import essence.person.AbstractClient;
import service.ServiceService;
import ui.actions.IAction;
import ui.utils.ErrorMessages;
import ui.utils.InputHandler;
import ui.utils.printers.ServicesPrinter;

/**
 * Класс предоставляет логику выполнения действия по получению списка услуг, оказанных клиенту, по возрастанию цены.
 */
public class GetClientServicesByPriceAction implements IAction {
    private final ServiceService serviceService;

    /**
     * Класс предоставляет логику выполнения действия по получению списка услуг, оказанных клиенту, по возрастанию цены.
     * @param serviceService Класс обработки данных по услугам.
     */
    public GetClientServicesByPriceAction(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    /**
     * Метод выполняет действие по получению списка услуг, оказанных клиенту, по возрастанию цены. При выполнении
     * действия пользователю предлагается выбрать клиента, для которого требуется вывести услуги, и затем выводится
     * список услуг по возрастанию цены, если клиент есть в отеле, иначе выводится сообщение об ошибке.
     */
    @Override
    public void execute() {
        AbstractClient client = InputHandler.getClientByInput();
        if (client == null) {
            System.out.println("\n" + ErrorMessages.NO_CLIENTS.getMessage());
        } else {
            System.out.println("\nСписок услуг, оказанных клиенту, по возрастанию цены: ");
            ServicesPrinter.printServices(serviceService.getClientServicesByPrice(client));
        }
    }
}
