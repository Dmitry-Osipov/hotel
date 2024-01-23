package ui.actions.service;

import essence.person.AbstractClient;
import repository.service.ProvidedServicesRepository;
import repository.service.ServiceRepository;
import service.ServiceService;
import ui.actions.IAction;
import ui.utils.ErrorMessages;
import ui.utils.InputHandler;
import ui.utils.printers.ServicesPrinter;

/**
 * Класс предоставляет логику выполнения действия по получению списка услуг, оказанных клиенту, по убыванию времени
 * оказания.
 */
public class GetClientServicesByTimeAction implements IAction {
    /**
     * Метод выполняет действие по получению списка услуг, оказанных клиенту, по убыванию времени оказания. При
     * выполнении действия пользователю предлагается выбрать клиента, для которого требуется вывести услуги, и затем
     * выводится список услуг по убыванию времени оказания, если клиенты есть в отеле, иначе выводится сообщение об
     * ошибке.
     */
    @Override
    public void execute() {
        AbstractClient client = InputHandler.getClientByInput();
        if (client == null) {
            System.out.println("\n" + ErrorMessages.NO_CLIENTS.getMessage());
        } else {
            System.out.println("Список услуг, оказанных клиенту, по убыванию времени оказания: ");
            ServicesPrinter.printServices(
                    new ServiceService(ServiceRepository.getInstance(), ProvidedServicesRepository.getInstance())
                            .getClientServicesByTime(client));
        }
    }
}
