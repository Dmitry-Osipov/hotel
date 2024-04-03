package dao;

import essence.Identifiable;
import jakarta.persistence.OptimisticLockException;
import lombok.NoArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import utils.database.SessionFactoryCreator;
import utils.exceptions.ErrorMessages;
import utils.exceptions.TechnicalException;

import java.util.List;
import java.util.Objects;

/**
 * Класс HiberDao представляет собой реализацию интерфейса {@link IDao} для работы с объектами базы данных
 * с использованием Hibernate.
 */
@Repository
@NoArgsConstructor
public class HiberDao implements IDao {
    private static final Logger logger = LoggerFactory.getLogger(HiberDao.class);

    @Override
    public <T extends Identifiable> void save(T essence) {
        String name = essence.getClass().getSimpleName();
        logger.info("Вызван метод сохранения {} в БД", name);
        try (
                SessionFactory factory = SessionFactoryCreator.getSessionFactory();
                Session session = factory.getCurrentSession()) {
            session.beginTransaction();
            session.save(essence);
            session.getTransaction().commit();
            logger.info("Удалось сохранить {} в БД", name);
        }
    }

    @Override
    public <T extends Identifiable> void update(T essence) {
        String name = essence.getClass().getSimpleName();
        int id = essence.getId();
        logger.info("Вызван метод обновления {} с ID {} в БД", name, id);
        try (
                SessionFactory factory = SessionFactoryCreator.getSessionFactory();
                Session session = factory.getCurrentSession()) {
            session.beginTransaction();
            session.update(essence);
            session.getTransaction().commit();
            logger.info("Удалось обновить {} с ID {} в БД", name, id);
        } catch (OptimisticLockException e) {
            logger.error("Не удалось обновить {} с ID {} в БД", name, id);
            throw new TechnicalException(ErrorMessages.DB_UPDATE_EXCEPTION.getMessage());
        }
    }

    @Override
    public <T extends Identifiable> void delete(T essence) {
        String name = essence.getClass().getSimpleName();
        int id = essence.getId();
        logger.info("Вызван метод удаления {} с ID {} из БД", name, id);
        try (
                SessionFactory factory = SessionFactoryCreator.getSessionFactory();
                Session session = factory.getCurrentSession()) {
            session.beginTransaction();
            session.remove(essence);
            session.getTransaction().commit();
            logger.info("Удалось удалить {} с ID {} из БД", name, id);
        } catch (OptimisticLockException e) {
            logger.error("Не удалось удалить {} с ID {} из БД", name, id);
            throw new TechnicalException(ErrorMessages.DB_DELETE_EXCEPTION.getMessage());
        }
    }

    @Override
    public <T extends Identifiable> T getOne(int id, Class<T> clazz) {
        String name = clazz.getSimpleName();
        logger.info("Вызван метод получения {} с ID {} из БД", name, id);
        try (
                SessionFactory factory = SessionFactoryCreator.getSessionFactory();
                Session session = factory.getCurrentSession()) {
            session.beginTransaction();
            T entity = session.get(clazz, id);

            if (entity == null) {
                logger.error("Не удалось получить {} с ID {} из БД", name, id);
                throw new TechnicalException(ErrorMessages.DB_NO_SELECT_EXCEPTION.getMessage());
            }

            session.getTransaction().commit();
            logger.info("Удалось получить {} с ID {} из БД", name, id);
            return entity;
        }
    }

    @Override
    public <T extends Identifiable> List<T> getAll(Class<T> clazz) {
        String name = clazz.getSimpleName();
        logger.info("Вызван метод получения списка {} из БД", name);
        try (
                SessionFactory factory = SessionFactoryCreator.getSessionFactory();
                Session session = factory.getCurrentSession()) {
            session.beginTransaction();
            String hql = String.format("from %s", clazz.getSimpleName());
            List<T> list = session.createQuery(hql, clazz).getResultList();

            if (list.isEmpty() || list.stream().allMatch(Objects::isNull)) {
                logger.error("Не удалось получить список {} из БД", name);
                throw new TechnicalException(ErrorMessages.DB_NO_SELECT_EXCEPTION.getMessage());
            }

            session.getTransaction().commit();
            logger.info("Удалось получить список {} из БД", name);
            return list;
        }
    }
}
