package ui.actions.service;

import essence.service.Service;
import essence.service.ServiceNames;
import service.ServiceService;
import ui.actions.IAction;
import ui.utils.InputHandler;
import ui.utils.validators.UniqueIdValidator;

/**
 * Класс предоставляет логику выполнения действия по добавлению новой услуги в отель.
 */
public class AddServiceAction implements IAction {
    private final ServiceService serviceService;

    /**
     * Класс предоставляет логику выполнения действия по добавлению новой услуги в отель.
     * @param serviceService Класс обработки данных по услугам.
     */
    public AddServiceAction(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    /**
     * Метод выполняет действие по добавлению новой услуги в отель. При выполнении действия пользователю предлагается
     * выбрать название услуги из списка и ввести стоимость. Затем происходит добавление услуги, и выводится результат
     * операции.
     */
    @Override
    public void execute() {
        System.out.println("\nВведите уникальное ID услуги: ");
        int id = InputHandler.getUserIntegerInput();
        while (!UniqueIdValidator.validateUniqueId(serviceService.getServices(), id)) {
            System.out.println("\nID услуги должно быть уникальным. Повторите: ");
            id = InputHandler.getUserIntegerInput();
        }

        System.out.println("\nВыберите название услуги: ");
        ServiceNames[] names = ServiceNames.values();
        for (int i = 0; i < names.length; i++) {
            System.out.println(i+1 + ". " + names[i]);
        }
        ServiceNames name = names[InputHandler.getUserIntegerInput() - 1];

        System.out.println("\nВведите стоимость услуги: ");
        int cost = InputHandler.getUserIntegerInput();

        String result = serviceService.addService(new Service(id, name, cost)) ? "Удалось добавить услугу" :
                "Не удалось добавить услугу";
        System.out.println("\n" + result);
    }
}
