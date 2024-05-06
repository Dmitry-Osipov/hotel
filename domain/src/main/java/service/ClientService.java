package service;

import essence.person.AbstractClient;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.ClientRepository;
import utils.exceptions.EntityContainedException;
import utils.exceptions.TechnicalException;

import java.sql.SQLException;
import java.util.List;

/**
 * Класс отвечает за обработку данных по клиентам.
 */
@Service
@ToString
public class ClientService {
    private static final Logger logger = LoggerFactory.getLogger(ClientService.class);
    private final ClientRepository clientRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

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
     * @throws SQLException если произошла ошибка SQL.
     * @throws TechnicalException если произошла ошибка обновления клиента.
     */
    public void updateClient(AbstractClient client) throws SQLException {
        int clientId = client.getId();
        logger.info("Вызван метод обновления клиента с ID {}", clientId);
        try {
            clientRepository.update(client);
            logger.info("Удалось обновить клиента с ID {}", clientId);
        } catch (TechnicalException e) {
            logger.error("Не удалось обновить клиента с ID {}", clientId);
            throw e;
        }
    }

    /**
     * Импортирует список клиентов.
     * Метод выполняет импорт списка клиентов, обновляя существующих клиентов или добавляя новые.
     * @param clients список клиентов для импорта.
     * @throws SQLException если возникает ошибка при работе с базой данных.
     */
    public void importClients(List<AbstractClient> clients) throws SQLException {
        logger.info("Вызван метод импорта клиентов");
        for (AbstractClient client : clients) {
            try {
                updateClient(client);
            } catch (TechnicalException e) {
                addClient(client);
            }
        }
        logger.info("Клиенты импортированы успешно");
    }

    /**
     * Получает клиента из репозитория по указанному идентификатору.
     * @param clientId Уникальный идентификатор клиента.
     * @return Клиент с указанным идентификатором.
     * @throws SQLException Если возникает ошибка доступа к базе данных.
     * @throws TechnicalException Если возникает техническая ошибка при выполнении операции.
     */
    public AbstractClient getClientById(int clientId) throws SQLException {
        logger.info("Вызван метод получения клиента по ID {}", clientId);
        try {
            AbstractClient client = clientRepository.getClientById(clientId);
            logger.info("Удалось получить клиента по ID {}", clientId);
            return client;
        } catch (TechnicalException e) {
            logger.error("Не удалось получить клиента по ID {}", clientId);
            throw e;
        }
    }

    /**
     * Удаляет клиента из базы данных.
     * @param client Клиент, который должен быть удален.
     * @throws SQLException Если возникает ошибка доступа к базе данных.
     * @throws TechnicalException Если возникает техническая ошибка при выполнении операции.
     */
    public void deleteClient(AbstractClient client) throws SQLException {
        int clientId = client.getId();
        logger.info("Вызван метод удаления клиента с ID {}", clientId);
        try {
            clientRepository.deleteClient(client);
            logger.info("Удалось удалить клиента с ID {}", clientId);
        } catch (TechnicalException e) {
            logger.error("Не удалось удалить клиента с ID {}", clientId);
            throw e;
        }
    }
}
