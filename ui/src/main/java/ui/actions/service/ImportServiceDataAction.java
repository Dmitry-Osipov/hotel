package ui.actions.service;

import com.opencsv.exceptions.CsvValidationException;
import essence.service.AbstractService;
import service.ServiceService;
import ui.actions.IAction;
import utils.InputHandler;
import utils.csv.FileAdditionResult;
import utils.csv.ImportCSV;
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
                boolean updated = serviceService.updateService(service);
                int id = service.getId();
                if (!updated) {
                    boolean added = serviceService.addService(service);
                    if (!added) {
                        result = ErrorMessages.SERVICE_ADDITION_FAILURE.getMessage() + " с ID " + id;
                    } else {
                        result = "Услуга с ID " + id + " успешно добавлена";
                    }
                } else {
                    result = "Данные по услуге с ID " + id + " успешно обновлены";
                }
                System.out.println("\n" + result);
            }
        } catch (IOException | CsvValidationException e) {
            System.out.println("\n" + ErrorMessages.FILE_ERROR.getMessage());
        }
    }
}
