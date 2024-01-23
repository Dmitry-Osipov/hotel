package ui.actions.service;

import repository.service.ProvidedServicesRepository;
import repository.service.ServiceRepository;
import service.ServiceService;
import ui.actions.IAction;
import ui.utils.printers.ServicesPrinter;

/**
 * Класс предоставляет логику выполнения действия по получению списка всех услуг.
 */
public class GetServicesAction implements IAction {
    /**
     * Метод выполняет действие по получению списка всех услуг. При выполнении действия выводится список всех доступных
     * услуг, если они есть, иначе сообщение об их отсутствии.
     */
    @Override
    public void execute() {
        System.out.println("\nСписок всех услуг: ");
        ServicesPrinter.printServices(
                new ServiceService(ServiceRepository.getInstance(), ProvidedServicesRepository.getInstance())
                        .getServices());
    }
}
