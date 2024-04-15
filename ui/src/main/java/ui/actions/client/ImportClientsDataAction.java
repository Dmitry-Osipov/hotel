package ui.actions.client;

import com.opencsv.exceptions.CsvValidationException;
import essence.person.AbstractClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import service.ClientService;
import ui.actions.IAction;
import utils.InputHandler;
import utils.exceptions.EntityContainedException;
import utils.exceptions.ErrorMessages;
import utils.exceptions.TechnicalException;
import utils.file.DataPath;
import utils.file.csv.ImportCSV;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Класс представляет собой действие по импорту данных о клиентах.
 */
@Component
public class ImportClientsDataAction implements IAction {
    private final ClientService clientService;

    @Autowired
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
            String path = DataPath.CSV_DIRECTORY.getPath() + InputHandler.getFileNameFromUser();
            List<AbstractClient> clients = ImportCSV.importClientsData(path);
            String result;
            for (AbstractClient client : clients) {
                int id = client.getId();
                try {
                    clientService.updateClient(client);
                    result = "Данные по клиенту с ID " + id + " успешно обновлены";
                } catch (TechnicalException e) {
                    clientService.addClient(client);
                    result = "Клиент с ID " + id + " успешно добавлен";
                }
                System.out.println("\n" + result);
            }
        } catch (IOException | CsvValidationException e) {
            System.out.println("\n" + ErrorMessages.FILE_ERROR.getMessage());
        } catch (EntityContainedException e) {
            System.out.println("\n" + e.getMessage());
        } catch (SQLException e) {
            System.out.println("\n" + ErrorMessages.FATAL_ERROR.getMessage());
        }
    }
}
