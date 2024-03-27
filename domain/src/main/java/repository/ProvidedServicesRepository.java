package repository;

import annotations.annotation.Component;
import annotations.annotation.InjectByInterface;
import dao.IDao;
import dao.JdbcDao;
import essence.provided.ProvidedService;
import lombok.ToString;
import utils.exceptions.TechnicalException;

import java.sql.SQLException;
import java.util.List;

/**
 * Класс отвечает за хранение списка оказанных услуг.
 */
@Component
@ToString
public class ProvidedServicesRepository {
    @InjectByInterface(clazz = JdbcDao.class)
    private IDao dao;

    /**
     * Возвращает список всех оказанных услуг.
     * @return список оказанных услуг.
     * @throws SQLException если произошла ошибка при получении данных из базы данных.
     */
    public <T extends ProvidedService> List<T> getProvidedServices() throws SQLException {
        return (List<T>) dao.getAll(ProvidedService.class);
    }

    /**
     * Сохраняет или обновляет информацию об оказанной услуге в репозитории.
     * @param providedService объект оказанной услуги, который нужно сохранить или обновить.
     * @throws SQLException если произошла ошибка при выполнении SQL-запроса.
     */
    public void saveOrUpdate(ProvidedService providedService) throws SQLException {
        try {
            dao.update(providedService);
        } catch (TechnicalException e) {
            dao.save(providedService);
        }
    }
}
