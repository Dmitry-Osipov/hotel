package service;

import essence.person.AbstractClient;
import repository.ClientRepository;

/**
 * Класс отвечает за обработку данных по клиентам.
 */
public class ClientService {
    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    /**
     * Метод добавляет нового клиента в список всех клиентов отеля.
     * @param client Новый клиент.
     * @return true, если клиент был добавлен, иначе false.
     */
    public boolean addClient(AbstractClient client) {
        return clientRepository.getClients().add(client);
    }

    /**
     * Метод подсчитывает общее число клиентов.
     * @return Количество клиентов.
     */
    public int countClients() {
        return clientRepository.getClients().size();
    }
}
