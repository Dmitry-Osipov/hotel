package ui.actions.service;

import com.opencsv.exceptions.CsvValidationException;
import essence.provided.ProvidedService;
import service.ServiceService;
import ui.actions.IAction;
import utils.InputHandler;
import utils.exceptions.ErrorMessages;
import utils.file.DataPath;
import utils.file.csv.ImportCSV;

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
            String path = DataPath.CSV_DIRECTORY.getPath() + InputHandler.getFileNameFromUser();
            List<ProvidedService> providedServices = ImportCSV.importProvidedServicesData(path);
            String result;
            for (ProvidedService providedService : providedServices) {
                int id = providedService.getId();
                boolean updated = serviceService.updateProvidedService(providedService);
                if (updated) {
                    result = "Данные по проведённой услуге с ID " + id + " успешно обновлены";
                } else {
                    serviceService.addProvidedService(providedService);
                    result = "Проведённая услуга с ID " + id + " успешно добавлена";
                }
                System.out.println("\n" + result);
            }
        } catch (IOException | CsvValidationException e) {
            System.out.println("\n" + ErrorMessages.FILE_ERROR.getMessage());
        }
    }
}
