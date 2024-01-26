package ui.actions.service;

import com.opencsv.exceptions.CsvValidationException;
import essence.provided.ProvidedService;
import service.ServiceService;
import ui.actions.IAction;
import ui.utils.ErrorMessages;
import ui.utils.InputHandler;
import ui.utils.csv.FileAdditionResult;
import ui.utils.csv.ImportCSV;

import java.io.IOException;
import java.util.List;

/**
 * Класс представляет собой действие по импорту данных о проведенных услугах.
 */
public class ImportProvidedServicesDataAction implements IAction {
    private final ServiceService serviceService;

    /**
     * Класс представляет собой действие по импорту данных о проведенных услугах.
     * @param serviceService Сервис для работы с данными о проведенных услугах.
     */
    public ImportProvidedServicesDataAction(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    /**
     * Метод execute выполняет действие по импорту данных о проведенных услугах.
     * Пользователю предлагается ввести название файла без расширения.
     * Данные считываются из CSV файла, затем обновляются или добавляются в сервис услуг.
     * Результат операции выводится на консоль.
     */
    @Override
    public void execute() {
        try {
            String path = FileAdditionResult.getDataDirectory() + InputHandler.getFileNameFromUser();
            List<ProvidedService> providedServices = ImportCSV.importProvidedServicesData(path);
            String result;
            for (ProvidedService providedService : providedServices) {
                boolean updated = serviceService.updateProvidedService(providedService);
                int id = providedService.getId();
                if (!updated) {
                    boolean added = serviceService.addProvidedService(providedService);
                    if (!added) {
                        result = ErrorMessages.SERVICE_PROVIDED_ADDITION_FAILURE.getMessage() + " с ID " + id;
                    } else {
                        result = "Проведённая услуга с ID " + id + " успешно добавлена";
                    }
                } else {
                    result = "Данные по проведённой услуге с ID " + id + " успешно обновлены";
                }
                System.out.println("\n" + result);
            }
        } catch (IOException | CsvValidationException e) {
            System.out.println("\n" + ErrorMessages.FILE_ERROR.getMessage());
        }
    }
}
