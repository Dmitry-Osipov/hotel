package ui.actions.service;

import essence.service.Service;
import essence.service.ServiceNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import service.ServiceService;
import ui.actions.IAction;
import utils.InputHandler;
import utils.exceptions.EntityContainedException;
import utils.exceptions.ErrorMessages;

import java.sql.SQLException;

/**
 * Класс предоставляет логику выполнения действия по добавлению новой услуги в отель.
 */
@Component
public class AddServiceAction implements IAction {
    private final ServiceService serviceService;

    @Autowired
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
        try {
            System.out.println("\nВыберите название услуги: ");
            ServiceNames[] names = ServiceNames.values();
            for (int i = 0; i < names.length; i++) {
                System.out.println(i+1 + ". " + names[i]);
            }
            ServiceNames name = names[InputHandler.getUserIntegerInput() - 1];

            System.out.println("\nВведите стоимость услуги: ");
            int cost = InputHandler.getUserIntegerInput();

            Service service = new Service();
            service.setName(name);
            service.setPrice(cost);
            serviceService.addService(service);
            System.out.println("\nУдалось добавить услугу");
        } catch (EntityContainedException e) {
            System.out.println("\n" + e.getMessage());
        } catch (SQLException e) {
            System.out.println("\n" + ErrorMessages.FATAL_ERROR.getMessage());
        }
    }
}
