package dao;

import essence.Identifiable;

import java.sql.SQLException;
import java.util.List;

/**
 * Интерфейс, предоставляющий методы доступа к базе данных для работы с объектами, реализующими интерфейс
 * {@link Identifiable}. Каждый объект, сохраняемый, обновляемый или удаляемый в базе данных, должен быть типизирован
 * классом, реализующим интерфейс {@link Identifiable}.
 */
public interface IDao {
    /**
     * Сохраняет указанный объект в базе данных.
     * @param essence объект, который нужно сохранить в базе данных.
     * @throws SQLException если произошла ошибка SQL.
     */
    <T extends Identifiable> void save(T essence) throws SQLException;

    /**
     * Обновляет указанный объект в базе данных.
     * @param essence объект, который нужно обновить в базе данных.
     * @throws SQLException если произошла ошибка SQL.
     */
    <T extends Identifiable> void update(T essence) throws SQLException;

    /**
     * Удаляет указанный объект из базы данных.
     * @param essence объект, который нужно удалить из базы данных.
     * @throws SQLException если произошла ошибка SQL.
     */
    <T extends Identifiable> void delete(T essence) throws SQLException;

    /**
     * Возвращает один объект из базы данных по его идентификатору.
     * @param id идентификатор объекта.
     * @param clazz класс объекта.
     * @return объект из базы данных.
     * @throws SQLException если произошла ошибка SQL.
     */
    <T extends Identifiable> T getOne(int id, Class<T> clazz) throws SQLException;

    /**
     * Возвращает список всех объектов указанного класса из базы данных.
     * @param clazz класс объектов.
     * @return список объектов из базы данных.
     * @throws SQLException если произошла ошибка SQL.
     */
    <T extends Identifiable> List<T> getAll(Class<T> clazz) throws SQLException;
}
