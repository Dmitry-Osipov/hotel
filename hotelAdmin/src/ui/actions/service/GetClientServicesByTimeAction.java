package ui.actions.service;

import essence.person.AbstractClient;
import service.ServiceService;
import ui.actions.IAction;
import ui.utils.InputHandler;
import ui.utils.exceptions.NoEntityException;
import ui.utils.printers.ServicesPrinter;

/**
 * Класс предоставляет логику выполнения действия по получению списка услуг, оказанных клиенту, по убыванию времени
 * оказания.
 */
public class GetClientServicesByTimeAction implements IAction {
    private final ServiceService serviceService;

    /**
     * Класс предоставляет логику выполнения действия по получению списка услуг, оказанных клиенту, по убыванию времени
     * оказания.
     * @param serviceService Класс обработки данных по услугам.
     */
    public GetClientServicesByTimeAction(ServiceService serviceService) {
        this.serviceService = serviceService;
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
            AbstractClient client = InputHandler.getClientByInput();
            System.out.println("Список услуг, оказанных клиенту, по убыванию времени оказания: ");
            ServicesPrinter.printServices(serviceService.getClientServicesByTime(client));
        } catch (NoEntityException e) {
            System.out.println("\n" + e.getMessage());
        }
    }
}
