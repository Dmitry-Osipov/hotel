package ui.actions.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import service.ServiceService;
import ui.actions.IAction;
import utils.InputHandler;
import utils.exceptions.ErrorMessages;
import utils.file.DataPath;
import utils.file.FileAdditionResult;
import utils.file.csv.ExportCSV;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Класс представляет собой действие по экспорту данных о предоставленных услугах.
 */
@Component
public class ExportProvidedServicesDataAction implements IAction {
    private final ServiceService serviceService;

    @Autowired
    public ExportProvidedServicesDataAction(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    /**
     * Метод execute выполняет действие по экспорту данных о предоставленных услугах. Пользователю предлагается ввести
     * название файла без расширения. Перед экспортом проверяется, не существует ли уже файл с таким именем.
     * Пользователю предоставляется выбор перезаписи файла. В случае согласия происходит экспорт данных в CSV файл.
     * Результат операции выводится на консоль.
     */
    @Override
    public void execute() {
        try {
            String path = DataPath.CSV_DIRECTORY.getPath() + InputHandler.getFileNameFromUser();
            String choice = InputHandler.getUserOverwriteChoice(path + ".csv");
            if (choice.equals("да")) {
                ExportCSV.exportProvidedServicesData(path, serviceService.getProvidedServices());
                System.out.println("\n" + FileAdditionResult.SUCCESS.getMessage());
            } else {
                System.out.println("\n" + FileAdditionResult.FAILURE.getMessage());
            }
        } catch (IOException e) {
            System.out.println("\n" + ErrorMessages.FILE_ERROR.getMessage());
        } catch (SQLException e) {
            System.out.println("\n" + ErrorMessages.FATAL_ERROR.getMessage());
        }
    }
}
