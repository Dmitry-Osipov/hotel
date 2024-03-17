package dao;

import essence.Identifiable;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

// TODO: дока
public interface IDAO {
    <T extends Identifiable> void save(T essence) throws SQLException, IllegalAccessException, NoSuchFieldException,
            NoSuchMethodException, InvocationTargetException, InstantiationException;
    <T extends Identifiable> T getOne(int id, Class<T> clazz) throws SQLException, NoSuchMethodException,
            InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchFieldException,
            ClassNotFoundException;
    <T extends Identifiable> void update(T essence) throws SQLException, IllegalAccessException;
    <T extends Identifiable> void delete(T essence) throws SQLException;
    <T extends Identifiable> List<T> getAll(Class<T> clazz) throws SQLException, NoSuchFieldException,
            InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException,
            ClassNotFoundException;
}
