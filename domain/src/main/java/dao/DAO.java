package dao;

import annotations.annotation.Component;
import annotations.annotation.ConfigProperty;
import lombok.Setter;
import utils.database.ParserFromApplication;
import utils.database.ParserFromDB;
import utils.exceptions.NoEntityException;
import utils.exceptions.TechnicalException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

// TODO: дока
@Component
@Setter  // TODO: убрать сеттеры после тестов
public class DAO implements IDAO {
    @ConfigProperty(configName = "db_url")
    private String dbUrl;
    @ConfigProperty(configName = "db_user")
    private String dbUser;
    @ConfigProperty(configName = "db_password")
    private String dbPassword;

    @Override
    public <T> void save(T essence) throws SQLException, IllegalAccessException {
        Class<?> clazz = essence.getClass();
        String table = clazz.getSimpleName().toLowerCase();
        Field[] fields = clazz.getDeclaredFields();
        String sql = sqlInsertIntoBuild(table, fields);
        try (
                Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            for (int i = 0; i < fields.length; i++) {
                fields[i].setAccessible(true);
                Object value = ParserFromApplication.parseValueFromApplication(essence, fields, i);
                statement.setObject(i + 1, value);
            }

            if (statement.executeUpdate() == 0) {
                throw new TechnicalException("Не удалось вставить данные в БД");
            }
        }
    }

    @Override
    public <T> T getOne(int id, Class<T> clazz) throws SQLException, NoSuchMethodException,
            InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        String table = clazz.getSimpleName().toLowerCase();
        try (
                Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(String.format("SELECT * FROM %s WHERE id = %d", table, id))
        ) {
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            T entity = clazz.getDeclaredConstructor().newInstance();

            if (resultSet.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = ParserFromDB.parseColumnNameFromSnakeCase(metaData.getColumnName(i));
                    Object columnValue = ParserFromDB.parseValueFromDB(columnName, table, resultSet, i);
                    Field field = clazz.getDeclaredField(columnName);
                    field.setAccessible(true);
                    field.set(entity, columnValue);
                }
            } else {
                throw new NoEntityException("В БД отсутствует сущность с id=" + id);
            }

            return entity;
        }
    }

    @Override
    public <T> void update(T essence) throws SQLException {

    }

    @Override
    public <T> void delete(T essence) throws SQLException {

    }

    @Override
    public <T> List<T> getAll(Class<T> clazz) throws SQLException, NoSuchFieldException, InvocationTargetException,
            NoSuchMethodException, InstantiationException, IllegalAccessException {
        List<T> list = new ArrayList<>();
        String table = clazz.getSimpleName().toLowerCase();
        try (
                Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(String.format("SELECT * FROM %s", table))
        ) {
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                while (resultSet.next()) {
                    list.add(getOne((Integer) resultSet.getObject(i), clazz));
                }
            }
        }

        return list;
    }

    private static String sqlInsertIntoBuild(String table, Field[] fields) {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("INSERT INTO ").append(table).append(" (");
        for (int i = 0; i < fields.length; i++) {
            if (i > 0) {
                sqlBuilder.append(", ");
            }
            sqlBuilder.append(ParserFromApplication.parseColumnNameFromCamelCase(fields[i].getName()));
        }

        sqlBuilder.append(") VALUES (");
        for (int i = 0; i < fields.length; i++) {
            if (i > 0) {
                sqlBuilder.append(", ");
            }
            sqlBuilder.append("?");
        }
        sqlBuilder.append(")");
        return sqlBuilder.toString();
    }
}
