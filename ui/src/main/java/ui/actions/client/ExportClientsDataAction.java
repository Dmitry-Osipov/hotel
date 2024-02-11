package ui.actions.client;

import service.ClientService;
import ui.actions.IAction;
import utils.InputHandler;
import utils.csv.ExportCSV;
import utils.csv.FileAdditionResult;
import utils.exceptions.ErrorMessages;

import java.io.IOException;

/**
 * Класс представляет собой действие по экспорту данных о клиентах.
 */
public class ExportClientsDataAction implements IAction {
    private final ClientService clientService;

    /**
     * Класс представляет собой действие по экспорту данных о клиентах.
     * @param clientService Сервис для работы с данными о клиентах.
     */
    public ExportClientsDataAction(ClientService clientService) {
        this.clientService = clientService;
    }

    /**
     * Метод execute выполняет действие по экспорту данных о клиентах. Пользователю предлагается ввести название файла
     * без расширения. Данные о клиентах экспортируются в указанный файл в формате CSV.
     * Перед экспортом пользователю предоставляется выбор: перезаписать существующий файл или создать новый.
     * Выводится результат каждой операции на консоль. В случае ошибки при экспорте выводится сообщение об ошибке.
     */
    @Override
    public void execute() {
        try {
            String path = FileAdditionResult.getDataDirectory() + InputHandler.getFileNameFromUser();
            String choice = InputHandler.getUserOverwriteChoice(path);
            if (choice.equals("да")) {
                ExportCSV.exportClientsData(path, clientService.getClients());
                System.out.println("\n" + FileAdditionResult.SUCCESS.getMessage());
            } else {
                System.out.println("\n" + FileAdditionResult.FAILURE.getMessage());
            }
        } catch (IOException e) {
            System.out.println("\n" + ErrorMessages.FILE_ERROR.getMessage());
        }
    }
}
