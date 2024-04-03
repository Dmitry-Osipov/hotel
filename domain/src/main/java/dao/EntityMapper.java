package dao;

import annotations.annotation.Component;
import annotations.annotation.ConfigProperty;
import essence.Identifiable;
import essence.person.AbstractClient;
import essence.person.Client;
import essence.provided.ProvidedService;
import essence.reservation.RoomReservation;
import essence.room.Room;
import essence.room.RoomStatusTypes;
import essence.service.Service;
import essence.service.ServiceNames;
import essence.service.ServiceStatusTypes;
import utils.exceptions.ErrorMessages;
import utils.exceptions.TechnicalException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс EntityMapper предоставляет методы для отображения строк из ResultSet на объекты Java.
 * Этот класс используется для отображения данных из базы данных на объекты сущностей.
 */

@Component
public class EntityMapper {
    @ConfigProperty(propertyName = "db_url")
    private String dbUrl = "jdbc:postgresql://localhost:5432/hotel";
    @ConfigProperty(propertyName = "db_user")
    private String dbUser = "hotel_user";
    @ConfigProperty(propertyName = "db_password")
    private String dbPassword = "R7hB2fK9sL6e";

    /**
     * Отображает строку из ResultSet на объект сущности, в зависимости от переданного класса.
     * @param clazz класс сущности.
     * @param resultSet результат запроса SQL.
     * @return объект сущности.
     * @throws SQLException если произошла ошибка при доступе к данным из ResultSet.
     */
    public <T extends Identifiable> T mapEntity(Class<T> clazz, ResultSet resultSet) throws SQLException {
        return switch (clazz.getSimpleName()) {
            case "Room" -> (T) mapRoom(resultSet);
            case "Service" -> (T) mapService(resultSet);
            case "Client" -> (T) mapClient(resultSet);
            case "RoomReservation" -> (T) mapRoomReservation(resultSet);
            case "ProvidedService" -> (T) mapProvidedService(resultSet);
            default -> throw new TechnicalException(ErrorMessages.NO_HANDLER.getMessage());
        };
    }

    /**
     * Отображает данные из ResultSet на объект типа {@link Room}.
     * @param resultSet результат запроса SQL.
     * @return объект {@link Room}.
     * @throws SQLException если произошла ошибка при доступе к данным из ResultSet.
     */
    private Room mapRoom(ResultSet resultSet) throws SQLException {
        Room room = new Room();
        room.setId(resultSet.getInt("id"));
        room.setNumber(resultSet.getInt("number"));
        room.setCapacity(resultSet.getInt("capacity"));
        room.setPrice(resultSet.getInt("price"));
        room.setStatus(RoomStatusTypes.valueOf(resultSet.getString("status")));
        room.setStars(resultSet.getInt("stars"));
        room.setCheckInTime(parseFromTimestamp(resultSet.getTimestamp("check_in_time")));
        room.setCheckOutTime(parseFromTimestamp(resultSet.getTimestamp("check_out_time")));

        return room;
    }

    /**
     * Отображает данные из ResultSet на объект типа {@link Service}.
     * @param resultSet результат запроса SQL.
     * @return объект {@link Service}.
     * @throws SQLException если произошла ошибка при доступе к данным из ResultSet.
     */
    private Service mapService(ResultSet resultSet) throws SQLException {
        Service service = new Service();
        service.setId(resultSet.getInt("id"));
        service.setName(ServiceNames.valueOf(resultSet.getString("name")));
        service.setPrice(resultSet.getInt("price"));
        service.setStatus(ServiceStatusTypes.valueOf(resultSet.getString("status")));
        service.setServiceTime(parseFromTimestamp(resultSet.getTimestamp("service_time")));

        return service;
    }

    /**
     * Отображает данные из ResultSet на объект типа {@link Client}.
     * @param resultSet результат запроса SQL.
     * @return объект {@link Client}.
     * @throws SQLException если произошла ошибка при доступе к данным из ResultSet.
     */
    private Client mapClient(ResultSet resultSet) throws SQLException {
        Client client = new Client();
        client.setId(resultSet.getInt("id"));
        client.setFio(resultSet.getString("fio"));
        client.setPhoneNumber(resultSet.getString("phone_number"));
        client.setCheckInTime(parseFromTimestamp(resultSet.getTimestamp("check_in_time")));
        client.setCheckOutTime(parseFromTimestamp(resultSet.getTimestamp("check_out_time")));

        return client;
    }

