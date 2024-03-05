package ui.actions.service;

import annotations.annotation.Autowired;
import annotations.annotation.Component;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import service.ServiceService;
import ui.actions.IAction;
import utils.exceptions.NoEntityException;
import utils.printers.ServicesPrinter;

/**
 * Класс предоставляет логику выполнения действия по получению списка всех услуг.
 */
@Component
@Getter
@Setter
@NoArgsConstructor
public class GetServicesAction implements IAction {
    @Autowired
    private ServiceService serviceService;

    /**
     * Класс предоставляет логику выполнения действия по получению списка всех услуг.
     * @param serviceService Класс обработки данных по услугам.
     */
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
        }
    }
}
