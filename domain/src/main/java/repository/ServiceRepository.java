package repository;

import dao.IDao;
import essence.service.AbstractService;
import essence.service.Service;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import utils.exceptions.TechnicalException;

import java.sql.SQLException;
import java.util.List;

/**
 * Класс отвечает за хранение услуг отеля.
 */
@Repository
@ToString
public class ServiceRepository {
    private final IDao dao;

    @Autowired
    public ServiceRepository(@Qualifier("hiberDao") IDao dao) {
        this.dao = dao;
    }

    /**
     * Возвращает список всех услуг отеля.
     * @return список услуг отеля.
     * @throws SQLException если произошла ошибка при получении данных из базы данных.
     */
    public <T extends AbstractService> List<T> getServices() throws SQLException {
        return (List<T>) dao.getAll(Service.class);
    }

    /**
     * Сохраняет информацию об услуге в репозитории.
     * @param service объект услуги, который нужно сохранить или обновить.
     * @throws SQLException если произошла ошибка при выполнении SQL-запроса.
     */
    public void save(AbstractService service) throws SQLException {
        dao.save(service);
    }

    /**
     * Обновляет информацию об услуге в репозитории.
     * @param service объект услуги, который нужно сохранить или обновить.
     * @throws SQLException если произошла ошибка при выполнении SQL-запроса.
     * @throws TechnicalException если произошла ошибка обновления услуги.
     */
    public void update(AbstractService service) throws SQLException {
        dao.update(service);
    }

    /**
     * Получает услугу по ее уникальному идентификатору.
     * @param id Уникальный идентификатор услуги.
     * @return Услуга с указанным ID.
     * @throws SQLException Если происходит ошибка доступа к базе данных.
     * @throws TechnicalException Если возникает техническая ошибка при выполнении операции.
     */
    public AbstractService getServiceById(int id) throws SQLException {
        return dao.getOne(id, Service.class);
    }

    /**
     * Удаляет услугу из репозитория.
     * @param service Услуга, которая должна быть удалена.
     * @throws SQLException Если происходит ошибка доступа к базе данных.
     * @throws TechnicalException Если возникает техническая ошибка при выполнении операции.
     */
    public void deleteService(AbstractService service) throws SQLException {
        dao.delete(service);
    }
}
