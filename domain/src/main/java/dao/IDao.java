package dao;

import essence.Identifiable;

import java.lang.reflect.InvocationTargetException;
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
     * @throws IllegalAccessException если доступ к классу или его полям был закрыт.
     */
    <T extends Identifiable> void save(T essence) throws SQLException, IllegalAccessException;

    /**
     * Обновляет указанный объект в базе данных.
     * @param essence объект, который нужно обновить в базе данных.
     * @throws SQLException если произошла ошибка SQL.
     * @throws IllegalAccessException если доступ к классу или его полям был закрыт.
     */
    <T extends Identifiable> void update(T essence) throws SQLException, IllegalAccessException;

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
     * @throws NoSuchMethodException если запрашиваемый метод не существует.
     * @throws InvocationTargetException если вызванный метод вызывает исключение.
     * @throws InstantiationException если попытка создать экземпляр класса абстрактного класса, интерфейса, массива
     * абстрактных классов или интерфейсов, или если класс, указанный в параметре типа, является абстрактным.
     * @throws IllegalAccessException если доступ к классу или его полям был закрыт.
     * @throws NoSuchFieldException если запрашиваемое поле не существует.
     */
    <T extends Identifiable> T getOne(int id, Class<T> clazz) throws SQLException, NoSuchMethodException,
            InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException;

    /**
     * Возвращает список всех объектов указанного класса из базы данных.
     * @param clazz класс объектов.
     * @return список объектов из базы данных.
     * @throws SQLException если произошла ошибка SQL.
     * @throws NoSuchFieldException если запрашиваемое поле не существует.
     * @throws InvocationTargetException если вызванный метод вызывает исключение.
     * @throws NoSuchMethodException если запрашиваемый метод не существует.
     * @throws InstantiationException если попытка создать экземпляр класса абстрактного класса, интерфейса, массива
     * абстрактных классов или интерфейсов, или если класс, указанный в параметре типа, является абстрактным.
     * @throws IllegalAccessException если доступ к классу или его полям был закрыт.
     */
    <T extends Identifiable> List<T> getAll(Class<T> clazz) throws SQLException, NoSuchFieldException,
            InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, ClassNotFoundException;
}
