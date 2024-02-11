package ui.actions.service;

import com.opencsv.exceptions.CsvValidationException;
import essence.service.AbstractService;
import service.ServiceService;
import ui.actions.IAction;
import utils.InputHandler;
import utils.csv.FileAdditionResult;
import utils.csv.ImportCSV;
import utils.exceptions.EntityContainedException;
import utils.exceptions.ErrorMessages;

import java.io.IOException;
import java.util.List;

/**
 * Класс представляет собой действие по импорту данных по услугам.
 */
public class ImportServiceDataAction implements IAction {
    private final ServiceService serviceService;

    /**
     * Класс представляет собой действие по импорту данных по услугам.
     * @param serviceService Сервис для работы с данными по услугам.
     */
    public ImportServiceDataAction(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    /**
     * Метод execute выполняет действие по импорту данных по услугам.
     * Пользователю предлагается ввести название файла без расширения.
     * Данные считываются из CSV файла, затем обновляются или добавляются в сервис услуг.
     * Результат операции выводится на консоль.
     */
    @Override
    public void execute() {
        try {
            String path = FileAdditionResult.getDataDirectory() + InputHandler.getFileNameFromUser();
            List<AbstractService> services = ImportCSV.importServicesData(path);
            String result;
            for (AbstractService service : services) {
                int id = service.getId();
                boolean updated = serviceService.updateService(service);
                if (updated) {
                    result = "Данные по услуге с ID " + id + " успешно обновлены";
                } else {
                    serviceService.addService(service);
                    result = "Услуга с ID " + id + " успешно добавлена";
                }
                System.out.println("\n" + result);
            }
        } catch (IOException | CsvValidationException e) {
            System.out.println("\n" + ErrorMessages.FILE_ERROR.getMessage());
        } catch (EntityContainedException e) {
            System.out.println("\n" + e.getMessage());
        }
    }
}
