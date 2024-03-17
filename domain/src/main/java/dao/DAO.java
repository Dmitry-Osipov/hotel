package dao;

import annotations.annotation.Component;
import annotations.annotation.ConfigProperty;
import essence.Identifiable;
import essence.room.RoomStatusTypes;
import essence.service.ServiceNames;
import essence.service.ServiceStatusTypes;
import lombok.Setter;
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
import java.sql.Timestamp;
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
    public <T extends Identifiable> void save(T essence) throws SQLException, IllegalAccessException {
        Field[] fields = essence.getClass().getDeclaredFields();
        String sql = sqlInsertIntoBuild(essence);
        try (
                Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            for (int i = 0; i < fields.length; i++) {
                fields[i].setAccessible(true);
                Object value = parseValueFromApplication(essence, fields, i);
                statement.setObject(i + 1, value);
            }

            if (statement.executeUpdate() == 0) {
                throw new TechnicalException("Не удалось вставить данные в БД");
            }
        }
    }

    @Override
    public <T extends Identifiable> void update(T essence) throws SQLException, IllegalAccessException {
        String sql = sqlUpdateBuilder(essence);
        Field[] fields = essence.getClass().getDeclaredFields();
        try (
                Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            for (int i = 0; i < fields.length; i++) {
                fields[i].setAccessible(true);
                Object value = parseValueFromApplication(essence, fields, i);
                statement.setObject(i + 1, value);
            }

            if (statement.executeUpdate() == 0) {
                throw new TechnicalException("Не удалось обновить данные в БД");
            }
        }
    }

    @Override
    public <T extends Identifiable> void delete(T essence) throws SQLException {
        String table = parseStringFromCamelCaseToSnakeCase(essence.getClass().getSimpleName());
        try (
                Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
                PreparedStatement statement = connection.prepareStatement(String.format("DELETE FROM %s WHERE id = %d",
                table, essence.getId()))
        ) {
            if (statement.executeUpdate() == 0) {
                throw new TechnicalException("Не удалось удалить данные из БД");
            }
        }
    }

    @Override
    public <T extends Identifiable> T getOne(int id, Class<T> clazz) throws SQLException, NoSuchMethodException,
            InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        String table = parseStringFromCamelCaseToSnakeCase(clazz.getSimpleName());
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
                    String columnName = parseStringFromSnakeCaseToCamelCase(metaData.getColumnName(i));
                    Object columnValue = parseValueFromDB(columnName, table, resultSet, i);
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
    public <T extends Identifiable> List<T> getAll(Class<T> clazz) throws SQLException, NoSuchFieldException,
            InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        List<T> list = new ArrayList<>();
        String table = parseStringFromCamelCaseToSnakeCase(clazz.getSimpleName());
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

    private String parseStringFromSnakeCaseToCamelCase(String columnName) {
        String[] parts = columnName.split("_");
        StringBuilder camelCase = new StringBuilder(parts[0]);

        for (int i = 1; i < parts.length; i++) {
            camelCase.append(Character.toUpperCase(parts[i].charAt(0)))
                    .append(parts[i].substring(1));
        }

        return camelCase.toString();
    }

    private Object parseValueFromDB(String columnName, String table, ResultSet resultSet, int index) throws
            SQLException {
        Object columnValue;
        if (columnName.equals("status")) {
            columnValue = parseStatusValueFromDB(columnName, table, resultSet, index);
        } else if (table.equals("service") && columnName.equals("name")) {
            columnValue = ServiceNames.valueOf((String) resultSet.getObject(index));
        } else if (columnName.contains("Time")) {
            columnValue = parseTimeValueFromDB(resultSet, index);
        }  else {
            columnValue = resultSet.getObject(index);
        }

        return columnValue;
    }

    private Object parseTimeValueFromDB(ResultSet resultSet, int index) throws SQLException {
        Object columnValue;
        columnValue = resultSet.getObject(index);
        if (columnValue != null) {
            Timestamp timestamp = (Timestamp) columnValue;
            columnValue = timestamp.toLocalDateTime();
        }

        return columnValue;
    }

    private Object parseStatusValueFromDB(String columnName, String table, ResultSet resultSet, int index) throws
            SQLException {
        Object columnValue;
        if (table.equals("room")) {
            columnValue = RoomStatusTypes.valueOf((String) resultSet.getObject(index));
        } else if (table.equals("service")) {
            columnValue = ServiceStatusTypes.valueOf((String) resultSet.getObject(index));
        } else {
            throw new TechnicalException(String.format("Таблица %s имеет колонку %s, но нет специального " +
                    "обработчика", table, columnName));
        }
        return columnValue;
    }

    private String parseStringFromCamelCaseToSnakeCase(String columnName) {
        StringBuilder snakeCase = new StringBuilder();
        char[] chars = columnName.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            if (i == 0 && Character.isUpperCase(chars[i])) {
                snakeCase.append(Character.toLowerCase(chars[i]));
            } else if (Character.isUpperCase(chars[i])) {
                snakeCase.append("_").append(Character.toLowerCase(chars[i]));
            } else {
                snakeCase.append(chars[i]);
            }
        }

        return snakeCase.toString();
    }

    private <T extends Identifiable> Object parseValueFromApplication(T essence, Field[] fields, int index) throws
            IllegalAccessException {
        Object value;
        if (fields[index].getType().isEnum()) {
            value = String.valueOf(fields[index].get(essence));
        } else {
            value = fields[index].get(essence);
        }
        return value;
    }

    private <T extends Identifiable> String sqlInsertIntoBuild(T essence) {
        Class<?> clazz = essence.getClass();
        Field[] fields = clazz.getDeclaredFields();
        StringBuilder sql =
                new StringBuilder("INSERT INTO " + parseStringFromCamelCaseToSnakeCase(clazz.getSimpleName()) + " (");
        for (int i = 0; i < fields.length; i++) {
            if (i > 0) {
                sql.append(", ");
            }
            sql.append(parseStringFromCamelCaseToSnakeCase(fields[i].getName()));
        }

        sql.append(") VALUES (");
        for (int i = 0; i < fields.length; i++) {
            if (i > 0) {
                sql.append(", ");
            }
            sql.append("?");
        }
        sql.append(")");

        return sql.toString();
    }

    private <T extends Identifiable> String sqlUpdateBuilder(T essence) {
        Class<?> clazz = essence.getClass();
        StringBuilder sql =
                new StringBuilder("UPDATE " + parseStringFromCamelCaseToSnakeCase(clazz.getSimpleName()) + " SET ");
        Field[] fields = clazz.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            fields[i].setAccessible(true);
            if (i > 0) {
                sql.append(", ");
            }
            sql.append(parseStringFromCamelCaseToSnakeCase(fields[i].getName())).append(" = ?");
        }
        sql.append(" WHERE id = ").append(essence.getId());

        return sql.toString();
    }
}
