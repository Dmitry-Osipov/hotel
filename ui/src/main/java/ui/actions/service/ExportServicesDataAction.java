package ui.actions.service;

import service.ServiceService;
import ui.actions.IAction;
import utils.InputHandler;
import utils.exceptions.ErrorMessages;
import utils.file.FileAdditionResult;
import utils.file.csv.ExportCSV;

import java.io.IOException;

/**
 * Класс представляет собой действие по экспорту данных об услугах.
 */
public class ExportServicesDataAction implements IAction {
    private final ServiceService serviceService;

    /**
     * Класс представляет собой действие по экспорту данных об услугах.
     * @param serviceService Сервис для работы с данными об услугах.
     */
    public ExportServicesDataAction(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    /**
     * Метод execute выполняет действие по экспорту данных об услугах. Пользователю предлагается ввести название файла
     * без расширения. Перед экспортом проверяется, не существует ли уже файл с таким именем. Пользователю
     * предоставляется выбор перезаписи файла. В случае согласия происходит экспорт данных в CSV файл.
     * Результат операции выводится на консоль.
     */
    @Override
    public void execute() {
        try {
            String path = FileAdditionResult.getCsvDirectory() + InputHandler.getFileNameFromUser();
            String choice = InputHandler.getUserOverwriteChoice(path + ".csv");
            if (choice.equals("да")) {
                ExportCSV.exportServicesData(path, serviceService.getServices());
                System.out.println("\n" + FileAdditionResult.SUCCESS.getMessage());
            } else {
                System.out.println("\n" + FileAdditionResult.FAILURE.getMessage());
            }
        } catch (IOException e) {
            System.out.println("\n" + ErrorMessages.FILE_ERROR.getMessage());
        }
    }
}
