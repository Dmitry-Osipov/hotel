package repository;

import dao.IDao;
import essence.provided.ProvidedService;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import utils.exceptions.TechnicalException;

import java.sql.SQLException;
import java.util.List;

/**
 * Класс отвечает за хранение списка оказанных услуг.
 */
@Repository
@ToString
public class ProvidedServicesRepository {
    private final IDao dao;

    @Autowired
    public ProvidedServicesRepository(@Qualifier("hiberDao") IDao dao) {
        this.dao = dao;
    }

    /**
     * Возвращает список всех оказанных услуг.
     * @return список оказанных услуг.
     * @throws SQLException если произошла ошибка при получении данных из базы данных.
     */
    public <T extends ProvidedService> List<T> getProvidedServices() throws SQLException {
        return (List<T>) dao.getAll(ProvidedService.class);
    }

    /**
     * Сохраняет информацию об оказанной услуге в репозитории.
     * @param providedService объект оказанной услуги, который нужно сохранить или обновить.
     * @throws SQLException если произошла ошибка при выполнении SQL-запроса.
     */
    public void save(ProvidedService providedService) throws SQLException {
        dao.save(providedService);
    }

    /**
     * Обновляет информацию об оказанной услуге в репозитории.
     * @param providedService объект оказанной услуги, который нужно сохранить или обновить.
     * @throws SQLException если произошла ошибка при выполнении SQL-запроса.
     * @throws TechnicalException если произошла ошибка обновления проведённой услуги.
     */
    public void update(ProvidedService providedService) throws SQLException {
        dao.update(providedService);
    }
}
