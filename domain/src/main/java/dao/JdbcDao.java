package dao;

import annotations.annotation.Component;
import annotations.annotation.ConfigProperty;
import essence.Identifiable;
import essence.person.AbstractClient;
import essence.person.Client;
import essence.provided.ProvidedService;
import essence.reservation.RoomReservation;
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

/**
 * Реализация интерфейса {@link IDao}, предоставляющая методы доступа к базе данных. Класс выполняет операции
 * сохранения, обновления, удаления и получения объектов из базы данных. Для взаимодействия с базой данных используется
 * JDBC.
 */
@Component
@Setter  // TODO: убрать сеттеры после тестов
public class JdbcDao implements IDao {
    @ConfigProperty(configName = "db_url")
    private String dbUrl;
    @ConfigProperty(configName = "db_user")
    private String dbUser;
    @ConfigProperty(configName = "db_password")
    private String dbPassword;

    /**
     * Сохраняет указанный объект в базе данных.
     * @param essence объект, который нужно сохранить в базе данных.
     * @throws SQLException если произошла ошибка SQL.
     * @throws IllegalAccessException если доступ к классу или его полям был закрыт.
     * @throws TechnicalException если не удалось вставить данные в БД.
     */
    @Override
    public <T extends Identifiable> void save(T essence) throws SQLException, IllegalAccessException {
        Field[] fields = essence.getClass().getDeclaredFields();
        String sql = sqlInsertIntoBuild(essence);
        try (
                Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            for (int i = 0; i < fields.length; i++) {
                if (essence.getClass().equals(RoomReservation.class) && fields[i].getName().equals("room")) {
                    continue;
                }

                fields[i].setAccessible(true);
                Object value;
                if (fields[i].getName().equals("clients")) {
                    value = ((ArrayList<T>) parseValueFromApplication(essence, fields[i])).getFirst().getId();
                } else if (fields[i].getName().equals("room") || fields[i].getName().equals("service")
                        || fields[i].getName().equals("client")) {
                    value = ((T) parseValueFromApplication(essence, fields[i])).getId();
                } else {
                    value = parseValueFromApplication(essence, fields[i]);
                }

                if (i > 2 && essence.getClass().equals(RoomReservation.class)) {
                    statement.setObject(i, value);
                } else {
                    statement.setObject(i + 1, value);
                }
            }

            if (statement.executeUpdate() == 0) {
                throw new TechnicalException("Не удалось вставить данные в БД");
            }
        }
    }

