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
 * Класс отвечает за хранение клиентов отеля. Реализован с использованием паттерна Singleton для обеспечения
 * единственного экземпляра репозитория клиентов.
 */
@Component
@ToString
public class ClientRepository {
    @InjectByInterface(clazz = JdbcDao.class)
    private IDao dao;

    // TODO: дока
    public <T extends AbstractClient> List<T> getClients() throws SQLException {
        return (List<T>) dao.getAll(Client.class);
    }

    public void saveOrUpdate(AbstractClient client) throws SQLException {
        try {
            dao.update(client);
        } catch (TechnicalException e) {
            dao.save(client);
        }
    }
}
