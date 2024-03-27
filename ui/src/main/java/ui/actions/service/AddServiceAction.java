package ui.actions.service;

import annotations.annotation.Autowired;
import annotations.annotation.Component;
import essence.Identifiable;
import essence.service.Service;
import essence.service.ServiceNames;
import service.ServiceService;
import ui.actions.IAction;
import utils.InputHandler;
import utils.exceptions.EntityContainedException;
import utils.exceptions.ErrorMessages;
import utils.file.DataPath;
import utils.file.FileAdditionResult;
import utils.file.id.IdFileManager;
import utils.validators.UniqueIdValidator;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Класс предоставляет логику выполнения действия по добавлению новой услуги в отель.
 */
@Component
public class AddServiceAction implements IAction {
    @Autowired
    private ServiceService serviceService;

    /**
     * Метод выполняет действие по добавлению новой услуги в отель. При выполнении действия пользователю предлагается
     * выбрать название услуги из списка и ввести стоимость. Затем происходит добавление услуги, и выводится результат
     * операции.
     */
    @Override
    public void execute() {
        try {
            String path = DataPath.ID_DIRECTORY.getPath() + "service_id.txt";
            int id = IdFileManager.readMaxId(path);
            if (!UniqueIdValidator.validateUniqueId(serviceService.getServices(), id)) {
                id = serviceService.getServices().stream().mapToInt(Identifiable::getId).max().orElse(0) + 1;
            }
            IdFileManager.writeMaxId(path, id + 1);

            System.out.println("\nВыберите название услуги: ");
            ServiceNames[] names = ServiceNames.values();
            for (int i = 0; i < names.length; i++) {
                System.out.println(i+1 + ". " + names[i]);
            }
            ServiceNames name = names[InputHandler.getUserIntegerInput() - 1];

            System.out.println("\nВведите стоимость услуги: ");
            int cost = InputHandler.getUserIntegerInput();

            serviceService.addService(new Service(id, name, cost));
            System.out.println("\nУдалось добавить услугу");
        } catch (IOException e) {
            System.out.println("\n" + FileAdditionResult.FAILURE.getMessage());
        } catch (EntityContainedException e) {
            System.out.println("\n" + e.getMessage());
        } catch (SQLException e) {
            System.out.println("\n" + ErrorMessages.FATAL_ERROR.getMessage());
        }
    }
}
