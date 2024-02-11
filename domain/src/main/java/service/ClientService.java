package service;

import essence.person.AbstractClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.client.ClientRepository;
import utils.exceptions.EntityContainedException;
import utils.exceptions.ErrorMessages;

import java.util.List;

/**
 * Класс отвечает за обработку данных по клиентам.
 */
public class ClientService {
    private static final Logger logger = LoggerFactory.getLogger(ClientService.class);
    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    /**
     * Метод добавляет нового клиента в список всех клиентов отеля.
     * @param client Новый клиент.
     * @throws EntityContainedException Ошибка вылетает, когда клиент уже содержится в отеле (невозможно повторно
     * добавить).
     */
    public void addClient(AbstractClient client) throws EntityContainedException {
        int clientId = client.getId();
        logger.info("Вызван метод добавления нового клиента с ID {}", clientId);
        boolean added = clientRepository.getClients().add(client);

        if (!added) {
            logger.error("Не удалось добавить нового клиента с ID {}", clientId);
            throw new EntityContainedException(ErrorMessages.CLIENT_CONTAINED.getMessage());
        }

        logger.info("Удалось добавить нового клиента с ID {}", clientId);
    }

    /**
     * Метод формирует список клиентов отеля.
     * @return Список.
     */
    public List<AbstractClient> getClients() {
        logger.info("Вызван метод получения списка клиентов отеля");
        List<AbstractClient> clients = clientRepository.getClients().stream().toList();
        logger.info("Получен список клиентов отеля: {}", clients);
        return clients;
    }

    /**
     * Метод подсчитывает общее число клиентов.
     * @return Количество клиентов.
     */
    public int countClients() {
        logger.info("Вызван метод подсчёта количества клиентов отеля");
        return clientRepository.getClients().size();
    }

    /**
     * Метод обновляет данные по клиенту.
     * @param client Новые данные клиента, собранные в классе клиента.
     * @return {@code true}, если удалось обновить данные, иначе {@code false}.
     */
    public boolean updateClient(AbstractClient client) {
        int clientId = client.getId();
        logger.info("Вызван метод обновления клиента с ID {}", clientId);
        for (AbstractClient currentClient : clientRepository.getClients()) {
            if (currentClient.getId() == clientId) {
                currentClient.setFio(client.getFio());
                currentClient.setPhoneNumber(client.getPhoneNumber());
                currentClient.setCheckInTime(client.getCheckInTime());
                currentClient.setCheckOutTime(client.getCheckOutTime());
                logger.info("Удалось обновить клиента с ID {}", clientId);

                return true;
            }
        }

        logger.error("Не удалось обновить клиента с ID {}", clientId);
        return false;
    }
}
