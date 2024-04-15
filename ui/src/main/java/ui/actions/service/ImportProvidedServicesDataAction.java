package ui.actions.service;

import com.opencsv.exceptions.CsvValidationException;
import essence.provided.ProvidedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import service.ServiceService;
import ui.actions.IAction;
import utils.InputHandler;
import utils.exceptions.ErrorMessages;
import utils.exceptions.TechnicalException;
import utils.file.DataPath;
import utils.file.csv.ImportCSV;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Класс представляет собой действие по импорту данных о проведенных услугах.
 */
@Component
public class ImportProvidedServicesDataAction implements IAction {
    private final ServiceService serviceService;

    @Autowired
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
                try {
                    serviceService.updateProvidedService(providedService);
                    result = "Данные по проведённой услуге с ID " + id + " успешно обновлены";
                } catch (TechnicalException e) {
                    serviceService.addProvidedService(providedService);
                    result = "Проведённая услуга с ID " + id + " успешно добавлена";
                }
                System.out.println("\n" + result);
            }
        } catch (IOException | CsvValidationException e) {
            System.out.println("\n" + ErrorMessages.FILE_ERROR.getMessage());
        } catch (SQLException e) {
            System.out.println("\n" + ErrorMessages.FATAL_ERROR.getMessage());
        }
    }
}
