package repository;

import dao.IDao;
import essence.person.AbstractClient;
import essence.person.Client;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import utils.exceptions.TechnicalException;

import java.sql.SQLException;
import java.util.List;

/**
 * Класс отвечает за хранение клиентов отеля.
 */
@Repository
@ToString
public class ClientRepository {
    private final IDao dao;

    @Autowired
    public ClientRepository(@Qualifier("hiberDao") IDao dao) {
        this.dao = dao;
    }

    /**
     * Возвращает список всех клиентов отеля.
     * @return список клиентов отеля.
     * @throws SQLException если произошла ошибка при получении данных из базы данных.
     */
    public <T extends AbstractClient> List<T> getClients() throws SQLException {
        return (List<T>) dao.getAll(Client.class);
    }

    /**
     * Сохраняет информацию о клиенте в репозитории.
     * @param client объект клиента, который нужно сохранить или обновить.
     * @throws SQLException если произошла ошибка при выполнении SQL-запроса.
     */
    public void save(AbstractClient client) throws SQLException {
        dao.save(client);
    }

    /**
     * Обновляет информацию о клиенте в репозитории.
     * @param client объект клиента, который нужно сохранить или обновить.
     * @throws SQLException если произошла ошибка при выполнении SQL-запроса.
     * @throws TechnicalException если невозможно обновить клиента.
     */
    public void update(AbstractClient client) throws SQLException {
        dao.update(client);
    }

    /**
     * Получает клиента по его уникальному идентификатору.
     * @param id Уникальный идентификатор клиента.
     * @return Клиент с указанным ID.
     * @throws SQLException Если происходит ошибка доступа к базе данных.
     * @throws TechnicalException Если возникает техническая ошибка при выполнении операции.
     */
    public AbstractClient getClientById(int id) throws SQLException {
        return dao.getOne(id, Client.class);
    }

    /**
     * Удаляет клиента из репозитория.
     * @param client Клиент, который должен быть удален.
     * @throws SQLException Если происходит ошибка доступа к базе данных.
     * @throws TechnicalException Если возникает техническая ошибка при выполнении операции.
     */
    public void deleteClient(AbstractClient client) throws SQLException {
        dao.delete(client);
    }
}
