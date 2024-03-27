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
 * Класс отвечает за хранение списка оказанных услуг. Реализован с использованием паттерна Singleton для обеспечения
 * единственного экземпляра репозитория оказанных услуг.
 */
@Component
@ToString
public class ProvidedServicesRepository {
    @InjectByInterface(clazz = JdbcDao.class)
    private IDao dao;

    // TODO: дока
    public <T extends ProvidedService> List<T> getProvidedServices() throws SQLException {
        return (List<T>) dao.getAll(ProvidedService.class);
    }

    public void saveOrUpdate(ProvidedService providedService) throws SQLException {
        try {
            dao.update(providedService);
        } catch (TechnicalException e) {
            dao.save(providedService);
        }
    }
}
