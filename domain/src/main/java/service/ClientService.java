package service;

import annotations.annotation.Autowired;
import annotations.annotation.Component;
import essence.person.AbstractClient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.ClientRepository;
import utils.exceptions.EntityContainedException;
import utils.exceptions.ErrorMessages;
import utils.file.DataPath;
import utils.file.serialize.SerializationUtils;

import java.io.IOException;
import java.util.List;

/**
 * Класс отвечает за обработку данных по клиентам.
 */
@Component
@Getter
@Setter
@NoArgsConstructor
@ToString
public class ClientService {
    private static final Logger logger = LoggerFactory.getLogger(ClientService.class);
    @Autowired
    private ClientRepository clientRepository;

    @Deprecated(forRemoval = true)
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

    /**
     * Метод производит сериализацию данных по клиентам.
     */
    public void serializeClientsData() {
        logger.info("Происходит сериализация данных по клиентам");
        try {
            String path = DataPath.SERIALIZE_DIRECTORY.getPath() + "clients";
            SerializationUtils.serialize(clientRepository, path);
        } catch (IOException e) {
            logger.error("Сериализация данных по клиентам не произошла");
        }
        logger.info("Сериализация данных по клиентам завершена");
    }

    /**
     * Метод производит десериализацию данных по клиентам.
     */
    public void deserializeClientsData() {
        logger.info("Происходит десериализация данных по клиентам");
        try {
            String path = DataPath.SERIALIZE_DIRECTORY.getPath() + "clients";
            ClientRepository repo = SerializationUtils.deserialize(ClientRepository.class, path);
            for (AbstractClient client : repo.getClients()) {
                if (!updateClient(client)) {
                    addClient(client);
                }
            }
        } catch (IOException e) {
            logger.error("Десериализация данных по клиентам не произошла");
        }
        logger.info("Десериализация данных по клиентам завершена");
    }
}
