package repository;

import annotations.annotation.Component;
import annotations.annotation.InjectByInterface;
import dao.IDao;
import dao.JdbcDao;
import essence.person.AbstractClient;
import essence.person.Client;
import lombok.ToString;
import utils.exceptions.TechnicalException;

import java.sql.SQLException;
import java.util.List;

/**
 * Класс отвечает за хранение клиентов отеля.
 */
@Component
@ToString
public class ClientRepository {
    @InjectByInterface(clazz = JdbcDao.class)
    private IDao dao;

    /**
     * Возвращает список всех клиентов отеля.
     * @return список клиентов отеля.
     * @throws SQLException если произошла ошибка при получении данных из базы данных.
     */
    public <T extends AbstractClient> List<T> getClients() throws SQLException {
        return (List<T>) dao.getAll(Client.class);
    }

    /**
     * Сохраняет или обновляет информацию о клиенте в репозитории.
     * @param client объект клиента, который нужно сохранить или обновить.
     * @throws SQLException если произошла ошибка при выполнении SQL-запроса.
     */
    public void saveOrUpdate(AbstractClient client) throws SQLException {
        try {
            dao.update(client);
        } catch (TechnicalException e) {
            dao.save(client);
        }
    }
}