    /**
     * Отображает данные из ResultSet на объект типа {@link RoomReservation}.
     * @param resultSet результат запроса SQL.
     * @return объект {@link RoomReservation}.
     * @throws SQLException если произошла ошибка при доступе к данным из ResultSet.
     */
    private RoomReservation mapRoomReservation(ResultSet resultSet) throws SQLException {
        RoomReservation roomReservation = new RoomReservation();
        roomReservation.setId(resultSet.getInt("id"));
        roomReservation.setRoom(getRoomById(resultSet.getInt("room_id")));
        roomReservation.setCheckInTime(parseFromTimestamp(
                resultSet.getTimestamp("check_in_time")));
        roomReservation.setCheckOutTime(parseFromTimestamp(
                resultSet.getTimestamp("check_out_time")));
        roomReservation.setClients(getClientsByRoomReservation(roomReservation.getId()));

        return roomReservation;
    }

    /**
     * Отображает данные из ResultSet на объект типа {@link ProvidedService}.
     * @param resultSet результат запроса SQL.
     * @return объект {@link ProvidedService}.
     * @throws SQLException если произошла ошибка при доступе к данным из ResultSet.
     */
    private ProvidedService mapProvidedService(ResultSet resultSet) throws SQLException {
        ProvidedService providedService = new ProvidedService();
        providedService.setId(resultSet.getInt("id"));
        providedService.setClient(getClientById(resultSet.getInt("client_id")));
        providedService.setService(getServiceById(resultSet.getInt("service_id")));
        providedService.setServiceTime(parseFromTimestamp(resultSet.getTimestamp("service_time")));

        return providedService;
    }

    /**
     * Получает клиента по его идентификатору из базы данных.
     * @param clientId идентификатор клиента.
     * @return объект {@link Client}.
     * @throws SQLException если произошла ошибка при доступе к данным из базы данных.
     */
    private Client getClientById(int clientId) throws SQLException {
        try (
                Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
                PreparedStatement statement = connection.prepareStatement(String.format(
                        "SELECT * FROM hotel.client WHERE id = %d", clientId));
                ResultSet resultSet = statement.executeQuery()
        ) {
            if (resultSet.next()) {
                return mapClient(resultSet);
            }

            return null;
        }
    }

    /**
     * Получает список клиентов по идентификатору бронирования комнаты из базы данных.
     * @param reservationId идентификатор бронирования комнаты.
     * @return список клиентов.
     * @throws SQLException если произошла ошибка при доступе к данным из базы данных.
     */
    private List<AbstractClient> getClientsByRoomReservation(int reservationId) throws SQLException {
        try (
                Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
                PreparedStatement statement = connection.prepareStatement(String.format(
                        "SELECT * FROM hotel.reservation_client WHERE reservation_id = %d", reservationId));
                ResultSet resultSet = statement.executeQuery()
        ) {
            List<AbstractClient> list = new ArrayList<>();
            while (resultSet.next()) {
                list.add(getClientById(resultSet.getInt("client_id")));
            }

            return list;
        }
    }

    /**
     * Получает комнату по ее идентификатору из базы данных.
     * @param roomId идентификатор комнаты.
     * @return объект {@link Room}.
     * @throws SQLException если произошла ошибка при доступе к данным из базы данных.
     */
    private Room getRoomById(int roomId) throws SQLException {
        try (
                Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
                PreparedStatement statement = connection.prepareStatement(String.format(
                        "SELECT * FROM hotel.room WHERE id = %d", roomId));
                ResultSet resultSet = statement.executeQuery()
        ) {
            if (resultSet.next()) {
                return mapRoom(resultSet);
            }

            return null;
        }
    }

    /**
     * Получает услугу по ее идентификатору из базы данных.
     * @param serviceId идентификатор услуги.
     * @return объект {@link Service}.
     * @throws SQLException если произошла ошибка при доступе к данным из базы данных.
     */
    private Service getServiceById(int serviceId) throws SQLException {
        try (
                Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
                PreparedStatement statement = connection.prepareStatement(String.format(
                        "SELECT * FROM hotel.service WHERE id = %d", serviceId));
                ResultSet resultSet = statement.executeQuery()
        ) {
            if (resultSet.next()) {
                return mapService(resultSet);
            }

            return null;
        }
    }

    /**
     * Преобразует объект {@link Timestamp} в {@link LocalDateTime}.
     * @param timestamp объект {@link Timestamp}.
     * @return объект {@link LocalDateTime}.
     */
    private LocalDateTime parseFromTimestamp(Timestamp timestamp) {
        LocalDateTime time = null;
        if (timestamp != null) {
            time = timestamp.toLocalDateTime();
        }

        return time;
    }
}
