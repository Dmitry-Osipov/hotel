package service;

import essence.person.AbstractClient;
import essence.person.Client;
import repository.client.ClientRepository;

import java.time.LocalDateTime;
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
     * @param id ID клиента, по которому будет проходить идентификация клиента.
     * @param fio Новое имя клиента.
     * @param checkInTime Новое время въезда.
     * @param checkOutTime Новое время выезда.
     * @return true, если удалось обновить данные, иначе false.
     */
    public boolean updateClient(int id, String fio, LocalDateTime checkInTime, LocalDateTime checkOutTime) {
        for (AbstractClient client : clientRepository.getClients()) {
            if (client.getId() == id) {
                client.setFio(fio);
                client.setCheckInTime(checkInTime);
                client.setCheckOutTime(checkOutTime);

                return true;
            }
        }

        return false;
    }
}
