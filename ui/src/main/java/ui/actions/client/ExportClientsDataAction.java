package ui.actions.client;

import annotations.annotation.Autowired;
import annotations.annotation.Component;
import service.ClientService;
import ui.actions.IAction;
import utils.InputHandler;
import utils.exceptions.ErrorMessages;
import utils.file.DataPath;
import utils.file.FileAdditionResult;
import utils.file.csv.ExportCSV;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Класс представляет собой действие по экспорту данных о клиентах.
 */
@Component
public class ExportClientsDataAction implements IAction {
    @Autowired
    private ClientService clientService;

    /**
     * Метод execute выполняет действие по экспорту данных о клиентах. Пользователю предлагается ввести название файла
     * без расширения. Данные о клиентах экспортируются в указанный файл в формате CSV.
     * Перед экспортом пользователю предоставляется выбор: перезаписать существующий файл или создать новый.
     * Выводится результат каждой операции на консоль. В случае ошибки при экспорте выводится сообщение об ошибке.
     */
    @Override
    public void execute() {
        try {
            String path = DataPath.CSV_DIRECTORY.getPath() + InputHandler.getFileNameFromUser();
            String choice = InputHandler.getUserOverwriteChoice(path + ".csv");
            if (choice.equals("да")) {
                ExportCSV.exportClientsData(path, clientService.getClients());
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
