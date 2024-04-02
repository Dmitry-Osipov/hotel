package service;

import annotations.annotation.Autowired;
import annotations.annotation.Component;
import essence.person.AbstractClient;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.ClientRepository;
import utils.exceptions.EntityContainedException;
import utils.exceptions.TechnicalException;

import java.sql.SQLException;
import java.util.List;

/**
 * Класс отвечает за обработку данных по клиентам.
 */
@Component
@ToString
public class ClientService {
    private static final Logger logger = LoggerFactory.getLogger(ClientService.class);
    @Autowired
    private ClientRepository clientRepository;

    /**
     * Метод добавляет нового клиента в список всех клиентов отеля.
     * @param client Новый клиент.
     * @throws EntityContainedException Ошибка вылетает, когда клиент уже содержится в отеле (невозможно повторно
     * добавить).
     * @throws SQLException если произошла ошибка SQL.
     */
    public void addClient(AbstractClient client) throws EntityContainedException, SQLException {
        logger.info("Вызван метод добавления нового клиента");
        clientRepository.save(client);
        logger.info("Удалось добавить нового клиента");
    }

    /**
     * Метод формирует список клиентов отеля.
     * @return Список.
     * @throws SQLException если произошла ошибка SQL.
     */
    public List<AbstractClient> getClients() throws SQLException {
        logger.info("Вызван метод получения списка клиентов отеля");
        List<AbstractClient> clients = clientRepository.getClients();
        logger.info("Получен список клиентов отеля: {}", clients);
        return clients;
    }

    /**
     * Метод подсчитывает общее число клиентов.
     * @return Количество клиентов.
     * @throws SQLException если произошла ошибка SQL.
     */
    public int countClients() throws SQLException {
        logger.info("Вызван метод подсчёта количества клиентов отеля");
        return clientRepository.getClients().size();
    }

    /**
     * Метод обновляет данные по клиенту.
     * @param client Новые данные клиента, собранные в классе клиента.
     * @return {@code true}, если удалось обновить данные, иначе {@code false}.
     * @throws SQLException если произошла ошибка SQL.
     */
    public boolean updateClient(AbstractClient client) throws SQLException {
        int clientId = client.getId();
        logger.info("Вызван метод обновления клиента с ID {}", clientId);
        try {
            clientRepository.update(client);
            logger.info("Удалось обновить клиента с ID {}", clientId);
            return true;
        } catch (TechnicalException e) {
            logger.error("Не удалось обновить клиента с ID {}", clientId);
            return false;
        }
    }
}
