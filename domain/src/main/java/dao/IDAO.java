package dao;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

// TODO: дока
public interface IDAO {
    <T> void save(T essence) throws SQLException, IllegalAccessException, NoSuchFieldException, NoSuchMethodException,
            InvocationTargetException, InstantiationException;
    <T> T getOne(int id, Class<T> clazz) throws SQLException, NoSuchMethodException,
            InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchFieldException;
    <T> void update(T essence) throws SQLException;
    <T> void delete(T essence) throws SQLException;
    <T> List<T> getAll(Class<T> clazz) throws SQLException, NoSuchFieldException, InvocationTargetException,
            NoSuchMethodException, InstantiationException, IllegalAccessException;
}
