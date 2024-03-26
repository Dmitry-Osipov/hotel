package dao;

import annotations.annotation.Autowired;
import annotations.annotation.Component;
import annotations.annotation.ConfigProperty;
import essence.Identifiable;
import essence.person.AbstractClient;
import essence.reservation.RoomReservation;
import utils.SqlBuilder;
import utils.exceptions.ErrorMessages;
import utils.exceptions.NoEntityException;
import utils.exceptions.TechnicalException;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Реализация интерфейса {@link IDao}, предоставляющая методы доступа к базе данных. Класс выполняет операции
 * сохранения, обновления, удаления и получения объектов из базы данных. Для взаимодействия с базой данных используется
 * JDBC.
 */
@Component
public class JdbcDao implements IDao {
    @ConfigProperty(propertyName = "db_url")
    private String dbUrl;
    @ConfigProperty(propertyName = "db_user")
    private String dbUser;
    @ConfigProperty(propertyName = "db_password")
    private String dbPassword;
    @Autowired
    private EntityMapper mapper;

    @Override
    public <T extends Identifiable> void save(T essence) throws SQLException, IllegalAccessException {
        String sql = SqlBuilder.sqlInsertIntoBuild(essence);
        try (
                Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            PreparerStatement.prepareStatement(essence, statement);
            if (statement.executeUpdate() == 0) {
                throw new TechnicalException(ErrorMessages.DB_NO_INSERT_EXCEPTION.getMessage());
            }
        } finally {
            if (essence.getClass().equals(RoomReservation.class)) {
                insertReservationClients((RoomReservation) essence);
            }
        }
    }

    @Override
    public <T extends Identifiable> void update(T essence) throws SQLException, IllegalAccessException {
        String sql = SqlBuilder.sqlUpdateBuild(essence);
        try (
                Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            PreparerStatement.prepareStatement(essence, statement);
            if (statement.executeUpdate() == 0) {
                throw new TechnicalException(ErrorMessages.DB_UPDATE_EXCEPTION.getMessage());
            }

            if (essence.getClass().equals(RoomReservation.class)) {
                deleteFromReservationClientByReservation((RoomReservation) essence);
                insertReservationClients((RoomReservation) essence);
            }
        }
    }

    @Override
    public <T extends Identifiable> void delete(T essence) throws SQLException {
        Class<?> clazz = essence.getClass();
        if (clazz.equals(RoomReservation.class)) {
            deleteFromReservationClientByReservation((RoomReservation) essence);
        }

        String sql = String.format("DELETE FROM hotel.%s WHERE id = %d",
                SqlBuilder.parseStringFromCamelCaseToSnakeCase(clazz.getSimpleName()), essence.getId());
        try (
                Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            if (statement.executeUpdate() == 0) {
                throw new TechnicalException(ErrorMessages.DB_DELETE_EXCEPTION.getMessage());
            }
        }
    }

    @Override
    public <T extends Identifiable> T getOne(int id, Class<T> clazz) throws SQLException, NoSuchMethodException,
            InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchFieldException,
            ClassNotFoundException {
        String sql = String.format("SELECT * FROM hotel.%s WHERE id = %d",
                SqlBuilder.parseStringFromCamelCaseToSnakeCase(clazz.getSimpleName()), id);
        try (
                Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()
        ) {
            if (resultSet.next()) {
                return mapper.mapEntity(clazz, resultSet);
            }

            throw new NoEntityException(ErrorMessages.DB_NO_SELECT_EXCEPTION.getMessage());
        }
    }

    @Override
    public <T extends Identifiable> List<T> getAll(Class<T> clazz) throws SQLException, NoSuchFieldException,
            InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException,
            ClassNotFoundException {
        String sql = String.format("SELECT * FROM hotel.%s",
                SqlBuilder.parseStringFromCamelCaseToSnakeCase(clazz.getSimpleName()));
        try (
                Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()
        ) {
            List<T> list = new ArrayList<>();
            while (resultSet.next()) {
                list.add(mapper.mapEntity(clazz, resultSet));
            }

            return list;
        }
    }

    /**
     * Вставляет записи о клиентах бронирования в таблицу hotel.reservation_client.
     * @param roomReservation объект {@link RoomReservation}, данные которого будут вставлены в таблицу.
     * @throws SQLException если произошла ошибка при выполнении SQL-запроса.
     */
    private void insertReservationClients(RoomReservation roomReservation) throws SQLException {
        try (
                Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
                PreparedStatement statement = connection.prepareStatement("INSERT INTO hotel.reservation_client " +
                        "(reservation_id, client_id) VALUES (?, ?)")
        ) {
            for (AbstractClient client : roomReservation.getClients()) {
                statement.setInt(1, roomReservation.getId());
                statement.setInt(2, client.getId());
                statement.addBatch();
            }
            statement.executeBatch();
        }
    }

    /**
     * Удаляет записи из таблицы hotel.reservation_client по идентификатору бронирования.
     * @param reservation объект {@link RoomReservation}, по которому будет выполнено удаление записей.
     * @throws SQLException если произошла ошибка при выполнении SQL-запроса или если не удалось удалить ни одной
     * записи.
     */
    private void deleteFromReservationClientByReservation (RoomReservation reservation) throws SQLException {
        try (
                Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
                PreparedStatement statement = connection.prepareStatement(String.format("DELETE FROM " +
                        "hotel.reservation_client WHERE reservation_id = %d", reservation.getId()))
        ) {
            if (statement.executeUpdate() == 0) {
                throw new TechnicalException(ErrorMessages.DB_DELETE_EXCEPTION.getMessage());
            }
        }
    }
}