    /**
     * Обновляет указанный объект в базе данных.
     * @param essence объект, который нужно обновить в базе данных.
     * @throws SQLException если произошла ошибка SQL.
     * @throws IllegalAccessException если доступ к классу или его полям был закрыт.
     * @throws TechnicalException если не удалось обновить данные в БД.
     */
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
                Object value = parseValueFromApplication(essence, fields[i]);
                statement.setObject(i + 1, value);
            }

            if (statement.executeUpdate() == 0) {
                throw new TechnicalException("Не удалось обновить данные в БД");
            }
        }
    }

    /**
     * Удаляет указанный объект из базы данных.
     * @param essence объект, который нужно удалить из базы данных.
     * @throws SQLException если произошла ошибка SQL.
     * @throws TechnicalException если не удалось удалить данные из БД.
     */
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
     * @throws NoEntityException если сущность с переданным id отсутствует в БД.
     */
    @Override
    public <T extends Identifiable> T getOne(int id, Class<T> clazz) throws SQLException, NoSuchMethodException,
            InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchFieldException,
            ClassNotFoundException {
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
                    Object columnValue;
                    if (columnName.contains("Id")) {
                        columnName = columnName.substring(0, columnName.lastIndexOf("Id"));
                        String classField = columnName.substring(0, 1).toUpperCase() +
                                columnName.substring(1);
                        if (classField.equals("Room")) {
                            columnValue = getOne(i,
                                    (Class<T>) Class.forName("essence.room." + classField));
                        } else if (classField.equals("Service")) {
                            columnValue = getOne(i,
                                    (Class<T>) Class.forName("essence.service." + classField));
                        } else if (classField.equals("Client") && clazz.equals(ProvidedService.class)) {
                            columnName = "beneficiaries";
                            columnValue = List.of(getOne(i,
                                    (Class<T>) Class.forName("essence.person." + classField)));
                        } else {
                            throw new TechnicalException("Для данного класса нет специального обработчика");
                        }
                    } else {
                        columnValue = parseValueFromDB(columnName, table, resultSet, i);
                    }
                    Field field = clazz.getDeclaredField(columnName);
                    field.setAccessible(true);
                    field.set(entity, columnValue);
                }

                if (clazz.equals(RoomReservation.class)) {
                    implementationClientListIntoReservation(entity);
                }
            } else {
                throw new NoEntityException("В БД отсутствует сущность с id = " + id);
            }

            return entity;
        }
    }

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
    @Override
    public <T extends Identifiable> List<T> getAll(Class<T> clazz) throws SQLException, NoSuchFieldException,
            InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException,
            ClassNotFoundException {
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

    // TODO: добавляем связи по клиентам. Нужно вызывать метод после добавления резервации в БД
    private <T extends Identifiable> void insertReservationClients(T roomReservation) throws SQLException {
        try (
                Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
                PreparedStatement statement = connection.prepareStatement("INSERT INTO reservation_client " +
                        "(reservation_id, client_id) VALUES (?, ?)")
        ) {
            RoomReservation reservation = (RoomReservation) roomReservation;
            for (AbstractClient client : reservation.getClients()) {
                statement.setInt(1, reservation.getId());
                statement.setInt(2, client.getId());
                statement.addBatch();
            }
            statement.executeBatch();
        }
    }

    private <T extends Identifiable> void implementationClientListIntoReservation(T roomReservation) throws
            SQLException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException,
            InstantiationException, IllegalAccessException, ClassNotFoundException {
        try (
                Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(String.format("SELECT * FROM reservation_client " +
                        "WHERE reservation_id = %d", roomReservation.getId()))
        ) {
            List<T> list = new ArrayList<>();
            while (resultSet.next()) {
                list.add((T) getOne((Integer) resultSet.getObject(2), Client.class));
            }

            Field field = roomReservation.getClass().getDeclaredField("clients");
            field.setAccessible(true);
            field.set(roomReservation, list);
        }
    }

    /**
     * Преобразует строку из формата camelCase в snake_case.
     * @param columnName строка в формате camelCase.
     * @return строка в формате snake_case.
     */
    private String parseStringFromSnakeCaseToCamelCase(String columnName) {
        String[] parts = columnName.split("_");
        StringBuilder camelCase = new StringBuilder(parts[0]);

        for (int i = 1; i < parts.length; i++) {
            camelCase.append(Character.toUpperCase(parts[i].charAt(0)))
                    .append(parts[i].substring(1));
        }

        return camelCase.toString();
    }

    /**
     * Преобразует значение из базы данных в соответствующий тип Java.
     * @param columnName имя колонки в базе данных.
     * @param table имя таблицы в базе данных.
     * @param resultSet объект ResultSet, содержащий результат запроса.
     * @param index индекс колонки в ResultSet.
     * @return значение из базы данных в соответствующем типе Java.
     * @throws SQLException если произошла ошибка SQL.
     */
    private Object parseValueFromDB(String columnName, String table, ResultSet resultSet, int index) throws
            SQLException {
        Object columnValue;
        if (columnName.equals("status")) {
            columnValue = parseStatusValueFromDB(columnName, table, resultSet, index);
        } else if (table.equals("service") && columnName.equals("name")) {
            columnValue = ServiceNames.valueOf((String) resultSet.getObject(index));
        } else if (columnName.contains("Time")) {
            columnValue = parseTimeValueFromDB(resultSet, index);
        } else {
            columnValue = resultSet.getObject(index);
        }

        return columnValue;
    }

    /**
     * Преобразует значение времени из базы данных в тип LocalDateTime.
     * @param resultSet объект ResultSet, содержащий результат запроса.
     * @param index индекс колонки в ResultSet.
     * @return значение времени из базы данных в типе LocalDateTime.
     * @throws SQLException если произошла ошибка SQL.
     */
    private Object parseTimeValueFromDB(ResultSet resultSet, int index) throws SQLException {
        Object columnValue = resultSet.getObject(index);
        if (columnValue != null) {
            Timestamp timestamp = (Timestamp) columnValue;
            columnValue = timestamp.toLocalDateTime();
        }

        return columnValue;
    }

    /**
     * Преобразует значение статуса из базы данных в соответствующий Java enum тип.
     * @param columnName имя колонки в базе данных.
     * @param table имя таблицы в базе данных.
     * @param resultSet объект ResultSet, содержащий результат запроса.
     * @param index индекс колонки в ResultSet.
     * @return значение статуса из базы данных в соответствующем Java enum типе.
     * @throws SQLException если произошла ошибка SQL.
     * @throws TechnicalException если таблица имеет колонку статуса, но специальный обработчик для неё не предусмотрен.
     */
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

    /**
     * Преобразует строку из формата camelCase в snake_case.
     * @param columnName строка в формате camelCase.
     * @return строка в формате snake_case.
     */
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

    /**
     * Получает значение поля из объекта сущности.
     * @param essence объект сущности.
     * @param field поле объекта.
     * @return значение поля.
     * @throws IllegalAccessException если доступ к полю невозможен.
     */
    private <T extends Identifiable> Object parseValueFromApplication(T essence, Field field) throws
            IllegalAccessException {
        Object value;
        if (field.getType().isEnum()) {
            value = String.valueOf(field.get(essence));
        } else {
            value = field.get(essence);
        }
        return value;
    }

    /**
     * Создает SQL запрос для вставки данных в таблицу.
     * @param essence объект, данные которого нужно вставить в таблицу.
     * @return SQL запрос для вставки данных в таблицу.
     */
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

        if (essence.getClass().equals(RoomReservation.class)) {
            sql.delete(sql.lastIndexOf("clients"), sql.lastIndexOf("clients") + 9);
            sql.replace(sql.lastIndexOf("room"), sql.lastIndexOf("room") + 4, "room_id");
            sql.delete(sql.lastIndexOf("?, "), sql.lastIndexOf("?, ") + 3);
        }

        return sql.toString();
    }

    /**
     * Создает SQL запрос для обновления данных в таблице.
     * @param essence объект, данные которого нужно обновить в таблице.
     * @return SQL запрос для обновления данных в таблице.
     */
    private <T extends Identifiable> String sqlUpdateBuilder(T essence) {
        Class<?> clazz = essence.getClass();
        StringBuilder sql =
                new StringBuilder("UPDATE " + parseStringFromCamelCaseToSnakeCase(clazz.getSimpleName()) + " SET ");
        Field[] fields = clazz.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            if (i > 0) {
                sql.append(", ");
            }
            sql.append(parseStringFromCamelCaseToSnakeCase(fields[i].getName())).append(" = ?");
        }
        sql.append(" WHERE id = ").append(essence.getId());

        return sql.toString();
    }
}
