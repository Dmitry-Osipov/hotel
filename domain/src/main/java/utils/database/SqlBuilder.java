package utils.database;

import essence.Identifiable;
import essence.person.Client;
import essence.provided.ProvidedService;
import essence.reservation.RoomReservation;
import essence.room.Room;
import essence.service.Service;
import utils.exceptions.ErrorMessages;
import utils.exceptions.TechnicalException;

import java.lang.reflect.Field;

/**
 * Класс SqlBuilder предоставляет удобные методы для построения SQL-запросов для операций вставки и обновления данных в
 * базе данных. Этот класс содержит статические методы, которые генерируют SQL-запросы в зависимости от типа
 * переданного объекта.
 */
public final class SqlBuilder {
    private SqlBuilder() {
    }

    /**
     * Генерирует SQL-запрос для операции вставки данных в базу данных на основе переданного объекта.
     * @param essence объект, для которого генерируется SQL-запрос.
     * @return SQL-запрос для операции вставки.
     * @throws TechnicalException если не найден обработчик для переданного типа объекта.
     */
    public static <T extends Identifiable> String sqlInsertIntoBuild(T essence) {
        Class<?> clazz = essence.getClass();
        String sql;
        if (clazz.equals(Room.class) || clazz.equals(Service.class) || clazz.equals(Client.class)) {
            sql = sqlSimpleClassesInsertIntoBuild(essence);
        } else if (clazz.equals(RoomReservation.class) || clazz.equals(ProvidedService.class)) {
            sql = sqlHardClassesInsertIntoBuild(essence);
        } else {
            throw new TechnicalException(ErrorMessages.NO_HANDLER.getMessage());
        }

        return sql;
    }

    /**
     * Генерирует SQL-запрос для операции обновления данных в базе данных на основе переданного объекта.
     * @param essence объект, для которого генерируется SQL-запрос.
     * @return SQL-запрос для операции обновления.
     * @throws TechnicalException если не найден обработчик для переданного типа объекта.
     */
    public static <T extends Identifiable> String sqlUpdateBuild(T essence) {
        Class<?> clazz = essence.getClass();
        String sql;
        if (clazz.equals(Room.class) || clazz.equals(Service.class) || clazz.equals(Client.class)) {
            sql = sqlSimpleClassesUpdateBuild(essence);
        } else if (clazz.equals(RoomReservation.class) || clazz.equals(ProvidedService.class)) {
            sql = sqlHardClassesUpdateBuild(essence);
        } else {
            throw new TechnicalException(ErrorMessages.NO_HANDLER.getMessage());
        }

        return sql;
    }

    /**
     * Преобразует строку из формата camelCase в snake_case.
     * @param str строка в формате camelCase.
     * @return строка в формате snake_case.
     */
    public static String parseStringFromCamelCaseToSnakeCase(String str) {
        StringBuilder snakeCase = new StringBuilder();
        char[] chars = str.toCharArray();

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
     * Создает SQL запрос для вставки данных в таблицу по сущностям {@link Room}, {@link Service} и {@link Client}.
     * @param essence объект, данные которого нужно вставить в таблицу.
     * @return SQL запрос для вставки данных в таблицу room, client или service.
     */
    private static <T extends Identifiable> String sqlSimpleClassesInsertIntoBuild(T essence) {
        Class<?> clazz = essence.getClass();
        Field[] fields = clazz.getDeclaredFields();
        StringBuilder sql =
                new StringBuilder("INSERT INTO " + "hotel." +
                        parseStringFromCamelCaseToSnakeCase(clazz.getSimpleName()) + " (");
        for (int i = 1; i < fields.length; i++) {
            if (i > 1) {
                sql.append(", ");
            }
            sql.append(parseStringFromCamelCaseToSnakeCase(fields[i].getName()));
        }

        sql.append(") VALUES (");
        for (int i = 1; i < fields.length; i++) {
            if (i > 1) {
                sql.append(", ");
            }
            sql.append("?");
        }
        sql.append(")");

        return sql.toString();
    }

    /**
     * Создает SQL запрос для обновления данных в таблице по сущностям {@link Room}, {@link Service} и {@link Client}.
     * @param essence объект, данные которого нужно обновить в таблице.
     * @return SQL запрос для обновления данных в таблицу room, client или service.
     */
    private static <T extends Identifiable> String sqlSimpleClassesUpdateBuild(T essence) {
        Class<?> clazz = essence.getClass();
        StringBuilder sql =
                new StringBuilder("UPDATE " + "hotel." +
                        parseStringFromCamelCaseToSnakeCase(clazz.getSimpleName()) + " SET ");
        Field[] fields = clazz.getDeclaredFields();
        for (int i = 1; i < fields.length; i++) {
            if (i > 1) {
                sql.append(", ");
            }
            sql.append(parseStringFromCamelCaseToSnakeCase(fields[i].getName())).append(" = ?");
        }
        sql.append(" WHERE id = ").append(essence.getId());

        return sql.toString();
    }

    /**
     * Создает SQL запрос для вставки данных в таблицу по сущностям {@link RoomReservation} и {@link ProvidedService}.
     * @param essence объект, данные которого нужно вставить в таблицу.
     * @return SQL запрос для вставки данных в таблицу room_reservation или provided_service.
     */
    private static <T extends Identifiable> String sqlHardClassesInsertIntoBuild(T essence) {
        StringBuilder sql = new StringBuilder(sqlSimpleClassesInsertIntoBuild(essence));
        Class<?> clazz = essence.getClass();

        if (clazz.equals(RoomReservation.class)) {
            int clientsIndex = sql.lastIndexOf("clients");
            sql.delete(clientsIndex, clientsIndex + "clients".length() + 2);

            int roomIndex = sql.lastIndexOf("room");
            sql.replace(roomIndex, roomIndex + "room".length(), "room_id");

            int questionIndex = sql.lastIndexOf("?, ");
            sql.delete(questionIndex, questionIndex + "?, ".length());
        } else if (clazz.equals(ProvidedService.class)) {
            int clientIndex = sql.lastIndexOf("client");
            sql.replace(clientIndex, clientIndex + "client".length(), "client_id");

            int serviceIndex = sql.indexOf(" service");
            sql.replace(serviceIndex, serviceIndex + " service".length(), " service_id");
        }

        return sql.toString();
    }

    /**
     * Создает SQL запрос для обновления данных в таблице по сущностям {@link RoomReservation} и
     * {@link ProvidedService}.
     * @param essence объект, данные которого нужно обновить в таблице.
     * @return SQL запрос для обновления данных в таблицу room_reservation или provided_service.
     */
    private static <T extends Identifiable> String sqlHardClassesUpdateBuild(T essence) {
        StringBuilder sql = new StringBuilder(sqlSimpleClassesUpdateBuild(essence));
        Class<?> clazz = essence.getClass();

        if (clazz.equals(RoomReservation.class)) {
            int clientsIndex = sql.lastIndexOf("clients = ?, ");
            sql.delete(clientsIndex, clientsIndex + "clients = ?, ".length());

            int roomIndex = sql.lastIndexOf("room");
            sql.replace(roomIndex, roomIndex + "room".length(), "room_id");
        } else if (clazz.equals(ProvidedService.class)) {
            int clientIndex = sql.lastIndexOf("client");
            sql.replace(clientIndex, clientIndex + "client".length(), "client_id");

            int serviceIndex = sql.indexOf(" service");
            sql.replace(serviceIndex, serviceIndex + " service".length(), " service_id");
        }

        return sql.toString();
    }
}
