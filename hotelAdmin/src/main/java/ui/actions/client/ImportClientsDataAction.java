package ui.actions.client;

import com.opencsv.exceptions.CsvValidationException;
import essence.person.AbstractClient;
import service.ClientService;
import ui.actions.IAction;
import ui.utils.InputHandler;
import ui.utils.csv.FileAdditionResult;
import ui.utils.csv.ImportCSV;
import ui.utils.exceptions.ErrorMessages;

import java.io.IOException;
import java.util.List;

/**
 * Класс представляет собой действие по импорту данных о клиентах.
 */
public class ImportClientsDataAction implements IAction {
    private final ClientService clientService;

    /**
     * Класс представляет собой действие по импорту данных о клиентах.
     * @param clientService Сервис для работы с данными о клиентах.
     */
    public ImportClientsDataAction(ClientService clientService) {
        this.clientService = clientService;
    }

    /**
     * Метод execute выполняет действие по импорту данных о клиентах. Пользователю предлагается ввести название файла
     * без расширения. Данные о клиентах импортируются из указанного файла, после чего происходит обновление или
     * добавление каждого клиента. Выводится результат каждой операции на консоль.
     * В случае ошибки при импорте выводится сообщение об ошибке.
     */
    @Override
    public void execute() {
        try {
            String path = FileAdditionResult.getDataDirectory() + InputHandler.getFileNameFromUser();
            List<AbstractClient> clients = ImportCSV.importClientsData(path);
            String result;
            for (AbstractClient client : clients) {
                boolean updated = clientService.updateClient(client);
                int id = client.getId();
                if (!updated) {
                    boolean added = clientService.addClient(client);
                    if (!added) {
                        result = ErrorMessages.CLIENT_ADDITION_FAILURE.getMessage() + " с ID " + id;
                    } else {
                        result = "Клиент с ID " + id + " успешно добавлен";
                    }
                } else {
                    result = "Данные по клиенту с ID " + id + " успешно обновлены";
                }
                System.out.println("\n" + result);
            }
        } catch (IOException | CsvValidationException e) {
            System.out.println("\n" + ErrorMessages.FILE_ERROR.getMessage());
        }
    }
}
