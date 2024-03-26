package dao;

import essence.Identifiable;
import essence.person.Client;
import essence.provided.ProvidedService;
import essence.reservation.RoomReservation;
import essence.room.Room;
import essence.service.Service;
import utils.exceptions.ErrorMessages;
import utils.exceptions.TechnicalException;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Класс PreparerStatement предоставляет статические методы для подготовки параметров PreparedStatement на основе
 * переданных сущностей. Этот класс используется для установки параметров PreparedStatement в зависимости от типа
 * переданной сущности.
 */
public final class PreparerStatement {
    private PreparerStatement() {
    }

    /**
     * Подготавливает параметры {@link PreparedStatement} на основе переданной сущности.
     * @param entity сущность, для которой подготавливаются параметры.
     * @param statement объект {@link PreparedStatement}, в который будут установлены параметры.
     * @throws SQLException если произошла ошибка при установке параметров в {@link PreparedStatement}.
     */
    public static <T extends Identifiable> void prepareStatement(T entity, PreparedStatement statement) throws
            SQLException {
        switch (entity.getClass().getSimpleName()) {
            case "Room" -> prepareRoom((Room) entity, statement);
            case "Service" -> prepareService((Service) entity, statement);
            case "Client" -> prepareClient((Client) entity, statement);
            case "RoomReservation" -> prepareRoomReservation((RoomReservation) entity, statement);
            case "ProvidedService" -> prepareProvidedService((ProvidedService) entity, statement);
            default -> throw new TechnicalException(ErrorMessages.NO_HANDLER.getMessage());
        }
    }

    /**
     * Подготавливает параметры {@link PreparedStatement} на основе сущности {@link Room}.
     * @param room объект {@link Room}, данные которого будут установлены в {@link PreparedStatement}.
     * @param statement объект {@link PreparedStatement}, в который будут установлены параметры.
     * @throws SQLException если произошла ошибка при установке параметров в {@link PreparedStatement}.
     */
    private static void prepareRoom(Room room, PreparedStatement statement) throws SQLException {
        statement.setInt(1, room.getId());
        statement.setInt(2, room.getPrice());
        statement.setInt(3, room.getNumber());
        statement.setInt(4, room.getCapacity());
        statement.setString(5, String.valueOf(room.getStatus()));
        statement.setInt(6, room.getStars());
        statement.setTimestamp(7, parseFromLocalDateTime(room.getCheckInTime()));
        statement.setTimestamp(8, parseFromLocalDateTime(room.getCheckOutTime()));
    }

    /**
     * Подготавливает параметры {@link PreparedStatement} на основе сущности {@link Service}.
     * @param service объект {@link Service}, данные которого будут установлены в {@link PreparedStatement}.
     * @param statement объект {@link PreparedStatement}, в который будут установлены параметры.
     * @throws SQLException если произошла ошибка при установке параметров в {@link PreparedStatement}.
     */
    private static void prepareService(Service service, PreparedStatement statement) throws SQLException {
        statement.setInt(1, service.getId());
        statement.setInt(2, service.getPrice());
        statement.setString(3, String.valueOf(service.getName()));
        statement.setString(4, String.valueOf(service.getStatus()));
        statement.setTimestamp(5, parseFromLocalDateTime(service.getServiceTime()));
    }

    /**
     * Подготавливает параметры {@link PreparedStatement} на основе сущности {@link Client}.
     * @param client объект {@link Client}, данные которого будут установлены в {@link PreparedStatement}.
     * @param statement объект {@link PreparedStatement}, в который будут установлены параметры.
     * @throws SQLException если произошла ошибка при установке параметров в {@link PreparedStatement}.
     */
    private static void prepareClient(Client client, PreparedStatement statement) throws SQLException {
        statement.setInt(1, client.getId());
        statement.setString(2, client.getFio());
        statement.setString(3, client.getPhoneNumber());
        statement.setTimestamp(4, parseFromLocalDateTime(client.getCheckInTime()));
        statement.setTimestamp(5, parseFromLocalDateTime(client.getCheckOutTime()));
    }

    /**
     * Подготавливает параметры {@link PreparedStatement} на основе сущности {@link RoomReservation}.
     * @param roomReservation объект {@link RoomReservation}, данные которого будут установлены в
     * {@link PreparedStatement}.
     * @param statement объект {@link PreparedStatement}, в который будут установлены параметры.
     * @throws SQLException если произошла ошибка при установке параметров в {@link PreparedStatement}.
     */
    private static void prepareRoomReservation(RoomReservation roomReservation, PreparedStatement statement) throws
            SQLException {
        statement.setInt(1, roomReservation.getId());
        statement.setInt(2, roomReservation.getRoom().getId());
        statement.setTimestamp(3, parseFromLocalDateTime(roomReservation.getCheckInTime()));
        statement.setTimestamp(4, parseFromLocalDateTime(roomReservation.getCheckOutTime()));
    }

    /**
     * Подготавливает параметры {@link PreparedStatement} на основе сущности {@link ProvidedService}.
     * @param providedService объект {@link ProvidedService}, данные которого будут установлены в
     * {@link PreparedStatement}.
     * @param statement объект {@link PreparedStatement}, в который будут установлены параметры.
     * @throws SQLException если произошла ошибка при установке параметров в {@link PreparedStatement}.
     */
    private static void prepareProvidedService(ProvidedService providedService, PreparedStatement statement) throws
            SQLException {
        statement.setInt(1, providedService.getId());
        statement.setInt(2, providedService.getBeneficiaries().getFirst().getId());
        statement.setInt(3, providedService.getService().getId());
        statement.setTimestamp(4, parseFromLocalDateTime(providedService.getServiceTime()));
    }

    /**
     * Преобразует объект {@link LocalDateTime} в {@link Timestamp}.
     * @param dateTime объект {@link LocalDateTime}
     * @return объект {@link Timestamp}
     */
    private static Timestamp parseFromLocalDateTime(LocalDateTime dateTime) {
        Timestamp time = null;
        if (dateTime != null) {
            time = Timestamp.valueOf(dateTime);
        }

        return time;
    }
}
