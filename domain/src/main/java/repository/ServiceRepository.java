package repository;

import annotations.annotation.Component;
import annotations.annotation.InjectByInterface;
import dao.IDao;
import dao.JdbcDao;
import essence.service.AbstractService;
import essence.service.Service;
import lombok.ToString;
import utils.exceptions.TechnicalException;

import java.sql.SQLException;
import java.util.List;

/**
 * Класс отвечает за хранение услуг отеля.
 */
@Component
@ToString
public class ServiceRepository {
    @InjectByInterface(clazz = JdbcDao.class)
    private IDao dao;

    /**
     * Возвращает список всех услуг отеля.
     * @return список услуг отеля.
     * @throws SQLException если произошла ошибка при получении данных из базы данных.
     */
    public <T extends AbstractService> List<T> getServices() throws SQLException {
        return (List<T>) dao.getAll(Service.class);
    }

    /**
     * Сохраняет или обновляет информацию об услуге в репозитории.
     * @param service объект услуги, который нужно сохранить или обновить.
     * @throws SQLException если произошла ошибка при выполнении SQL-запроса.
     */
    public void saveOrUpdate(AbstractService service) throws SQLException {
        try {
            dao.update(service);
        } catch (TechnicalException e) {
            dao.save(service);
        }
    }
}
