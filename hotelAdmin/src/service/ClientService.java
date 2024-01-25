package service;

import essence.person.AbstractClient;
import repository.client.ClientRepository;

import java.util.List;

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
     * Метод формирует список клиентов отеля.
     * @return Список.
     */
    public List<AbstractClient> getClients() {
        return clientRepository.getClients().stream().toList();
    }

    /**
     * Метод подсчитывает общее число клиентов.
     * @return Количество клиентов.
     */
    public int countClients() {
        return clientRepository.getClients().size();
    }

    /**
     * Метод обновляет данные по клиенту.
     * @param client Новые данные клиента, собранные в классе клиента.
     * @return true, если удалось обновить данные, иначе false.
     */
    public boolean updateClient(AbstractClient client) {
        for (AbstractClient currentClient : clientRepository.getClients()) {
            if (currentClient.getId() == client.getId()) {
                currentClient.setFio(client.getFio());
                currentClient.setPhoneNumber(client.getPhoneNumber());
                currentClient.setCheckInTime(client.getCheckInTime());
                currentClient.setCheckOutTime(client.getCheckOutTime());

                return true;
            }
        }

        return false;
    }
}
